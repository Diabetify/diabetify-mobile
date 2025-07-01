package com.itb.diabetify.data.remote.prediction.response

import com.google.gson.annotations.SerializedName

data class WhatIfPredictionResponse (
    @SerializedName("data")
    val data: WhatIfData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class WhatIfData(
    @SerializedName("feature_explanations")
    val featureExplanations: Map<String, FeatureExplanation>,
    @SerializedName("risk_percentage")
    val riskPercentage: Double,
)

data class FeatureExplanation(
    @SerializedName("contribution")
    val contribution: Double,
    @SerializedName("impact")
    val impact: Int
)