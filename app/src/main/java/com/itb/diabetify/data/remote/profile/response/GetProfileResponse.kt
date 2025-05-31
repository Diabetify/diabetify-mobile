package com.itb.diabetify.data.remote.profile.response

import com.google.gson.annotations.SerializedName

data class GetProfileResponse (
    @SerializedName("data")
    val data: ProfileData?,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)

data class ProfileData(
    @SerializedName("hypertension")
    val hypertension: Boolean,
    @SerializedName("weight")
    val weight: Double,
    @SerializedName("height")
    val height: Double,
    @SerializedName("bmi")
    val bmi: Double,
    @SerializedName("smoking")
    val smoking: Boolean,
    @SerializedName("year_of_smoking")
    val yearOfSmoking: String,
    @SerializedName("macrosomic_baby")
    val macrosomicBaby: Boolean
)