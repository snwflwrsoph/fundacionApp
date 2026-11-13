package com.fer.app.model

import androidx.annotation.DrawableRes

/** Quién es el usuario (pantalla "Bienvenido" y Configuración > Tipo de usuario). */
enum class TipoUsuario { BECARIO, ASPIRANTE }

enum class NivelEducativo { PREPARATORIA, UNIVERSIDAD, MAESTRIA }

enum class TamanoTexto(val label: String, val scale: Float) {
    CHICO("Chico", 0.85f),
    NORMAL("Normal", 1.0f),
    GRANDE("Grande", 1.15f)
}

/** El [id] es el que usa [OpcionSeleccion] en la pantalla de selección. */
enum class TipoUniversidad(val id: Int) {
    PUBLICA(1),
    PRIVADA(2);

    companion object {
        fun deId(id: Int): TipoUniversidad? = entries.firstOrNull { it.id == id }
    }
}

/** Id reservado de la opción "Otra" (abre el campo de texto en SeleccionScreen). */
const val ID_UNIVERSIDAD_OTRA = 99

/** Tarjeta de la pantalla de selección (tipo de universidad / lista de universidades). */
data class OpcionSeleccion(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    @DrawableRes val icono: Int
)

/** Lo que el usuario eligió en el flujo de preferencias. */
data class PerfilUsuario(
    val tipoUsuario: TipoUsuario? = null,
    val nivelEducativo: NivelEducativo? = null,
    val tipoUniversidad: TipoUniversidad? = null,
    val universidadId: Int? = null,
    /** Solo se llena cuando [universidadId] == [ID_UNIVERSIDAD_OTRA]. */
    val universidadOtra: String? = null,
    val onboardingCompleto: Boolean = false
)

/** Interruptores de la pantalla de Configuración. */
data class AjustesApp(
    val notificaciones: Boolean = true,
    val modoOscuro: Boolean = false,
    val lectorPantalla: Boolean = false,
    val compartirDatos: Boolean = true,
    val tamanoTexto: TamanoTexto = TamanoTexto.NORMAL
)

/** Listas que se muestran en las pantallas de selección. */
data class Catalogo(
    val tiposUniversidad: List<OpcionSeleccion> = emptyList(),
    val universidades: Map<TipoUniversidad, List<OpcionSeleccion>> = emptyMap()
)
