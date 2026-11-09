package com.fer.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomDestination(val label: String, val icon: ImageVector) {
    HOME("Inicio", Icons.Outlined.Home),
    SAVED("Guardados", Icons.Outlined.StarBorder),
    PREFERENCES("Preferencias", Icons.Outlined.Tune),
    SETTINGS("Ajustes", Icons.Outlined.Settings),
}