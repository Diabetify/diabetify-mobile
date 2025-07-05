package com.itb.diabetify.domain.model

data class Activity(
    val smokingId: Int? = null,
    val workoutId: Int? = null,
    val smokingValue: Int,
    val workoutValue: Int
)