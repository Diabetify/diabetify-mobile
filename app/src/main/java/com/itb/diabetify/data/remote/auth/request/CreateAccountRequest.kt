package com.itb.diabetify.data.remote.auth.request

import com.google.gson.annotations.SerializedName

data class CreateAccountRequest (
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("gender")
    val gender: String,
)