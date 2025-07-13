package com.itb.diabetify.data.remote.prediction.response

import com.google.gson.annotations.SerializedName

data class WhatIfJobStatusResponse(
    @SerializedName("data")
    val data: WhatIfJobStatusData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class WhatIfJobStatusData(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("job_id")
    val jobId: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: WhatIfJobResult?,
    @SerializedName("status")
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class WhatIfJobResult(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("prediction_id")
    val predictionId: Int,
    @SerializedName("risk_percentage")
    val riskPercentage: Double,
    @SerializedName("risk_score")
    val riskScore: Double
)
