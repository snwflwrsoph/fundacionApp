package com.fer.app.ui.buscar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fer.app.data.fake.FakeAnnouncements
import com.fer.app.model.Announcement
import com.fer.app.ui.components.AnnouncementCard
import com.fer.app.ui.theme.FerBotones
import com.fer.app.ui.theme.FerNavy
import com.fer.app.ui.theme.FerTheme

@Composable
fun BuscarScreen(
    anuncios: List<Announcement>,
    onAnnouncementClick: (Announcement) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: (Announcement) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }

    val anunciosFiltrados = remember(query, anuncios) {
        if (query.isBlank()) {
            anuncios
        } else {
            val q = query.trim().lowercase()
            anuncios.filter { anuncio ->
                anuncio.title.lowercase().contains(q) ||
                        anuncio.summary.lowercase().contains(q) ||
                        anuncio.category.label.lowercase().contains(q)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Bar superior de búsqueda
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

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Buscar publicaciones...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = FerNavy
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar búsqueda",
                                tint = FerNavy
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FerNavy,
                    unfocusedBorderColor = Color(0xFFD0D5E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
                modifier = Modifier.weight(1f)
            )
        }

        if (anunciosFiltrados.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se encontraron resultados para \"$query\".",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalItemSpacing = 12.dp
            ) {
                if (query.isNotBlank()) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Text(
                            text = "Resultados (${anunciosFiltrados.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = FerNavy,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
                items(items = anunciosFiltrados, key = { it.id }) { anuncio ->
                    AnnouncementCard(
                        announcement = anuncio,
                        onClick = { onAnnouncementClick(anuncio) },
                        onSaveClick = { onSaveClick(anuncio) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BuscarScreenPreview() {
    FerTheme {
        BuscarScreen(
            anuncios = FakeAnnouncements.all,
            onAnnouncementClick = {},
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
