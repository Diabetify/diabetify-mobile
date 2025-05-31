package com.itb.diabetify.data.remote.profile.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse (
    @SerializedName("data")
    val data: Any?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)