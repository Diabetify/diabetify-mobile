package com.itb.diabetify.domain.model

data class Prediction (
    val riskScore: String? = null,
    val age: String? = null,
    val ageContribution: String? = null,
    val ageImpact: String? = null,
    val bmi: String? = null,
    val bmiContribution: String? = null,
    val bmiImpact: String? = null,
    val brinkmanScore: String? = null,
    val brinkmanScoreContribution: String? = null,
    val brinkmanScoreImpact: String? = null,
    val isHypertension: String? = null,
    val isHypertensionContribution: String? = null,
    val isHypertensionImpact: String? = null,
    val isMacrosomicBaby: String? = null,
    val isMacrosomicBabyContribution: String? = null,
    val isMacrosomicBabyImpact: String? = null,
    val smokingStatus: String? = null,
    val smokingStatusContribution: String? = null,
    val smokingStatusImpact: String? = null,
    val physicalActivityMinutes: String? = null,
    val physicalActivityMinutesContribution: String? = null,
    val physicalActivityMinutesImpact: String? = null
)