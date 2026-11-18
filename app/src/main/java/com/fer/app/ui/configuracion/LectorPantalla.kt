package com.fer.app.ui.configuracion

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberLectorPantallaActivo(): Boolean {
    val context = LocalContext.current
    val manager = remember {
        context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    }
    var activo by remember { mutableStateOf(manager.isTouchExplorationEnabled) }

    DisposableEffect(manager) {
        val listener = AccessibilityManager.TouchExplorationStateChangeListener { activo = it }
        manager.addTouchExplorationStateChangeListener(listener)

        // Por si cambió entre la primera lectura y el registro del listener.
        activo = manager.isTouchExplorationEnabled
        onDispose { manager.removeTouchExplorationStateChangeListener(listener) }
    }
    return activo
}