package com.fer.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick)
            .height(48.dp),
        shape = shape,
        colors = CardDefaults.cardColors(FerNavy,),
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = announcement.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if(!hasOpened){
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(color = FerCeleste, shape = CircleShape)
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