package com.fer.app.ui.components

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun AnnouncementImage(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int? = null,
    imageUrl: String? = null,
    placeholderColor: Color = MaterialTheme.colorScheme.primary,
) {
    when {
        imageRes != null -> Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )

        !imageUrl.isNullOrBlank() -> AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.background(placeholderColor),
            placeholder = ColorPainter(placeholderColor.copy(alpha = 0.5f)),
            error = ColorPainter(Color(0xFF8A8FA3)),   // gris = la URL existe pero falló (ver Logcat "FER")
            onError = { Log.e("FER", "Imagen falló: $imageUrl -> ${it.result.throwable}") },
        )

        else -> Box(   // "FOTO" = el campo viene vacío desde Firestore
            modifier = modifier.background(placeholderColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "FOTO",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp,
                letterSpacing = 1.5.sp,
            )
        }
    }
}