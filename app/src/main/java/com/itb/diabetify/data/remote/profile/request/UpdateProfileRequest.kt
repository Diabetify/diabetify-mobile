package com.itb.diabetify.data.remote.profile.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest (
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("hypertension")
    val hypertension: Boolean,
    @SerializedName("macrosomic_baby")
    val macrosomicBaby: Int,
    @SerializedName("cholesterol")
    val cholesterol: Boolean,
    @SerializedName("bloodline")
    val bloodline: Boolean
)