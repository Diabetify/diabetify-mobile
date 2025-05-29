package com.itb.diabetify.data.remote.activity.response

import com.google.gson.annotations.SerializedName

data class ActivityResponse (
    @SerializedName("data")
    val data: Any?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)