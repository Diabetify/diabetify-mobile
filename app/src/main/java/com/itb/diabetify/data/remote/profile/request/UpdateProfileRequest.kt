package com.itb.diabetify.data.remote.profile.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest (
    @SerializedName("weight")
    val weight: Int? = null,
    @SerializedName("height")
    val height: Int? = null,
    @SerializedName("hypertension")
    val hypertension: Boolean? = null,
    @SerializedName("macrosomic_baby")
    val macrosomicBaby: Int? = null,
    @SerializedName("cholesterol")
    val cholesterol: Boolean? = null,
    @SerializedName("bloodline")
    val bloodline: Boolean? = null
)