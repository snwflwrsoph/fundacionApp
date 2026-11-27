package com.fer.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.fer.app.model.TamanoTexto

private val FerColorScheme = lightColorScheme(
    primary = FerAzul,
    onPrimary = FerSurface,
    secondary = FerGold,
    onSecondary = FerOnGold,
    background = FerBackground,
    surface = FerSurface,
    onSurface = FerOnSurface,
    onSurfaceVariant = FerMuted
)

private val DarkFerColorScheme = darkColorScheme(
    primary = Color(0xFF86A2E9),
    onPrimary = Color.White,
    secondary = FerGold,
    onSecondary = FerOnGold,
    background = Color(0xFF0D111A),
    surface = Color(0xFF161E2E),
    onSurface = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF94A3B8)
)

@Composable
fun FerTheme(
    darkTheme: Boolean = false,
    tamanoTexto: TamanoTexto = TamanoTexto.NORMAL,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkFerColorScheme else FerColorScheme
    val currentDensity = LocalDensity.current
    val customDensity = remember(currentDensity, tamanoTexto) {
        Density(density = currentDensity.density, fontScale = currentDensity.fontScale * tamanoTexto.scale)
    }

    CompositionLocalProvider(LocalDensity provides customDensity) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
