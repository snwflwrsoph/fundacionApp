package com.fer.app.data.fake

import com.fer.app.data.NoticiaRepository
import com.fer.app.model.Announcement
import com.fer.app.model.Noticia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Noticias en memoria. Mientras no haya backend, todos los anuncios abren la misma noticia de ejemplo
 * con el título del anuncio tocado; los "me gusta" se llevan por anuncio y se pierden al cerrar la app.
 */
class NoticiaRepositoryFake : NoticiaRepository {
    private val _noticias = MutableStateFlow<Map<String, Noticia>>(emptyMap())
    override val noticias: StateFlow<Map<String, Noticia>> = _noticias.asStateFlow()

    override suspend fun cargar(anuncio: Announcement) {
        _noticias.update { actuales ->
            if (anuncio.id in actuales) {
                actuales
            } else {
                val imagen = anuncio.imageRes ?: FakeNoticias.ejemplo.imagenPrincipal
                actuales + (anuncio.id to FakeNoticias.ejemplo.copy(
                    id = anuncio.id,
                    titulo = anuncio.title,
                    imagenPrincipal = imagen,
                ))
            }
        }
    }

    override suspend fun alternarLike(id: String) {
        _noticias.update { actuales ->
            val noticia = actuales[id] ?: return@update actuales
            val ahoraLeGusta = !noticia.isLikedByUser
            actuales + (id to noticia.copy(
                isLikedByUser = ahoraLeGusta,
                likes = noticia.likes + if (ahoraLeGusta) 1 else -1
            ))
        }
    }

    override suspend fun alternarGuardarCalendario(id: String) {
        _noticias.update { actuales ->
            val noticia = actuales[id] ?: return@update actuales
            val ahoraGuardadoCalendario = !noticia.isSavedOnCalendar
            actuales + (id to noticia.copy(isSavedOnCalendar = ahoraGuardadoCalendario))
        }
    }
}
