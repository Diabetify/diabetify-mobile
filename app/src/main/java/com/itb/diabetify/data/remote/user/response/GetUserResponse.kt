package com.itb.diabetify.data.remote.user.response

import com.google.gson.annotations.SerializedName

data class GetUserResponse (
    @SerializedName("data")
    val data: UserData?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class UserData(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String
)