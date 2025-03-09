package com.itb.diabetify.data.remote.auth.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("new_password")
    val newPassword: String,
    @SerializedName("code")
    val code: String
)