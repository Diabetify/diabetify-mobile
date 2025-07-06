package com.itb.diabetify.data.remote.profile.request

import com.google.gson.annotations.SerializedName

data class AddProfileRequest (
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("hypertension")
    val hypertension: Boolean,
    @SerializedName("macrosomic_baby")
    val macrosomicBaby: Int,
    @SerializedName("smoking")
    val smoking: Int,
    @SerializedName("age_of_smoking")
    val ageOfSmoking: Int,
    @SerializedName("age_of_stop_smoking")
    val ageOfStopSmoking: Int,
    @SerializedName("cholesterol")
    val cholesterol: Boolean,
    @SerializedName("bloodline")
    val bloodline: Boolean,
    @SerializedName("physical_activity_frequency")
    val physicalActivityFrequency: Int,
    @SerializedName("smoke_count")
    val smokeCount: Int
)