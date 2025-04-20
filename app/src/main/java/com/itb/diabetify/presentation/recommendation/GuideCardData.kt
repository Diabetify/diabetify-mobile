package com.itb.diabetify.presentation.recommendation

import androidx.compose.ui.graphics.Color
import com.itb.diabetify.R

data class GuideCardData(
    val icon: Int,
    val title: String,
    val backgroundColor: Color,
    val iconColor: Color
)

val guideCards = listOf(
    GuideCardData(
        icon = R.drawable.ic_heart,
        title = "Tentang Diabetes",
        backgroundColor = Color(0xFFFFF1F1),
        iconColor = Color(0xFFE57373)
    ),
    GuideCardData(
        icon = R.drawable.ic_danger,
        title = "Faktor Risiko",
        backgroundColor = Color(0xFFFFFBE6),
        iconColor = Color(0xFFFFB74D)
    ),
    GuideCardData(
        icon = R.drawable.ic_code,
        title = "Tentang XAI",
        backgroundColor = Color(0xFFF1F8FF),
        iconColor = Color(0xFF64B5F6)
    ),
    GuideCardData(
        icon = R.drawable.ic_brain,
        title = "Prediksi AI",
        backgroundColor = Color(0xFFF8F0FF),
        iconColor = Color(0xFFAB68FF)
    ),
)