package com.itb.diabetify.data.remote.auth.response

import com.google.gson.annotations.SerializedName

data class AuthResponse (
    @SerializedName("data")
    val data: Any?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)