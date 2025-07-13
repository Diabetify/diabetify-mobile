package com.itb.diabetify.data.remote.prediction.response

import com.google.gson.annotations.SerializedName

data class WhatIfJobResponse(
    @SerializedName("data")
    val data: WhatIfJobData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class WhatIfJobData(
    @SerializedName("input_used")
    val inputUsed: WhatIfInputUsed,
    @SerializedName("job_id")
    val jobId: String,
    @SerializedName("poll_url")
    val pollUrl: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("submit_time")
    val submitTime: String
)

data class WhatIfInputUsed(
    @SerializedName("smoking_status")
    val smokingStatus: Int,
    @SerializedName("years_of_smoking")
    val yearsOfSmoking: Int,
    @SerializedName("avg_smoke_count")
    val avgSmokeCount: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("is_hypertension")
    val isHypertension: Boolean,
    @SerializedName("physical_activity_frequency")
    val physicalActivityFrequency: Int,
    @SerializedName("is_cholesterol")
    val isCholesterol: Boolean
)
