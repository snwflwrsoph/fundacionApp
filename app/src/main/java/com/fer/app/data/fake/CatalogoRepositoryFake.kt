package com.fer.app.data.fake

import com.fer.app.R
import com.fer.app.data.CatalogoRepository
import com.fer.app.model.Catalogo
import com.fer.app.model.ID_UNIVERSIDAD_OTRA
import com.fer.app.model.OpcionSeleccion
import com.fer.app.model.TipoUniversidad

/** Catálogo hardcodeado (mismas opciones que en Figma). */
class CatalogoRepositoryFake : CatalogoRepository {

    override suspend fun cargarCatalogo() = Catalogo(
        tiposUniversidad = listOf(
            OpcionSeleccion(TipoUniversidad.PUBLICA.id, "Universidad Pública", DESC_FER, ICONO_GENERICO),
            OpcionSeleccion(TipoUniversidad.PRIVADA.id, "Universidad Privada", DESC_FUTUROS, ICONO_GENERICO)
        ),
        universidades = mapOf(
            TipoUniversidad.PUBLICA to listOf(
                OpcionSeleccion(1, "UNAM", "Universidad Nacional Autónoma de México.", ICONO_GENERICO),
                OpcionSeleccion(2, "IPN", "Instituto Politécnico Nacional.", ICONO_GENERICO),
                OpcionSeleccion(3, "UAM", "Universidad Autónoma Metropolitana.", ICONO_GENERICO),
                OpcionSeleccion(ID_UNIVERSIDAD_OTRA, "Otra", "Escribe el nombre de tu universidad pública.", ICONO_GENERICO)
            ),
            TipoUniversidad.PRIVADA to listOf(
                OpcionSeleccion(1, "UDEM", DESC_FER, R.drawable.logo_udem),
                OpcionSeleccion(2, "CETYS", DESC_FUTUROS, R.drawable.logo_cetys),
                OpcionSeleccion(3, "Tec de Monterrey", DESC_FUTUROS, R.drawable.logo_tec)
            )
        )
    )

    private companion object {
        const val DESC_FER = "Accede a avisos, convocatorias y beneficios exclusivos para becarios FER."
        const val DESC_FUTUROS = "Convocatorias y avisos para futuros becarios."
        val ICONO_GENERICO = R.drawable.ic_universidad
    }
}
