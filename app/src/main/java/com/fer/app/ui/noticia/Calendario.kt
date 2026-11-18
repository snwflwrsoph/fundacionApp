package com.fer.app.ui.noticia

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import com.fer.app.model.Announcement

fun Context.agregarAlCalendario(anuncio: Announcement) {
    val inicio = anuncio.eventStartMillis ?: return

    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, anuncio.title)
        putExtra(
            CalendarContract.Events.DESCRIPTION,
            anuncio.summary.ifBlank { anuncio.description }
        )
        anuncio.location?.let { putExtra(CalendarContract.Events.EVENT_LOCATION, it) }
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, inicio)
        anuncio.eventEndMillis?.let { putExtra(CalendarContract.EXTRA_EVENT_END_TIME, it) }
    }

    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "No hay una app de calendario instalada", Toast.LENGTH_SHORT).show()
    }
}