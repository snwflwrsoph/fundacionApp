package com.fer.app.data.fake

import com.fer.app.data.AnnouncementRepository
import com.fer.app.model.Announcement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Anuncios de prueba en memoria; "guardar" solo cambia la bandera mientras la app está abierta. */
class AnnouncementRepositoryFake(
    inicial: List<Announcement> = FakeAnnouncements.many
) : AnnouncementRepository {
    private val _anuncios = MutableStateFlow(inicial)
    override val anuncios: StateFlow<List<Announcement>> = _anuncios.asStateFlow()

    override suspend fun alternarGuardado(id: String) {
        _anuncios.update { lista ->
            lista.map { if (it.id == id) it.copy(isSaved = !it.isSaved) else it }
        }
    }
}
