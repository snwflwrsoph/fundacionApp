package com.fer.app.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fer.app.ui.theme.FerNavy

@Composable
fun BotonSiguienteFER(
    habilitado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
            imageVector = FlechaDerechaIcon,
            contentDescription = "Siguiente",
            tint = Color.White
        )
    }
}