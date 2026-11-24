package com.fer.app.data

import com.fer.app.model.AjustesApp
import com.fer.app.model.Announcement
import com.fer.app.model.Catalogo
import com.fer.app.model.EstadoCarga
import com.fer.app.model.PerfilUsuario
import kotlinx.coroutines.flow.StateFlow

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
    val estado: StateFlow<EstadoCarga>
    val leidas: StateFlow<Set<String>>

    suspend fun alternarGuardado(id: String)
    suspend fun alternarLike(id: String)
    suspend fun marcarLeida(id: String)
    suspend fun reintentar()

    suspend fun refrescar(id: String)
}

interface CatalogoRepository {
    suspend fun cargarCatalogo(): Catalogo
}