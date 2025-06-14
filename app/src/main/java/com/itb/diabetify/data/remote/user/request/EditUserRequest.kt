package com.itb.diabetify.data.remote.user.request

import com.google.gson.annotations.SerializedName

data class EditUserRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("dob")
    val dob: String
)