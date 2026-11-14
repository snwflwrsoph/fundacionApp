package com.fer.app.ui.noticia

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fer.app.data.fake.FakeAnnouncements
import com.fer.app.data.fake.FakeNoticias
import com.fer.app.model.Announcement
import com.fer.app.model.Noticia
import com.fer.app.ui.theme.FerGrisAzulado
import com.fer.app.ui.theme.FerNavy
import com.fer.app.ui.theme.FerBotones
import com.fer.app.ui.theme.FerTheme
import com.fer.app.ui.theme.GoogleSansFlex

@Composable
fun NewsDetailScreen(
    announcement: Announcement,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        // Botones de navegación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalIconButton(
                onClick = onBack,
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
        }

        // Imagen principal
        Image(
            painter = painterResource(announcement.mainImage),
            contentDescription = "Imagen de ${announcement.title}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(24.dp))
        )

        // Título
        Text(
            text = announcement.title,
            style = MaterialTheme.typography.headlineSmall,
            color = FerNavy,
            fontFamily = GoogleSansFlex,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(24.dp)
        )

        // Autor y fecha de publicación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(announcement.profile),
                    contentDescription = "Imagen de perfil de ${announcement.author}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(50))
                )
                Text(
                    text = announcement.author,
                    style = MaterialTheme.typography.labelSmall,
                    color = FerGrisAzulado,
                    fontFamily = GoogleSansFlex,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text(
                text = announcement.date,
                style = MaterialTheme.typography.labelSmall,
                color = FerGrisAzulado,
                fontFamily = GoogleSansFlex,
                fontWeight = FontWeight.Normal
            )
        }

        // Texto
        Text(
            text = announcement.description,
            style = MaterialTheme.typography.bodyMedium,
            color = FerNavy,
            fontFamily = GoogleSansFlex,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(24.dp)
        )

        // Fotos secundarias
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(announcement.secondaryImage),
                contentDescription = "Imagen de ${announcement.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(24.dp))
            )
            Image(
                painter = painterResource(announcement.tertiaryImage),
                contentDescription = "Imagen de ${announcement.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(24.dp))
            )
        }

        // Botones de interacción
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconToggleButton(
                checked = announcement.isLikedByUser,
                onCheckedChange = { onLikeClick() }
            ) {
                Icon(
                    imageVector = if (announcement.isLikedByUser) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Me gusta",
                    tint = FerNavy,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = announcement.likes.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = FerNavy
            )

            IconToggleButton(
                checked = announcement.isSavedOnCalendar,
                onCheckedChange = { onSaveClick() }
            ) {
                Icon(
                    imageVector = if (announcement.isSavedOnCalendar) Icons.Filled.CalendarToday else Icons.Outlined.CalendarToday,
                    contentDescription = "Agregar a calendario",
                    tint = FerNavy,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = "Agregar a calendario",
                style = MaterialTheme.typography.labelLarge,
                color = FerNavy
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 1200)
@Composable
private fun NewScreenPreview() {
    FerTheme {
        NewsDetailScreen(announcement = FakeAnnouncements.all[0])
    }
}
