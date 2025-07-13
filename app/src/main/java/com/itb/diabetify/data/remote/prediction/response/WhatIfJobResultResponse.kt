package com.itb.diabetify.data.remote.prediction.response

import com.google.gson.annotations.SerializedName

data class WhatIfJobResultResponse(
    @SerializedName("data")
    val data: WhatIfJobResultData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class WhatIfJobResultData(
    @SerializedName("feature_explanations")
    val featureExplanations: Map<String, WhatIfFeatureExplanation>,
    @SerializedName("job_id")
    val jobId: String,
    @SerializedName("job_info")
    val jobInfo: WhatIfJobInfo,
    @SerializedName("prediction_id")
    val predictionId: Int,
    @SerializedName("risk_percentage")
    val riskPercentage: Double,
    @SerializedName("risk_score")
    val riskScore: Double,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("user_data_used")
    val userDataUsed: WhatIfUserDataUsed
)

data class WhatIfFeatureExplanation(
    @SerializedName("contribution")
    val contribution: Double,
    @SerializedName("impact")
    val impact: Int,
    @SerializedName("shap")
    val shap: Double
)

data class WhatIfJobInfo(
    @SerializedName("completed_at")
    val completedAt: String,
    @SerializedName("processing_time")
    val processingTime: String
)

data class WhatIfUserDataUsed(
    @SerializedName("age")
    val age: Int,
    @SerializedName("bmi")
    val bmi: Int,
    @SerializedName("brinkman_score")
    val brinkmanScore: Int,
    @SerializedName("is_bloodline")
    val isBloodline: Boolean,
    @SerializedName("is_cholesterol")
    val isCholesterol: Boolean,
    @SerializedName("is_hypertension")
    val isHypertension: Boolean,
    @SerializedName("is_macrosomic_baby")
    val isMacrosomicBaby: Int,
    @SerializedName("physical_activity_frequency")
    val physicalActivityFrequency: Int,
    @SerializedName("smoking_status")
    val smokingStatus: Int
)
