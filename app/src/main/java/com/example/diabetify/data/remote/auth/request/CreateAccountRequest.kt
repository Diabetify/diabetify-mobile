package com.example.diabetify.data.remote.auth.request

import com.google.gson.annotations.SerializedName

data class CreateAccountRequest (
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("age")
    val age: Int,
    @SerializedName("hipertension")
    val hipertension: Boolean,
    @SerializedName("cholesterol")
    val cholesterol: Boolean,
    @SerializedName("disturbedVision")
    val disturbedVision: Boolean,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("verified")
    val verified: Boolean
)