package com.itb.diabetify.presentation.recommendation

import com.itb.diabetify.R

data class TipsCardData(
    val image: Int,
    val title: String
)

val tipsCard = listOf(
    TipsCardData(
        image = R.drawable.food,
        title = "Nutrisi Sehat"
    ),
    TipsCardData(
        image = R.drawable.food,
        title = "Aktivitas dan Olahraga"
    ),
    TipsCardData(
        image = R.drawable.food,
        title = "Aktivitas dan Olahraga"
    ),
    TipsCardData(
        image = R.drawable.food,
        title = "Aktivitas dan Olahraga"
    )
)