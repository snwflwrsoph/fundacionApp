package com.fer.app

import com.fer.app.data.firebase.categoriaDesdeTag
import com.fer.app.data.firebase.normalizar
import com.fer.app.model.AnnouncementCategory
import org.junit.Assert.assertEquals
import org.junit.Test

class AnnouncementUnitTest {

    @Test
    fun testCategoryMapping() {
        assertEquals(AnnouncementCategory.ANNIVERSARY, categoriaDesdeTag("80 Aniversario"))
        assertEquals(AnnouncementCategory.CALL, categoriaDesdeTag("Convocatoria"))
        assertEquals(AnnouncementCategory.STORIES, categoriaDesdeTag("Historias"))
        assertEquals(AnnouncementCategory.ALLIANCES, categoriaDesdeTag("Alianzas"))
        assertEquals(AnnouncementCategory.EVENTS, categoriaDesdeTag("Eventos"))
        assertEquals(AnnouncementCategory.GENERAL, categoriaDesdeTag("Desconocido"))
    }

    @Test
    fun testStringNormalizer() {
        assertEquals("aniversario", "Aniversário".normalizar())
        assertEquals("convocatoria", "  CONVOCATORIA  ".normalizar())
    }

    @Test
    fun testBasicMath() {
        assertEquals(2, 1 + 1)
    }
}
