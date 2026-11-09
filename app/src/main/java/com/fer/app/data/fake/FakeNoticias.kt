package com.fer.app.data.fake

import com.fer.app.R
import com.fer.app.model.Noticia

object FakeNoticias {
    /** Única noticia de ejemplo que se tiene diseñada; se reutiliza para cualquier anuncio del feed. */
    val ejemplo = Noticia(
        id = "1",
        titulo = "Encuentro Comunidad FER",
        descripcion = "Los becarios de Fundación Esposos Rodríguez se reúnen en un encuentro lleno de aprendizaje y participación.\n" +
                "\n" +
                "Fundación Esposos Rodríguez llevó a cabo un encuentro en el que estudiantes de preparatoria y universidad tuvieron la oportunidad de compartir ideas, conocimientos y experiencias.\n" +
                "Durante la jornada se realizaron diversas ponencias sobre temas de interés para la comunidad, además de actividades dinámicas que fomentaron la participación y la convivencia entre los asistentes.\n" +
                "\n" +
                "El encuentro se convirtió en un espacio para aprender, dialogar y fortalecer los vínculos entre los integrantes de la comunidad universitaria, reafirmando la importancia de generar espacios de intercambio y colaboración.",
        fecha = "16 de octubre del 2026",
        autor = "Comunicación FER",
        perfil = R.drawable.fer_logo,
        imagenPrincipal = R.drawable.espososrodriguezdiscurso,
        imagenSecundaria = R.drawable.career_fair,
        imagenTerciaria = R.drawable.activity_fair,
        likes = 42,
        isLikedByUser = false,
        isSavedOnCalendar = false
    )
}
