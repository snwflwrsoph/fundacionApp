package com.fer.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fer.app.R

val FerFontFamily: FontFamily = FontFamily.Default

/** Fuente de la pantalla de noticia. */
val GoogleSansFlex = FontFamily(Font(R.font.google_sans_flex))

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FerFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
