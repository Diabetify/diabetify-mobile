package com.itb.diabetify.data.remote.user.response

import com.google.gson.annotations.SerializedName

data class UserResponse (
    @SerializedName("data")
    val data: Any?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)