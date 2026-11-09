package com.fer.app.model

import androidx.annotation.DrawableRes

enum class AnnouncementCategory(val label: String) {
    ANNIVERSARY("80 aniversario"),
    CALL("Convocatoria"),
    STORIES("Historias"),
    ALLIANCES("Alianzas"),
    EVENTS("Eventos"),
}

data class Announcement(
    val id: String,
    val title: String,
    val summary: String,
    val category: AnnouncementCategory,
    @field:DrawableRes val imageRes: Int? = null,
    val imageUrl: String? = null,
    val isSaved: Boolean = false,
    val isFeatured: Boolean = false,
)

