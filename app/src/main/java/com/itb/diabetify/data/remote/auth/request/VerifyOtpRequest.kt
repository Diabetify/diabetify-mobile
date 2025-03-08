package com.itb.diabetify.data.remote.auth.request

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest (
    @SerializedName("email")
    val email: String,
    @SerializedName("code")
    val code: String
)