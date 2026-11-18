package com.fer.app.ui

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.fer.app.data.AjustesRepository
import com.fer.app.data.AnnouncementRepository
import com.fer.app.data.CatalogoRepository
import com.fer.app.data.PerfilRepository
import com.fer.app.data.fake.AjustesRepositoryPrefs
import com.fer.app.data.fake.CatalogoRepositoryFake
import com.fer.app.data.fake.PerfilRepositoryPrefs
import com.fer.app.data.firebase.AnnouncementRepositoryFirestore
import com.fer.app.data.firebase.normalizar
import com.fer.app.model.AjustesApp
import com.fer.app.model.Announcement
import com.fer.app.model.Catalogo
import com.fer.app.model.EstadoCarga
import com.fer.app.model.ID_UNIVERSIDAD_OTRA
import com.fer.app.model.NivelEducativo
import com.fer.app.model.PerfilUsuario
import com.fer.app.model.TamanoTexto
import com.fer.app.model.TipoUniversidad
import com.fer.app.model.TipoUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.distinctUntilChanged

class AppViewModel(
    application: Application,
    private val perfilRepository: PerfilRepository,
    private val ajustesRepository: AjustesRepository,
    private val catalogoRepository: CatalogoRepository,
    private val announcementRepository: AnnouncementRepository
) : AndroidViewModel(application) {

    val perfil: StateFlow<PerfilUsuario> = perfilRepository.perfil
    val ajustes: StateFlow<AjustesApp> = ajustesRepository.ajustes

    /** Anuncios que ve este usuario: los publicados, filtrados por la audiencia que eligió. */
    val anuncios: StateFlow<List<Announcement>> = combine(
        announcementRepository.anuncios,
        perfilRepository.perfil
    ) { lista, perfil ->
        lista.filter { a ->
            val visible = esParaAudiencia(a, perfil.tipoUsuario)
            if (!visible) {
                Log.w("FER", "[${a.id}] '${a.title}' oculto por audiencia ${a.audiences} (usuario=${perfil.tipoUsuario})")
            }
            visible
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val estadoAnuncios: StateFlow<EstadoCarga> = announcementRepository.estado
    val todosLosAnuncios: StateFlow<List<Announcement>> = announcementRepository.anuncios
    val notificacionesLeidas: StateFlow<Set<String>> = announcementRepository.leidas

    private val _catalogo = MutableStateFlow(Catalogo())
    val catalogo: StateFlow<Catalogo> = _catalogo.asStateFlow()

    init {
        viewModelScope.launch { _catalogo.value = catalogoRepository.cargarCatalogo() }
        viewModelScope.launch {
            combine(perfilRepository.perfil, ajustesRepository.ajustes) { perfil, ajustes ->
                ajustes.notificaciones to perfil.tipoUsuario
            }
                .distinctUntilChanged()
                .collect { (activas, tipo) -> sincronizarTemas(activas, tipo) }
        }
    }

    // Flujo de preferencias

    fun seleccionarTipoUsuario(tipo: TipoUsuario) = actualizarPerfil { it.copy(tipoUsuario = tipo) }

    fun seleccionarNivel(nivel: NivelEducativo) = actualizarPerfil {
        it.copy(
            nivelEducativo = nivel,
            tipoUniversidad = it.tipoUniversidad.takeIf { nivel == NivelEducativo.UNIVERSIDAD },
            universidadId = it.universidadId.takeIf { nivel == NivelEducativo.UNIVERSIDAD },
            universidadOtra = it.universidadOtra.takeIf { nivel == NivelEducativo.UNIVERSIDAD }
        )
    }

    fun seleccionarTipoUniversidad(tipo: TipoUniversidad) = actualizarPerfil {
        it.copy(tipoUniversidad = tipo, universidadId = null, universidadOtra = null)
    }

    fun seleccionarUniversidad(id: Int, textoOtra: String) = actualizarPerfil { perfil ->
        perfil.copy(
            universidadId = id,
            universidadOtra = textoOtra.takeIf { id == ID_UNIVERSIDAD_OTRA && it.isNotBlank() }
        )
    }

    fun completarOnboarding() = actualizarPerfil { it.copy(onboardingCompleto = true) }

    // Feed

    fun alternarGuardado(anuncio: Announcement) {
        viewModelScope.launch { announcementRepository.alternarGuardado(anuncio.id) }
    }

    fun reintentarCargaAnuncios() {
        viewModelScope.launch { announcementRepository.reintentar() }
    }

    // Detalle de anuncio

    fun alternarLike(id: String) {
        viewModelScope.launch { announcementRepository.alternarLike(id) }
    }

    /** Se llama al abrir un anuncio: su notificación deja de aparecer como nueva. */
    fun marcarLeida(id: String) {
        viewModelScope.launch { announcementRepository.marcarLeida(id) }
    }

    // Configuración

    fun cambiarNotificaciones(valor: Boolean) = actualizarAjustes { it.copy(notificaciones = valor) }
    fun cambiarModoOscuro(valor: Boolean) = actualizarAjustes { it.copy(modoOscuro = valor) }
    fun cambiarCompartirDatos(valor: Boolean) = actualizarAjustes { it.copy(compartirDatos = valor) }
    fun cambiarTamanoTexto(tamano: TamanoTexto) = actualizarAjustes { it.copy(tamanoTexto = tamano) }

    fun enviarReporteProblema(categoria: String, descripcion: String, contacto: String) {
        val ahora = System.currentTimeMillis()
        val prefs = getApplication<Application>().getSharedPreferences("fer_reportes_prefs", Context.MODE_PRIVATE)
        val guardados = prefs.getStringSet("reportes_locales", emptySet())?.toMutableSet() ?: mutableSetOf()
        guardados.add("$ahora|$categoria|$contacto|$descripcion")
        prefs.edit().putStringSet("reportes_locales", guardados).apply()

        val reporte = hashMapOf(
            "categoria" to categoria,
            "descripcion" to descripcion,
            "contacto" to contacto,
            "tipoUsuario" to (perfil.value.tipoUsuario?.name ?: "SIN_DEFINIR"),
            "dispositivo" to "${Build.MANUFACTURER} ${Build.MODEL}",
            "fecha" to FieldValue.serverTimestamp(),
            "timestampMillis" to ahora
        )
        runCatching {
            FirebaseFirestore.getInstance().collection("reportes")
                .add(reporte)
                .addOnSuccessListener {
                    Log.d("FER", "Reporte enviado a Firestore: ${it.id}")
                }
                .addOnFailureListener { error ->
                    Log.w("FER", "No se pudo sincronizar el reporte en Firestore; guardado localmente", error)
                }
        }
    }

    // Internos

    private fun actualizarPerfil(transformar: (PerfilUsuario) -> PerfilUsuario) {
        viewModelScope.launch { perfilRepository.guardar(transformar(perfilRepository.perfil.value)) }
    }

    private fun actualizarAjustes(transformar: (AjustesApp) -> AjustesApp) {
        viewModelScope.launch { ajustesRepository.guardar(transformar(ajustesRepository.ajustes.value)) }
    }

    /** Activa/desactiva FCM y sincroniza la suscripción a temas según la preferencia y audiencia. */
    private fun sincronizarTemas(activas: Boolean, tipo: TipoUsuario?) {
        runCatching {
            val mensajeria = FirebaseMessaging.getInstance()
            mensajeria.isAutoInitEnabled = activas
            if (!activas) {
                NotificationManagerCompat.from(getApplication()).cancelAll()
            }
            listOf("general", "todos").forEach { temaGeneral ->
                val tarea = if (activas) mensajeria.subscribeToTopic(temaGeneral)
                else mensajeria.unsubscribeFromTopic(temaGeneral)
                tarea.addOnCompleteListener {
                    Log.d("FER", "Tema $temaGeneral (${if (activas) "suscrito" else "baja"}): ${it.isSuccessful}")
                }
            }
            TipoUsuario.entries.forEach { t ->
                val suscribir = activas && t == tipo
                val tarea = if (suscribir) mensajeria.subscribeToTopic(temaDe(t))
                else mensajeria.unsubscribeFromTopic(temaDe(t))
                tarea.addOnCompleteListener {
                    Log.d("FER", "Tema ${temaDe(t)} (${if (suscribir) "suscrito" else "baja"}): ${it.isSuccessful}")
                }
            }
        }
    }

    override fun onCleared() {
        (announcementRepository as? AutoCloseable)?.close()
        super.onCleared()
    }

    companion object {
        private fun audienciaDe(tipo: TipoUsuario) = when (tipo) {
            TipoUsuario.BECARIO -> "becario"
            TipoUsuario.ASPIRANTE -> "aspirante"
        }
        // Para FCM
        private fun temaDe(tipo: TipoUsuario) = when (tipo) {
            TipoUsuario.BECARIO -> "becarios"
            TipoUsuario.ASPIRANTE -> "aspirantes"
        }

        private fun esParaAudiencia(anuncio: Announcement, tipo: TipoUsuario?): Boolean {
            if (tipo == null || anuncio.audiences.isEmpty()) return true
            val buscada = audienciaDe(tipo)
            return anuncio.audiences.any {
                val a = it.normalizar().removeSuffix("s")   // "Becarios" -> "becario", "Todos" -> "todo"
                a == buscada || a == "todo" || a == "general"
            }
        }

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return AppViewModel(
                    application = application,
                    perfilRepository = PerfilRepositoryPrefs(application),
                    ajustesRepository = AjustesRepositoryPrefs(application),
                    catalogoRepository = CatalogoRepositoryFake(),
                    // Para trabajar sin Firebase: AnnouncementRepositoryFake()
                    announcementRepository = AnnouncementRepositoryFirestore(application)
                ) as T
            }
        }
    }
}