package com.itb.diabetify.data.remote.prediction.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse (
    @SerializedName("data")
    val data: Any?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)