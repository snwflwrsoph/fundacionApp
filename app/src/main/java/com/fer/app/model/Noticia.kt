package com.fer.app.model

import androidx.annotation.DrawableRes

/** Detalle de un anuncio (pantalla de noticia). El [id] es el mismo que el del [Announcement] del feed. */
data class Noticia(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val autor: String,
    @DrawableRes val perfil: Int,
    @DrawableRes val imagenPrincipal: Int,
    @DrawableRes val imagenSecundaria: Int,
    @DrawableRes val imagenTerciaria: Int,
    val likes: Int,
    val isLikedByUser: Boolean,
    val isSavedOnCalendar: Boolean,
)
