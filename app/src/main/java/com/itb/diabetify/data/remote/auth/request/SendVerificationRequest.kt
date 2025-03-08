package com.itb.diabetify.data.remote.auth.request

import com.google.gson.annotations.SerializedName

data class SendVerificationRequest (
    @SerializedName("email")
    val email: String
)