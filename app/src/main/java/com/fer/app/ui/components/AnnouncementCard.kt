package com.fer.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fer.app.model.Announcement
import com.fer.app.data.fake.FakeAnnouncements
import com.fer.app.ui.theme.FerTheme

@Composable
fun AnnouncementCard(
    announcement: Announcement,
    onClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageHeight: Dp = 150.dp,
) {
    val shape = RoundedCornerShape(16.dp)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Box {
            AnnouncementImage(
                imageRes = announcement.imageRes,
                imageUrl = announcement.imageUrl,
                placeholderColor = announcement.category.placeholderColor(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight),
            )
            SaveButton(
                isSaved = announcement.isSaved,
                onClick = onSaveClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
            )
        }
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = announcement.category.label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = announcement.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF4F2FA)
@Composable
private fun AnnouncementCardPreview() {
    FerTheme {
        AnnouncementCard(
            announcement = FakeAnnouncements.all[1],
            onClick = {},
            onSaveClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}