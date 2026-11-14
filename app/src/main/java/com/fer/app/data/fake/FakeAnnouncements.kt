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
            description = "En un ambiente de convivencia, gratitud y compromiso social, la Fundación celebró un desayuno conmemorativo con motivo de su aniversario, reuniendo a colaboradores, benefactores, voluntarios e invitados especiales que han sido parte fundamental de su trayectoria.\n" +
                    "\n" +
                    "Durante el evento, se destacaron los logros alcanzados a lo largo de los años, así como el impacto positivo que la organización ha generado en la comunidad a través de sus diversos programas y proyectos. Los asistentes tuvieron la oportunidad de compartir experiencias, fortalecer vínculos y reafirmar su compromiso con la misión de la fundación.\n" +
                    "\n" +
                    "Representantes de la institución agradecieron el apoyo y la confianza de quienes han contribuido al crecimiento de la organización, resaltando que cada acción solidaria ha permitido transformar vidas y construir un futuro con mayores oportunidades para quienes más lo necesitan.\n" +
                    "\n" +
                    "El desayuno concluyó con un mensaje de esperanza y renovación, invitando a continuar trabajando en conjunto para seguir impulsando iniciativas que promuevan el bienestar y el desarrollo de la comunidad.",
            author = "Comunicación FER",
            date = "16 de octubre del 2026",
            category = AnnouncementCategory.ANNIVERSARY,
            profile = R.drawable.fer_logo,
            mainImage = R.drawable.espososrodriguezdiscurso,
            imageRes = R.drawable.espososrodriguezdiscurso,
            secondaryImage = R.drawable.career_fair,
            tertiaryImage = R.drawable.activity_fair,
            imageUrl = null,
            isSaved = false,
            isFeatured = true,
            likes = 42,
            isLikedByUser = false,
            isSavedOnCalendar = false,
        ),
        Announcement(
            id = "2",
            title = "Beca de Excelencia 2026 para jóvenes sonorenses",
            summary = "Conoce los requisitos y fechas de la convocatoria.",
            description = "Con el objetivo de impulsar la formación académica y el desarrollo profesional de jóvenes talentos, se anunció la apertura de una nueva convocatoria de becas para realizar estudios de maestría en prestigiosas universidades del extranjero.\n" +
                    "\n" +
                    "El programa brindará apoyo económico a estudiantes y profesionistas destacados que deseen continuar su preparación en áreas estratégicas como ciencia, tecnología, ingeniería, salud, educación, negocios y desarrollo sostenible. Las becas podrán cubrir gastos relacionados con matrícula, manutención, transporte y seguro médico, dependiendo de las condiciones establecidas en la convocatoria.\n" +
                    "\n" +
                    "Las personas interesadas deberán cumplir con requisitos académicos, demostrar excelencia en su trayectoria educativa y acreditar el dominio del idioma requerido por la institución de destino. El proceso de selección considerará el mérito académico, el potencial de liderazgo y el impacto que los estudios tendrán en el desarrollo profesional de los candidatos.\n" +
                    "\n" +
                    "Las solicitudes podrán presentarse a través de la plataforma oficial del programa dentro de las fechas establecidas. Las autoridades responsables destacaron que esta iniciativa busca fortalecer la internacionalización del talento y generar oportunidades para que más estudiantes accedan a una educación de excelencia en el extranjero.\n" +
                    "\n" +
                    "Se invita a los aspirantes a consultar las bases completas de la convocatoria y preparar con anticipación la documentación necesaria para participar en el proceso de selección.",
            author = "Becas FER",
            date = "12 de noviembre del 2026",
            category = AnnouncementCategory.CALL,
            profile = R.drawable.fer_logo,
            mainImage = R.drawable.edificioespososrodriguez,
            imageRes = R.drawable.edificioespososrodriguez,
            secondaryImage = R.drawable.career_fair,
            tertiaryImage = R.drawable.activity_fair,
            imageUrl = null,
            isSaved = false,
            isFeatured = true,
            likes = 35,
            isLikedByUser = false,
            isSavedOnCalendar = false,
        ),

        Announcement(
            id = "3",
            title = "Me voy a la maestría con la Beca de Excelencia",
            summary = "La historia de uno de nuestros becarios.",
            description = "Cuando Ana Martínez terminó su licenciatura en Ingeniería Ambiental, sabía que quería contribuir a resolver los desafíos relacionados con el uso sostenible del agua en su comunidad. Sin embargo, los costos de estudiar una maestría en el extranjero parecían un obstáculo difícil de superar.\n" +
                    "\n" +
                    "Tras meses de preparación, presentó su solicitud a un programa de becas internacionales y fue seleccionada entre cientos de aspirantes. Gracias al apoyo recibido, pudo cursar una maestría en Gestión de Recursos Hídricos en una reconocida universidad de Europa.\n" +
                    "\n" +
                    "Durante sus estudios, Ana participó en proyectos de investigación sobre tecnologías de ahorro de agua y colaboró con especialistas de diferentes países. Hoy, de regreso en México, trabaja impulsando iniciativas que buscan mejorar el acceso y la conservación del agua en comunidades vulnerables.\n" +
                    "\n" +
                    "“Esta beca cambió mi vida. No solo me dio acceso a una educación de calidad, sino que me permitió regresar con conocimientos y experiencias que ahora puedo aplicar para generar un impacto positivo”, señala la joven profesionista.\n" +
                    "\n" +
                    "Su historia es un ejemplo de cómo las oportunidades educativas pueden transformar vidas y contribuir al desarrollo de las comunidades.",
            author = "Comunicación FER",
            date = "26 de noviembre del 2026",
            category = AnnouncementCategory.STORIES,
            profile = R.drawable.fer_logo,
            mainImage = R.drawable.maestria,
            imageRes = R.drawable.espososrodriguezdiscurso,
            secondaryImage = R.drawable.career_fair,
            tertiaryImage = R.drawable.activity_fair,
            imageUrl = null,
            isSaved = false,
            isFeatured = true,
            likes = 67,
            isLikedByUser = false,
            isSavedOnCalendar = false,
        ),
        Announcement(
            id = "4",
            title = "[Título del anuncio]",
            summary = "[Descripción del anuncio]",
            description = "En un ambiente de convivencia, gratitud y compromiso social, la Fundación celebró un desayuno conmemorativo con motivo de su aniversario, reuniendo a colaboradores, benefactores, voluntarios e invitados especiales que han sido parte fundamental de su trayectoria.\n" +
                    "\n" +
                    "Durante el evento, se destacaron los logros alcanzados a lo largo de los años, así como el impacto positivo que la organización ha generado en la comunidad a través de sus diversos programas y proyectos. Los asistentes tuvieron la oportunidad de compartir experiencias, fortalecer vínculos y reafirmar su compromiso con la misión de la fundación.\n" +
                    "\n" +
                    "Representantes de la institución agradecieron el apoyo y la confianza de quienes han contribuido al crecimiento de la organización, resaltando que cada acción solidaria ha permitido transformar vidas y construir un futuro con mayores oportunidades para quienes más lo necesitan.\n" +
                    "\n" +
                    "El desayuno concluyó con un mensaje de esperanza y renovación, invitando a continuar trabajando en conjunto para seguir impulsando iniciativas que promuevan el bienestar y el desarrollo de la comunidad.",
            author = "Comunicación FER",
            date = "16 de octubre del 2026",
            category = AnnouncementCategory.ANNIVERSARY,
            profile = R.drawable.fer_logo,
            mainImage = R.drawable.espososrodriguezdiscurso,
            imageRes = R.drawable.espososrodriguezdiscurso,
            secondaryImage = R.drawable.career_fair,
            tertiaryImage = R.drawable.activity_fair,
            imageUrl = null,
            isSaved = false,
            isFeatured = true,
            likes = 42,
            isLikedByUser = false,
            isSavedOnCalendar = false,
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