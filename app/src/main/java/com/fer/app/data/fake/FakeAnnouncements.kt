package com.fer.app.data.fake

import com.fer.app.R
import com.fer.app.model.Announcement
import com.fer.app.model.AnnouncementCategory

object FakeAnnouncements {
    private val drawables = listOf(
        R.drawable.espososrodriguezdiscurso,
        R.drawable.edificioespososrodriguez,
        R.drawable.presentacionespososrodriguez,
        R.drawable.cenafer,
        R.drawable.sociasespososrodriguez,
        R.drawable.becasespososrodriguez,
    )

    val all = listOf(
        Announcement(
            id = "1",
            title = "Desayuno 80 aniversario de Fundación Esposos Rodríguez",
            summary = "En Fundación Esposos Rodríguez iniciamos las actividades conmemorativas por nuestro 80 aniversario…",
            category = AnnouncementCategory.ANNIVERSARY,
            imageRes = R.drawable.espososrodriguezdiscurso,
            isFeatured = true,
        ),
        Announcement(
            id = "2",
            title = "Beca de Excelencia 2026 para jóvenes sonorenses",
            summary = "Conoce los requisitos y fechas de la convocatoria.",
            category = AnnouncementCategory.CALL,
            imageRes = R.drawable.education,
        ),
        Announcement(
            id = "3",
            title = "Me voy a la maestría con la Beca de Excelencia",
            summary = "La historia de uno de nuestros becarios.",
            category = AnnouncementCategory.STORIES,
            imageRes = R.drawable.graduation,
        ),
        Announcement(
            id = "4",
            title = "[Título del anuncio]",
            summary = "[Descripción del anuncio]",
            category = AnnouncementCategory.ALLIANCES,
            imageRes = R.drawable.career_fair,
        ),
    )
    val many: List<Announcement> = List(30) { i ->
        val item = all[i % all.size]
        item.copy(
            id = "demo-$i",
            imageRes = drawables[i % drawables.size],
            isFeatured = i == 0,
        )
    }
}