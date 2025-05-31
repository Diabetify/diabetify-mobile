package com.itb.diabetify.data.remote.prediction.response

data class GetPredictionResponse (
    val data: List<PredictionData>,
    val message: String,
    val status: String
)

data class PredictionData(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val userId: Int,
    val riskScore: Double,
    val age: Int,
    val ageContribution: Double,
    val ageImpact: Int,
    val bmi: Double,
    val bmiContribution: Double,
    val bmiImpact: Int,
    val brinkmanScore: Double,
    val brinkmanScoreContribution: Double,
    val brinkmanScoreImpact: Int,
    val isHypertension: Boolean,
    val isHypertensionContribution: Double,
    val isHypertensionImpact: Int,
    val isMacrosomicBaby: Boolean,
    val isMacrosomicBabyContribution: Double,
    val isMacrosomicBabyImpact: Int,
    val smokingStatus: String,
    val smokingStatusContribution: Double,
    val smokingStatusImpact: Int,
    val physicalActivityMinutes: Int,
    val physicalActivityMinutesContribution: Double,
    val physicalActivityMinutesImpact: Int
)