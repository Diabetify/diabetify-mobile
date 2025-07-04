package com.itb.diabetify.domain.model

data class Profile(
    val hypertension: Boolean,
    val weight: Int,
    val height: Int,
    val bmi: Double,
    val smoking: Int,
    val yearOfSmoking: Int,
    val macrosomicBaby: Int,
    val cholesterol: Boolean,
    val bloodline: Boolean,
)