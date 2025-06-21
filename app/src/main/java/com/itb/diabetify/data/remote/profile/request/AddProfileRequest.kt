package com.itb.diabetify.data.remote.profile.request

import com.google.gson.annotations.SerializedName

data class AddProfileRequest (
    @SerializedName("weight")
    val weight: Int? = null,
    @SerializedName("height")
    val height: Int? = null,
    @SerializedName("hypertension")
    val hypertension: Boolean? = null,
    @SerializedName("macrosomic_baby")
    val macrosomicBaby: Boolean? = null,
    @SerializedName("smoking")
    val smoking: Int? = null,
    @SerializedName("year_of_smoking")
    val yearOfSmoking: Int? = null,
    @SerializedName("cholesterol")
    val cholesterol: Boolean? = null,
    @SerializedName("bloodline")
    val bloodline: Boolean? = null,
    @SerializedName("physical_activity_frequency")
    val physicalActivityFrequency: Int? = null,
    @SerializedName("smoke_count")
    val smokeCount: Int? = null
)