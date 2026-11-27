package com.fer.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fer.app.data.fake.FakeAnnouncements
import com.fer.app.model.Announcement
import com.fer.app.ui.theme.FerBotones
import com.fer.app.ui.theme.FerCeleste
import com.fer.app.ui.theme.FerNavy
import com.fer.app.ui.theme.FerTheme

@Composable
fun NotificationCard(
    announcement: Announcement,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasOpened: Boolean = false,
){
    val shape = RoundedCornerShape(16.dp)
    val texta = if (hasOpened) 0.7f else 1f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .semantics { if (!hasOpened) stateDescription = "Sin leer" }
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = if (hasOpened)
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
            else
                MaterialTheme.colorScheme.onSurface
        ),
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn (min = 64.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = announcement.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (hasOpened) FontWeight.Normal else FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = texta),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (announcement.description.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = announcement.description,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = texta),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
            Spacer(Modifier.width(8.dp))

            if(!hasOpened){
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(color = FerCeleste, shape = CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = FerCeleste,
                    modifier = Modifier.size(16.dp)
                )
            }
        }


    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF4F2FA)
@Composable
private fun NotificationCardPreview() {
    FerTheme {
        NotificationCard(
            announcement = FakeAnnouncements.all[1],
            onClick = {},
            modifier = Modifier.padding(16.dp),
            hasOpened = false,
        )
    }
}