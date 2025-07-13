package com.itb.diabetify.data.remote.prediction.response

import com.google.gson.annotations.SerializedName

data class PredictionJobResponse (
    @SerializedName("data")
    val data: PredictionJobData,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class PredictionJobData(
    @SerializedName("job_id")
    val jobId: String,
    @SerializedName("poll_url")
    val pollUrl: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("submit_time")
    val submitTime: String
)
