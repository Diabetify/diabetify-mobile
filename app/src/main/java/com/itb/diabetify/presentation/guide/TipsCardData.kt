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
        title = "Rekomendasi Nutrisi Sehat",
        tipsId = "healthy_nutrition"
    ),
    TipsCardData(
        image = R.drawable.activity,
        title = "Rekomendasi Olahraga",
        tipsId = "physical_activity"
    ),
    TipsCardData(
        image = R.drawable.smoking,
        title = "Tips Berhenti Merokok",
        tipsId = "smoking"
    ),
    TipsCardData(
        image = R.drawable.hypertension,
        title = "Mengelola Hipertensi",
        tipsId = "hypertension"
    ),
    TipsCardData(
        image = R.drawable.cholesterol,
        title = "Mengelola Kolesterol",
        tipsId = "cholesterol"
    ),
)