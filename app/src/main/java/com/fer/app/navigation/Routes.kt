package com.fer.app.navigation

import android.net.Uri
import com.fer.app.model.TipoUniversidad

object Routes {
    // Flujo de preferencias (primer arranque y pestaña "Preferencias")
    const val BIENVENIDA = "bienvenida"                 // Becario / Aspirante
    const val NIVEL_EDUCATIVO = "nivel_educativo"       // Preparatoria / Universidad / Maestría
    const val TIPO_UNIVERSIDAD = "tipo_universidad"     // Pública / Privada

    const val ARG_TIPO = "tipo"
    const val UNIVERSIDADES = "universidades/{$ARG_TIPO}"
    fun universidades(tipo: TipoUniversidad) = "universidades/${tipo.name}"

    // App principal (con barra inferior)
    const val INICIO = "inicio"
    const val GUARDADOS = "guardados"
    const val CONFIGURACION = "configuracion"

    // Detalle de un anuncio (pantalla completa, sin barras)
    const val ARG_ID = "id"
    const val DETALLE = "detalle/{$ARG_ID}"
    fun detalle(idAnuncio: String) = "detalle/${Uri.encode(idAnuncio)}"

    // Pantalla de búsqueda y subtramas de configuración
    const val BUSQUEDA = "busqueda"
    const val NOTIFICACIONES = "notificaciones"
    const val TAMANO_TEXTO = "tamano_texto"

    /** Rutas donde se muestra la barra superior (logo, buscar, notificaciones). */
    val CON_BARRA_SUPERIOR = setOf(INICIO, GUARDADOS)

    /** Rutas donde se muestra la barra inferior. */
    val CON_BARRA_INFERIOR = setOf(INICIO, GUARDADOS, CONFIGURACION)
}
