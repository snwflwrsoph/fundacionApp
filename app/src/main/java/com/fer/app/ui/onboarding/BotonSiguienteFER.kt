package com.fer.app.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.fer.app.ui.theme.FerNavy

@Composable
fun BotonSiguienteFER(
    habilitado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    esUltimoPaso: Boolean = false,
) {
    IconButton(
        onClick = onClick,
        enabled = habilitado,
        modifier = modifier
            .size(56.dp)
            .background(
                color = if (habilitado) {
                    FerNavy
                } else {
                    Color(0xFFD0D5E0)
                },
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = if (esUltimoPaso) PalomitaIcon else FlechaDerechaIcon,
            contentDescription = if (esUltimoPaso) "Confirmar" else "Siguiente",
            tint = Color.White
        )
    }
}

val FlechaIzquierdaIcon: ImageVector
    get() = ImageVector.Builder(
        name = "FlechaIzquierda",
        defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).path(fill = SolidColor(Color.White), pathFillType = PathFillType.NonZero) {
        moveTo(20f, 11f); horizontalLineTo(7.83f); lineTo(13.42f, 5.41f); lineTo(12f, 4f)
        lineTo(4f, 12f); lineTo(12f, 20f); lineTo(13.41f, 18.59f); lineTo(7.83f, 13f)
        horizontalLineTo(20f); verticalLineTo(11f); close()
    }.build()

val FlechaDerechaIcon: ImageVector
    get() = ImageVector.Builder(
        name = "FlechaDerecha",
        defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).path(fill = SolidColor(Color.White), pathFillType = PathFillType.NonZero) {
        moveTo(12f, 4f); lineTo(10.59f, 5.41f); lineTo(16.17f, 11f); horizontalLineTo(4f)
        verticalLineTo(13f); horizontalLineTo(16.17f); lineTo(10.59f, 18.59f); lineTo(12f, 20f)
        lineTo(20f, 12f); lineTo(12f, 4f); close()
    }.build()

val PalomitaIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Palomita",
        defaultWidth = 24.dp, defaultHeight = 24.dp,
        viewportWidth = 24f, viewportHeight = 24f
    ).path(fill = SolidColor(Color.White), pathFillType = PathFillType.NonZero) {
        moveTo(9f, 16.17f); lineTo(4.83f, 12f); lineTo(3.41f, 13.41f); lineTo(9f, 19f)
        lineTo(21f, 7f); lineTo(19.59f, 5.59f); close()
    }.build()
