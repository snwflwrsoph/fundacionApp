package com.fer.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.fer.app.ui.theme.FerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = { FerLogo() },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Outlined.Search, contentDescription = "Buscar anuncios")
            }
            IconButton(onClick = onNotificationsClick) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Notificaciones")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
}

@Composable
private fun FerLogo() {
    // Texto provisional. Cuando tengas el logo real lo cambiamos por una Image.
    Text(
        text = "FUNDACIÓN\nESPOSOS\nRODRÍGUEZ",
        fontFamily = FontFamily.Serif,
        fontSize = 9.sp,
        lineHeight = 10.sp,
        letterSpacing = 0.6.sp,
    )
}

@Preview(showBackground = true)
@Composable
private fun AppTopBarPreview() {
    FerTheme { AppTopBar() }
}