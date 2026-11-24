package com.fer.app.data.fake

import com.fer.app.data.AnnouncementRepository
import com.fer.app.model.Announcement
import com.fer.app.model.EstadoCarga
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AnnouncementRepositoryFake(
    inicial: List<Announcement> = FakeAnnouncements.many
) : AnnouncementRepository {
    private val _anuncios = MutableStateFlow(inicial)
    override val anuncios: StateFlow<List<Announcement>> = _anuncios.asStateFlow()

    override val estado: StateFlow<EstadoCarga> = MutableStateFlow<EstadoCarga>(EstadoCarga.Listo).asStateFlow()

    private val _leidas = MutableStateFlow<Set<String>>(emptySet())
    override val leidas: StateFlow<Set<String>> = _leidas.asStateFlow()

    override suspend fun alternarGuardado(id: String) = modificar(id) { it.copy(isSaved = !it.isSaved) }

    override suspend fun alternarLike(id: String) = modificar(id) {
        val ahoraLeGusta = !it.isLikedByUser
        it.copy(isLikedByUser = ahoraLeGusta, likes = it.likes + if (ahoraLeGusta) 1 else -1)
    }

    override suspend fun marcarLeida(id: String) {
        _leidas.update { it + id }
    }

    override suspend fun reintentar() = Unit
    override suspend fun refrescar(id: String) = Unit


    private fun modificar(id: String, cambio: (Announcement) -> Announcement) {
        _anuncios.update { lista -> lista.map { if (it.id == id) cambio(it) else it } }
    }
}
