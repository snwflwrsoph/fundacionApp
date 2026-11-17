package com.fer.app.data.firebase

import android.util.Log
import com.fer.app.model.Announcement
import com.fer.app.model.AnnouncementCategory
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG = "FER"
private const val TOLERANCIA_RELOJ_MS = 5 * 60 * 1000L

private val formatoFecha = ThreadLocal.withInitial {
    SimpleDateFormat("d 'de' MMMM 'del' yyyy", Locale("es", "MX"))
}

// Lecturas seguras: si el campo tiene otro tipo devuelven null en vez de lanzar excepción.
private fun DocumentSnapshot.texto(c: String) = get(c) as? String
private fun DocumentSnapshot.marca(c: String) = get(c) as? Timestamp
private fun DocumentSnapshot.booleano(c: String) = get(c) as? Boolean
private fun DocumentSnapshot.entero(c: String) = (get(c) as? Number)?.toInt()
private fun DocumentSnapshot.lista(c: String): List<String> =
    (get(c) as? List<*>)?.filterIsInstance<String>().orEmpty()

internal fun DocumentSnapshot.aAnnouncement(ahoraMillis: Long): Announcement? {
    val titulo = texto("titulo")?.takeIf { it.isNotBlank() }
    if (titulo == null) {
        Log.w(TAG, "[$id] descartado: sin título")
        return null
    }

    // Publicación programada: si todavía no llega la hora, no se muestra.
    val publicarEn = marca("publicarEn")?.toDate()
    if (publicarEn != null && publicarEn.time > ahoraMillis + TOLERANCIA_RELOJ_MS) {
        Log.w(TAG, "[$id] '$titulo' descartado: publicarEn=$publicarEn es futuro; hora del teléfono=${Date(ahoraMillis)}")
        return null
    }

    val fecha = (marca("fecha") ?: marca("publicarEn"))?.toDate()
    if (fecha == null) Log.w(TAG, "[$id] '$titulo': 'fecha' no es Timestamp (¿string?), se mostrará al final")
    val tags = lista("tags")

    return Announcement(
        id = id,
        title = titulo,
        summary = texto("resumen").orEmpty(),
        description = texto("descripcion").orEmpty(),
        author = texto("autor").orEmpty(),
        date = fecha?.let { formatoFecha.get()!!.format(it) }.orEmpty(),
        category = categoriaDesdeTag(tags.firstOrNull()),
        imageUrl = texto("imagenPrincipal")?.takeIf { it.isNotBlank() },
        profileUrl = texto("autorFotoUrl")?.takeIf { it.isNotBlank() },
        secondaryImageUrls = lista("imagenesSecundarias").filter { it.isNotBlank() },
        tags = tags,
        audiences = lista("audiencias"),
        isUrgent = texto("prioridad") == "urgente",
        timestampMillis = fecha?.time ?: 0L,
        isFeatured = booleano("isFeatured") ?: false,
        likes = entero("likes") ?: 0,
        isLikedByUser = false,        // lo aplica el repositorio
        isSavedOnCalendar = false,    // lo aplica el repositorio
    )
}

/** El primer tag define la categoría; si no coincide con ninguna conocida, GENERAL. */
internal fun categoriaDesdeTag(tag: String?): AnnouncementCategory {
    val t = tag?.normalizar() ?: return AnnouncementCategory.GENERAL
    return when {
        "aniversario" in t -> AnnouncementCategory.ANNIVERSARY
        "convocatoria" in t -> AnnouncementCategory.CALL
        "historia" in t -> AnnouncementCategory.STORIES
        "alianza" in t -> AnnouncementCategory.ALLIANCES
        "evento" in t -> AnnouncementCategory.EVENTS
        else -> AnnouncementCategory.GENERAL
    }
}

internal fun String.normalizar(): String =
    Normalizer.normalize(trim().lowercase(), Normalizer.Form.NFD).replace(Regex("\\p{Mn}+"), "")