package com.itb.diabetify.data.remote.prediction.response

import com.google.gson.annotations.SerializedName

data class GetPredictionScoreResponse (
    @SerializedName("data")
    val data: List<PredictionScoreData?>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class PredictionScoreData(
    @SerializedName("risk_score")
    val riskScore: Double,
    @SerializedName("created_at")
    val createdAt: String
)