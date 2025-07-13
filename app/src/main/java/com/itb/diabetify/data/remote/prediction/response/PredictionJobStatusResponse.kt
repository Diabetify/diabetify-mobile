package com.itb.diabetify.data.remote.prediction.response

import com.google.gson.annotations.SerializedName

data class PredictionJobStatusResponse (
    @SerializedName("data")
    val data: PredictionJobStatusData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class PredictionJobStatusData(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("job_id")
    val jobId: String,
    @SerializedName("progress")
    val progress: Int,
    @SerializedName("result")
    val result: PredictionJobResult?,
    @SerializedName("status")
    val status: String,
    @SerializedName("step")
    val step: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class PredictionJobResult(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("prediction_id")
    val predictionId: Int,
    @SerializedName("risk_percentage")
    val riskPercentage: Double,
    @SerializedName("risk_score")
    val riskScore: Double
)
