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
    val description: String,
    val author: String,
    val date: String,
    val category: AnnouncementCategory,
    @DrawableRes val profile: Int,
    @DrawableRes val mainImage: Int,
    @field:DrawableRes val imageRes: Int? = null,
    // @DrawableRes val images: List<Int>,
    @DrawableRes val secondaryImage: Int,
    @DrawableRes val tertiaryImage: Int,
    val imageUrl: String? = null,
    val isSaved: Boolean = false,
    val isFeatured: Boolean = false,
    val likes: Int,
    val isLikedByUser: Boolean,
    val isSavedOnCalendar: Boolean,
)

