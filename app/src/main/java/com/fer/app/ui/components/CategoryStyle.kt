package com.fer.app.ui.components

import androidx.compose.ui.graphics.Color
import com.fer.app.model.AnnouncementCategory

fun AnnouncementCategory.placeholderColor(): Color = when (this) {
    AnnouncementCategory.ANNIVERSARY -> Color(0xFF2F5597)
    AnnouncementCategory.CALL -> Color(0xFF2F6B6E)
    AnnouncementCategory.STORIES -> Color(0xFF8A6A2A)
    AnnouncementCategory.ALLIANCES -> Color(0xFF5A4A86)
    AnnouncementCategory.EVENTS -> Color(0xFF7A3E3E)
    AnnouncementCategory.GENERAL -> Color(0xFF4A5370)
}