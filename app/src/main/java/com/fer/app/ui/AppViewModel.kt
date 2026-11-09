package com.fer.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fer.app.data.AjustesRepository
import com.fer.app.data.AnnouncementRepository
import com.fer.app.data.CatalogoRepository
import com.fer.app.data.NoticiaRepository
import com.fer.app.data.PerfilRepository
import com.fer.app.data.fake.AjustesRepositoryFake
import com.fer.app.data.fake.AnnouncementRepositoryFake
import com.fer.app.data.fake.CatalogoRepositoryFake
import com.fer.app.data.fake.NoticiaRepositoryFake
import com.fer.app.data.fake.PerfilRepositoryFake
import com.fer.app.model.AjustesApp
import com.fer.app.model.Announcement
import com.fer.app.model.Catalogo
import com.fer.app.model.ID_UNIVERSIDAD_OTRA
import com.fer.app.model.NivelEducativo
import com.fer.app.model.Noticia
import com.fer.app.model.PerfilUsuario
import com.fer.app.model.TipoUniversidad
import com.fer.app.model.TipoUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estado compartido de toda la app (perfil, ajustes y catálogos).
 * Las pantallas solo leen estos StateFlow y llaman a sus funciones; no conocen de dónde vienen los datos.
 */
class AppViewModel(
    private val perfilRepository: PerfilRepository,
    private val ajustesRepository: AjustesRepository,
    private val catalogoRepository: CatalogoRepository,
    private val announcementRepository: AnnouncementRepository,
    private val noticiaRepository: NoticiaRepository
) : ViewModel() {

    val perfil: StateFlow<PerfilUsuario> = perfilRepository.perfil
    val ajustes: StateFlow<AjustesApp> = ajustesRepository.ajustes
    val anuncios: StateFlow<List<Announcement>> = announcementRepository.anuncios
    val noticias: StateFlow<Map<String, Noticia>> = noticiaRepository.noticias

    private val _catalogo = MutableStateFlow(Catalogo())
    val catalogo: StateFlow<Catalogo> = _catalogo.asStateFlow()

    init {
        viewModelScope.launch { _catalogo.value = catalogoRepository.cargarCatalogo() }
    }

    // ---------- Flujo de preferencias ----------

    fun seleccionarTipoUsuario(tipo: TipoUsuario) = actualizarPerfil { it.copy(tipoUsuario = tipo) }

    fun seleccionarNivel(nivel: NivelEducativo) = actualizarPerfil {
        it.copy(
            nivelEducativo = nivel,
            // Si ya no es universidad, se descartan las respuestas de las pantallas de universidad.
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

    // ---------- Feed ----------

    fun alternarGuardado(anuncio: Announcement) {
        viewModelScope.launch { announcementRepository.alternarGuardado(anuncio.id) }
    }

    // ---------- Detalle de anuncio (noticia) ----------

    fun cargarNoticia(anuncio: Announcement) {
        viewModelScope.launch { noticiaRepository.cargar(anuncio) }
    }

    fun alternarLikeNoticia(id: String) {
        viewModelScope.launch { noticiaRepository.alternarLike(id) }
    }

    // ---------- Configuración ----------

    fun cambiarNotificaciones(valor: Boolean) = actualizarAjustes { it.copy(notificaciones = valor) }
    fun cambiarModoOscuro(valor: Boolean) = actualizarAjustes { it.copy(modoOscuro = valor) }
    fun cambiarLectorPantalla(valor: Boolean) = actualizarAjustes { it.copy(lectorPantalla = valor) }
    fun cambiarCompartirDatos(valor: Boolean) = actualizarAjustes { it.copy(compartirDatos = valor) }

    // ---------- Internos ----------

    private fun actualizarPerfil(transformar: (PerfilUsuario) -> PerfilUsuario) {
        viewModelScope.launch { perfilRepository.guardar(transformar(perfilRepository.perfil.value)) }
    }

    private fun actualizarAjustes(transformar: (AjustesApp) -> AjustesApp) {
        viewModelScope.launch { ajustesRepository.guardar(transformar(ajustesRepository.ajustes.value)) }
    }

    companion object {
        /** Único lugar donde se eligen las implementaciones: hoy los datos hardcodeados. */
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = AppViewModel(
                perfilRepository = PerfilRepositoryFake(),
                ajustesRepository = AjustesRepositoryFake(),
                catalogoRepository = CatalogoRepositoryFake(),
                announcementRepository = AnnouncementRepositoryFake(),
                noticiaRepository = NoticiaRepositoryFake()
            ) as T
        }
    }
}
