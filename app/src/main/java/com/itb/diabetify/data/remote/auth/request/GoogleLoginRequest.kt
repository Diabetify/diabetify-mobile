package com.itb.diabetify.data.remote.auth.request

import com.google.gson.annotations.SerializedName

data class GoogleLoginRequest(
    @SerializedName("token")
    val token: String
)