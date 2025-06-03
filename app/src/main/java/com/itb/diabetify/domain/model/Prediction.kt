package com.itb.diabetify.domain.model

data class Prediction (
    val riskScore: String? = null,
    val age: String? = null,
    val ageContribution: String? = null,
    val bmi: String? = null,
    val bmiContribution: String? = null,
    val brinkmanScore: String? = null,
    val brinkmanScoreContribution: String? = null,
    val isHypertension: String? = null,
    val isHypertensionContribution: String? = null,
    val isMacrosomicBaby: String? = null,
    val isMacrosomicBabyContribution: String? = null,
    val smokingStatus: String? = null,
    val smokingStatusContribution: String? = null,
    val physicalActivityMinutes: String? = null,
    val physicalActivityMinutesContribution: String? = null,
)