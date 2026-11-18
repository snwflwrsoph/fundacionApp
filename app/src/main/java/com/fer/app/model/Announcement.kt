package com.fer.app.model

import androidx.annotation.DrawableRes

enum class AnnouncementCategory(val label: String) {
    ANNIVERSARY("80 aniversario"),
    CALL("Convocatoria"),
    STORIES("Historias"),
    ALLIANCES("Alianzas"),
    EVENTS("Eventos"),
    /** Cualquier tag que no corresponda a una categoría conocida. */
    GENERAL("Anuncio"),
}

data class Announcement(
    val id: String,
    val title: String,
    val summary: String,
    val description: String,
    val author: String,
    val date: String,
    val category: AnnouncementCategory,
    @DrawableRes val profile: Int? = null,
    @DrawableRes val mainImage: Int? = null,
    @field:DrawableRes val imageRes: Int? = null,
    @DrawableRes val secondaryImage: Int? = null,
    @DrawableRes val tertiaryImage: Int? = null,
    val imageUrl: String? = null,
    val profileUrl: String? = null,
    val secondaryImageUrls: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val audiences: List<String> = emptyList(),
    val isUrgent: Boolean = false,
    val timestampMillis: Long = 0L,
    val isSaved: Boolean = false,
    val isFeatured: Boolean = false,
    val likes: Int,
    val isLikedByUser: Boolean,
    val eventStartMillis: Long? = null,
    val eventEndMillis: Long? = null,
    val location: String? = null,
) {
    val etiqueta: String
        get() = if (category == AnnouncementCategory.GENERAL) {
            tags.firstOrNull()?.takeIf { it.isNotBlank() } ?: category.label
        } else {
            category.label
        }
}

sealed interface EstadoCarga {
    data object Cargando : EstadoCarga
    data object Listo : EstadoCarga
    data class Error(val mensaje: String) : EstadoCarga
}
