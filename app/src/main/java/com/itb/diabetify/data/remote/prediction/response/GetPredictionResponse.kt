package com.itb.diabetify.data.remote.prediction.response

import com.google.gson.annotations.SerializedName

data class GetPredictionResponse (
    @SerializedName("data")
    val data: List<PredictionData?>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class PredictionData(
    @SerializedName("risk_score")
    val riskScore: Double,
    @SerializedName("age")
    val age: Int,
    @SerializedName("age_contribution")
    val ageContribution: Double,
    @SerializedName("age_impact")
    val ageImpact: Int,
    @SerializedName("age_explanation")
    val ageExplanation: String,
    @SerializedName("bmi")
    val bmi: Double,
    @SerializedName("bmi_contribution")
    val bmiContribution: Double,
    @SerializedName("bmi_impact")
    val bmiImpact: Int,
    @SerializedName("bmi_explanation")
    val bmiExplanation: String,
    @SerializedName("brinkman_score")
    val brinkmanScore: Double,
    @SerializedName("brinkman_score_contribution")
    val brinkmanScoreContribution: Double,
    @SerializedName("brinkman_score_impact")
    val brinkmanScoreImpact: Int,
    @SerializedName("brinkman_score_explanation")
    val brinkmanScoreExplanation: String,
    @SerializedName("is_hypertension")
    val isHypertension: Boolean,
    @SerializedName("is_hypertension_contribution")
    val isHypertensionContribution: Double,
    @SerializedName("is_hypertension_impact")
    val isHypertensionImpact: Int,
    @SerializedName("is_hypertension_explanation")
    val isHypertensionExplanation: String,
    @SerializedName("is_macrosomic_baby")
    val isMacrosomicBaby: Boolean,
    @SerializedName("is_macrosomic_baby_contribution")
    val isMacrosomicBabyContribution: Double,
    @SerializedName("is_macrosomic_baby_impact")
    val isMacrosomicBabyImpact: Int,
    @SerializedName("is_macrosomic_baby_explanation")
    val isMacrosomicBabyExplanation: String,
    @SerializedName("smoking_status")
    val smokingStatus: String,
    @SerializedName("smoking_status_contribution")
    val smokingStatusContribution: Double,
    @SerializedName("smoking_status_impact")
    val smokingStatusImpact: Int,
    @SerializedName("smoking_status_explanation")
    val smokingStatusExplanation: String,
    @SerializedName("physical_activity_minutes")
    val physicalActivityMinutes: Int,
    @SerializedName("physical_activity_minutes_contribution")
    val physicalActivityMinutesContribution: Double,
    @SerializedName("physical_activity_minutes_impact")
    val physicalActivityMinutesImpact: Int,
    @SerializedName("physical_activity_minutes_explanation")
    val physicalActivityMinutesExplanation: String,
)