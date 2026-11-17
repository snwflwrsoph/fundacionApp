package com.fer.app.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fer.app.data.fake.FakeAnnouncements
import com.fer.app.model.Announcement
import com.fer.app.ui.components.NotificationCard
import com.fer.app.ui.theme.FerBotones
import com.fer.app.ui.theme.FerNavy
import com.fer.app.ui.theme.FerTheme

private const val MAX_NOTIFICACIONES = 30

@Composable
fun NotificationScreen(
    anuncios: List<Announcement>,
    leidas: Set<String>,
    onAnnouncementClick: (Announcement) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilledTonalIconButton(
                onClick = onBackClick,
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = FerBotones,
                    contentColor = FerNavy
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar"
                )
            }
            Text(
                text = "Notificaciones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        if (anuncios.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No tienes notificaciones por ahora.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(items = anuncios.take(MAX_NOTIFICACIONES), key = { it.id }) { anuncio ->
                    NotificationCard(
                        announcement = anuncio,
                        hasOpened = anuncio.id in leidas,
                        onClick = { onAnnouncementClick(anuncio) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF4F2FA, heightDp = 800)
@Composable
private fun NotificationScreenPreview() {
    FerTheme {
        NotificationScreen(
            anuncios = FakeAnnouncements.all,
            leidas = setOf(FakeAnnouncements.all[0].id),
            onAnnouncementClick = {},
            onBackClick = {},
        )
    }
}