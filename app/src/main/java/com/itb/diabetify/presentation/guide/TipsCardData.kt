package com.itb.diabetify.presentation.guide

import com.itb.diabetify.R

data class TipsCardData(
    val image: Int,
    val title: String,
    val tipsId: String,
)

val tipsCard = listOf(
    TipsCardData(
        image = R.drawable.food,
        title = "Nutrisi Sehat",
        tipsId = "healthy_nutrition"
    ),
    TipsCardData(
        image = R.drawable.food,
        title = "Aktivitas dan Olahraga",
        tipsId = "physical_activity"
    ),
    TipsCardData(
        image = R.drawable.food,
        title = "Aktivitas dan Olahraga",
        tipsId = "physical_activity"
    ),
    TipsCardData(
        image = R.drawable.food,
        title = "Aktivitas dan Olahraga",
        tipsId = "physical_activity"
    )
)