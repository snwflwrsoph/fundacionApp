package com.fer.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SaveButton(
    isSaved: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.background(Color(0x80081028), CircleShape),
    ) {
        Icon(
            imageVector = if (isSaved) Icons.Filled.Star else Icons.Outlined.StarBorder,
            contentDescription = if (isSaved) "Quitar de guardados" else "Guardar anuncio",
            tint = if (isSaved) MaterialTheme.colorScheme.secondary else Color.White,
        )
    }
}