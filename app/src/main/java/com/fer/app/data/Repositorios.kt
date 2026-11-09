package com.fer.app.data

import com.fer.app.model.AjustesApp
import com.fer.app.model.Announcement
import com.fer.app.model.Catalogo
import com.fer.app.model.Noticia
import com.fer.app.model.PerfilUsuario
import kotlinx.coroutines.flow.StateFlow

/*
 * Contratos de datos de la app. Hoy los implementan las clases del paquete `data.fake`
 * (datos hardcodeados en memoria). Para conectar el backend basta con crear otra
 * implementación (Retrofit, Room, Firebase, etc.) y cambiarla en AppViewModel.Factory;
 * ninguna pantalla ni el ViewModel necesitan modificarse.
 */

interface PerfilRepository {
    val perfil: StateFlow<PerfilUsuario>
    suspend fun guardar(perfil: PerfilUsuario)
}

interface AjustesRepository {
    val ajustes: StateFlow<AjustesApp>
    suspend fun guardar(ajustes: AjustesApp)
}

interface AnnouncementRepository {
    val anuncios: StateFlow<List<Announcement>>
    suspend fun alternarGuardado(id: String)
}

interface NoticiaRepository {
    /** Noticias ya cargadas, por id de anuncio. */
    val noticias: StateFlow<Map<String, Noticia>>
    /** Pide el detalle del [anuncio] (con backend: llamada de red; hoy: datos de ejemplo). */
    suspend fun cargar(anuncio: Announcement)
    suspend fun alternarLike(id: String)
}

interface CatalogoRepository {
    suspend fun cargarCatalogo(): Catalogo
}
