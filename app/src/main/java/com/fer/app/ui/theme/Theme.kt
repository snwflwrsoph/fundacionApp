package com.fer.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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

@Composable
fun FerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FerColorScheme,
        typography = Typography,
        content = content
    )
}
