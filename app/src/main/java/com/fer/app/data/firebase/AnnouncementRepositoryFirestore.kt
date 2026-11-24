package com.fer.app.data.firebase

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.fer.app.data.AnnouncementRepository
import com.fer.app.model.Announcement
import com.fer.app.model.EstadoCarga
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.firebase.firestore.Source

class AnnouncementRepositoryFirestore(
    context: Context,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
) : AnnouncementRepository, AutoCloseable {

    // Mismo archivo que usaba la versión local, para conservar los guardados existentes.
    private val prefs = context.getSharedPreferences("fer_anuncios_prefs", Context.MODE_PRIVATE)

    private val remotos = MutableStateFlow<List<Announcement>>(emptyList())
    private val guardados = MutableStateFlow(leerConjunto(CLAVE_GUARDADOS))
    private val conLike = MutableStateFlow(leerConjunto(CLAVE_LIKES))
    private val likesSincronizados = MutableStateFlow(leerConjunto(CLAVE_LIKES_SINCRONIZADOS))
    private val likesEnVuelo = mutableSetOf<String>()
    private val likesBaseServidor = mutableMapOf<String, Int>()

    private val _anuncios = MutableStateFlow<List<Announcement>>(emptyList())
    override val anuncios: StateFlow<List<Announcement>> = _anuncios.asStateFlow()

    private val _estado = MutableStateFlow<EstadoCarga>(EstadoCarga.Cargando)
    override val estado: StateFlow<EstadoCarga> = _estado.asStateFlow()

    private val _leidas = MutableStateFlow(leerConjunto(CLAVE_LEIDAS))
    override val leidas: StateFlow<Set<String>> = _leidas.asStateFlow()

    private var registro: ListenerRegistration? = null

    // Últimos documentos recibidos; se vuelven a evaluar cada 30 s por si llegó la hora de publicarEn.
    private var documentos: List<DocumentSnapshot> = emptyList()
    private val reloj = Handler(Looper.getMainLooper())
    private val refrescoPorHora = object : Runnable {
        override fun run() {
            reconstruir()
            reloj.postDelayed(this, 30_000)
        }
    }

    init {
        asegurarSesionYEscuchar()
        reloj.postDelayed(refrescoPorHora, 30_000)
    }

    private fun asegurarSesionYEscuchar() {
        _estado.value = EstadoCarga.Cargando
        if (auth.currentUser != null) {
            Log.d(TAG, "Sesion ya existente, uid=${auth.currentUser?.uid}")
            escuchar()
            return
        }
        auth.signInAnonymously().addOnCompleteListener { tarea ->
            if (!tarea.isSuccessful) {
                // Se intenta leer de todas formas: si las reglas permiten lectura pública funciona igual.
                Log.w(TAG, "No se pudo iniciar sesión anónima (¿está activada en Firebase Auth?)", tarea.exception)
            } else{
                Log.d(TAG, "Sesion anonima OK, uid=${auth.currentUser?.uid}")
            }
            escuchar()
        }
    }

    private fun escuchar() {
        registro?.remove()
        registro = firestore.collection(COLECCION)
            .whereEqualTo("estado", "publicado")
            .addSnapshotListener { instantanea, error ->
                if (error != null) {
                    Log.e(TAG, "Error leyendo '$COLECCION': ${error.code} ${error.message}", error)
                    _estado.value = EstadoCarga.Error("No se pudieron cargar los anuncios.")
                    return@addSnapshotListener
                }
                if (instantanea == null) return@addSnapshotListener

                Log.d(TAG, "Firestore devolvió ${instantanea.size()} docs (desdeCache=${instantanea.metadata.isFromCache})")
                documentos = instantanea.documents
                reconstruir()
                _estado.value = EstadoCarga.Listo
            }
    }

    override suspend fun reintentar() {
        asegurarSesionYEscuchar()
    }

    override suspend fun refrescar(id: String) {
        firestore.collection(COLECCION).document(id)
            .get(Source.SERVER)
            .addOnSuccessListener { doc ->
                if (!doc.exists()) return@addOnSuccessListener
                documentos = documentos.map { if (it.id == doc.id) doc else it }
                reconstruir()
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "No se pudo refrescar el anuncio $id", error)
            }

    }

    override fun close() {
        reloj.removeCallbacks(refrescoPorHora)
        registro?.remove()
        registro = null
    }

    // ---------- Acciones del usuario ----------

    override suspend fun alternarLike(id: String) {
        val yaLeGustaba = id in conLike.value
        val leGustaAhora = !yaLeGustaba
        actualizarConjunto(conLike, CLAVE_LIKES, id, leGustaAhora)

        val sincronizadoEnServidor = id in likesSincronizados.value
        if (leGustaAhora == sincronizadoEnServidor) return

        val delta = if (leGustaAhora) 1L else -1L
        likesEnVuelo.add(id)
        firestore.collection(COLECCION).document(id)
            .update("likes", FieldValue.increment(delta))
            .addOnSuccessListener {
                likesEnVuelo.remove(id)
                likesBaseServidor[id] = ((likesBaseServidor[id] ?: 0) + delta.toInt()).coerceAtLeast(0)
                actualizarConjunto(likesSincronizados, CLAVE_LIKES_SINCRONIZADOS, id, leGustaAhora)
                reconstruir()
            }
            .addOnFailureListener { error ->
                likesEnVuelo.remove(id)
                Log.w(TAG, "No se pudo sincronizar el like de $id en Firestore; se conserva localmente", error)
                reconstruir()
            }
    }

    override suspend fun alternarGuardado(id: String) =
        actualizarConjunto(guardados, CLAVE_GUARDADOS, id, id !in guardados.value)

    override suspend fun marcarLeida(id: String) {
        if (id in _leidas.value) return
        val nuevas = _leidas.value + id
        _leidas.value = nuevas
        prefs.edit().putStringSet(CLAVE_LEIDAS, nuevas).apply()
    }

    // ---------- Internos ----------

    private fun deltaLikeLocal(id: String): Int {
        val leGusta = id in conLike.value
        val sincronizado = id in likesSincronizados.value
        return (if (leGusta) 1 else 0) - (if (sincronizado) 1 else 0)
    }

    private fun reconstruir() {
        val ahora = System.currentTimeMillis()
        remotos.value = documentos
            .mapNotNull { doc ->
                runCatching {
                    val anuncio = doc.aAnnouncement(ahora) ?: return@runCatching null
                    val tieneEscrituraPendiente = doc.id in likesEnVuelo || doc.metadata.hasPendingWrites()
                    val likesServidor = if (!tieneEscrituraPendiente) {
                        if (anuncio.likes == 0 && doc.id in likesSincronizados.value) {
                            val actualizados = likesSincronizados.value - doc.id
                            likesSincronizados.value = actualizados
                            prefs.edit().putStringSet(CLAVE_LIKES_SINCRONIZADOS, actualizados).apply()
                        }
                        likesBaseServidor[doc.id] = anuncio.likes
                        anuncio.likes
                    } else {
                        likesBaseServidor.getOrPut(doc.id) {
                            (anuncio.likes - deltaLikeLocal(doc.id)).coerceAtLeast(0)
                        }
                    }
                    anuncio.copy(likes = likesServidor)
                }
                    .onFailure { Log.e(TAG, "[${doc.id}] no se pudo leer el documento", it) }
                    .getOrNull()
            }
            .sortedByDescending { it.timestampMillis }
        publicar()
    }

    private fun publicar() {
        val guardadosAhora = guardados.value
        val likesAhora = conLike.value
        _anuncios.value = remotos.value.map {
            val leGusta = it.id in likesAhora
            val likesCalculados = (it.likes + deltaLikeLocal(it.id)).coerceAtLeast(if (leGusta) 1 else 0)
            it.copy(
                isSaved = it.id in guardadosAhora,
                isLikedByUser = leGusta,
                likes = likesCalculados
            )
        }
    }

    private fun actualizarConjunto(
        flujo: MutableStateFlow<Set<String>>,
        clave: String,
        id: String,
        incluir: Boolean,
    ) {
        val nuevo = if (incluir) flujo.value + id else flujo.value - id
        flujo.value = nuevo
        prefs.edit().putStringSet(clave, nuevo).apply()
        publicar()
    }

    private fun leerConjunto(clave: String): Set<String> =
        prefs.getStringSet(clave, emptySet())?.toSet().orEmpty()

    private companion object {
        const val TAG = "FER"
        const val COLECCION = "anuncios"
        const val CLAVE_GUARDADOS = "saved_announcement_ids"
        const val CLAVE_LIKES = "liked_announcement_ids"
        const val CLAVE_LIKES_SINCRONIZADOS = "synced_liked_announcement_ids"
        const val CLAVE_LEIDAS = "read_announcement_ids"
    }
}