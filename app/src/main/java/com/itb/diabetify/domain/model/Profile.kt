package com.itb.diabetify.domain.model

data class Profile(
    val hypertension: Boolean? = null,
    val weight: String? = null,
    val height: String? = null,
    val bmi: String? = null,
    val smoking: Int? = null,
    val yearOfSmoking: String? = null,
    val macrosomicBaby: Boolean? = null,
    val cholesterol: Boolean? = null,
    val bloodline: Boolean? = null,
)