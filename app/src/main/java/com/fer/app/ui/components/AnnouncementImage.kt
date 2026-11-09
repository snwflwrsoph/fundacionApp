package com.fer.app.ui.components

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp

@Composable
fun AnnouncementImage(
    modifier: Modifier = Modifier,
    @DrawableRes imageRes: Int? = null,
    imageUrl: String? = null,
    placeholderColor: Color = MaterialTheme.colorScheme.primary,
) {
    if (imageRes != null) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )
    } else {
        Box(
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