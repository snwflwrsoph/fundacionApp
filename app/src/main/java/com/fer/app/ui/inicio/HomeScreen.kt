package com.fer.app.ui.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fer.app.model.Announcement
import com.fer.app.data.fake.FakeAnnouncements
import com.fer.app.ui.components.AnnouncementCard
import com.fer.app.ui.components.FeaturedCard
import com.fer.app.ui.theme.FerTheme

@Composable
fun HomeScreen(
    announcements: List<Announcement>,
    onAnnouncementClick: (Announcement) -> Unit,
    onSaveClick: (Announcement) -> Unit,
    modifier: Modifier = Modifier,
    onSeeAllClick: () -> Unit = {},
) {
    val featured = remember(announcements) {
        announcements.firstOrNull { it.isFeatured }
    }
    val others = remember(announcements, featured) {
        announcements.filter { it.id != featured?.id }
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
                text = "Para ti",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }

        if (featured != null) {
            item(
                key = "featured-${featured.id}",
                contentType = "featured",
                span = StaggeredGridItemSpan.FullLine,
            ) {
                FeaturedCard(
                    announcement = featured,
                    onClick = { onAnnouncementClick(featured) },
                    onSaveClick = { onSaveClick(featured) },
                )
            }
        }

        item(
            key = "more-header",
            contentType = "header",
            span = StaggeredGridItemSpan.FullLine,
        ) {
            SectionHeader(
                title = "Descubre más",
                actionLabel = "Ver todo",
                onActionClick = onSeeAllClick,
            )
        }

        itemsIndexed(
            items = others,
            key = { _, announcement -> announcement.id },
            contentType = { _, _ -> "card" },
        ) { index, announcement ->
            AnnouncementCard(
                announcement = announcement,
                onClick = { onAnnouncementClick(announcement) },
                onSaveClick = { onSaveClick(announcement) },
                imageHeight = if (index % 2 == 0) 150.dp else 110.dp,
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        TextButton(onClick = onActionClick) {
            Text(
                text = actionLabel,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF4F2FA, heightDp = 800)
@Composable
private fun HomeScreenPreview() {
    FerTheme {
        HomeScreen(
            announcements = FakeAnnouncements.all,
            onAnnouncementClick = {},
            onSaveClick = {},
        )
    }
}
