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
    private val enCalendario = MutableStateFlow(leerConjunto(CLAVE_CALENDARIO))

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
            escuchar()
            return
        }
        auth.signInAnonymously().addOnCompleteListener { tarea ->
            if (!tarea.isSuccessful) {
                // Se intenta leer de todas formas: si las reglas permiten lectura pública funciona igual.
                Log.w(TAG, "No se pudo iniciar sesión anónima (¿está activada en Firebase Auth?)", tarea.exception)
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

    override fun close() {
        reloj.removeCallbacks(refrescoPorHora)
        registro?.remove()
        registro = null
    }

    // ---------- Acciones del usuario ----------

    override suspend fun alternarLike(id: String) {
        val yaLeGustaba = id in conLike.value
        actualizarConjunto(conLike, CLAVE_LIKES, id, !yaLeGustaba)
        firestore.collection(COLECCION).document(id)
            .update("likes", FieldValue.increment(if (yaLeGustaba) -1L else 1L))
            .addOnFailureListener { error ->
                Log.w(TAG, "No se pudo actualizar el like de $id", error)
                actualizarConjunto(conLike, CLAVE_LIKES, id, yaLeGustaba) // deshace el cambio local
            }
    }

    override suspend fun alternarGuardado(id: String) =
        actualizarConjunto(guardados, CLAVE_GUARDADOS, id, id !in guardados.value)

    override suspend fun alternarGuardarCalendario(id: String) =
        actualizarConjunto(enCalendario, CLAVE_CALENDARIO, id, id !in enCalendario.value)

    override suspend fun marcarLeida(id: String) {
        if (id in _leidas.value) return
        val nuevas = _leidas.value + id
        _leidas.value = nuevas
        prefs.edit().putStringSet(CLAVE_LEIDAS, nuevas).apply()
    }

    // ---------- Internos ----------

    private fun reconstruir() {
        val ahora = System.currentTimeMillis()
        remotos.value = documentos
            .mapNotNull { doc ->
                runCatching { doc.aAnnouncement(ahora) }
                    .onFailure { Log.e(TAG, "[${doc.id}] no se pudo leer el documento", it) }
                    .getOrNull()
            }
            .sortedByDescending { it.timestampMillis }
        publicar()
    }

    private fun publicar() {
        val guardadosAhora = guardados.value
        val likesAhora = conLike.value
        val calendarioAhora = enCalendario.value
        _anuncios.value = remotos.value.map {
            it.copy(
                isSaved = it.id in guardadosAhora,
                isLikedByUser = it.id in likesAhora,
                isSavedOnCalendar = it.id in calendarioAhora,
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
        const val CLAVE_CALENDARIO = "calendar_announcement_ids"
        const val CLAVE_LEIDAS = "read_announcement_ids"
    }
}