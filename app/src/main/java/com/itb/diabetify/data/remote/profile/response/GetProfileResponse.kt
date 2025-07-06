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
    val weight: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("bmi")
    val bmi: Double,
    @SerializedName("smoking")
    val smoking: Int,
    @SerializedName("age_of_smoking")
    val ageOfSmoking: Int,
    @SerializedName("age_of_stop_smoking")
    val ageOfStopSmoking: Int,
    @SerializedName("macrosomic_baby")
    val macrosomicBaby: Int,
    @SerializedName("cholesterol")
    val cholesterol: Boolean,
    @SerializedName("bloodline")
    val bloodline: Boolean
)