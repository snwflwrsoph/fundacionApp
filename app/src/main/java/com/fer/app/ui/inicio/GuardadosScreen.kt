package com.fer.app.ui.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import com.fer.app.ui.components.AnnouncementCard
import com.fer.app.ui.theme.FerTheme

/** Anuncios que el usuario marcó con la estrella. Provisional: el diseño final de Guardados no venía en los zips. */
@Composable
fun GuardadosScreen(
    guardados: List<Announcement>,
    onAnnouncementClick: (Announcement) -> Unit,
    onSaveClick: (Announcement) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (guardados.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Aún no has guardado anuncios.\nToca la estrella de un anuncio para guardarlo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        return
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalItemSpacing = 12.dp,
    ) {
        item(
            key = "title",
            contentType = "title",
            span = StaggeredGridItemSpan.FullLine,
        ) {
            Text(
                text = "Guardados",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
        items(items = guardados, key = { it.id }, contentType = { "card" }) { anuncio ->
            AnnouncementCard(
                announcement = anuncio,
                onClick = { onAnnouncementClick(anuncio) },
                onSaveClick = { onSaveClick(anuncio) },
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF4F2FA, heightDp = 800)
@Composable
private fun GuardadosScreenPreview() {
    FerTheme {
        GuardadosScreen(
            guardados = FakeAnnouncements.all.map { it.copy(isSaved = true) },
            onAnnouncementClick = {},
            onSaveClick = {},
        )
    }
}
