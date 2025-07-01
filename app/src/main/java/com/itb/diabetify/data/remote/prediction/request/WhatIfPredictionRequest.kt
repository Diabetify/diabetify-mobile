package com.itb.diabetify.data.remote.prediction.request

import com.google.gson.annotations.SerializedName

data class WhatIfPredictionRequest (
    @SerializedName("smoking_status")
    val smokingStatus: Int,
    @SerializedName("avg_smoke_count")
    val avgSmokeCount: Int,
    @SerializedName("weight")
    val weight: Int,
    @SerializedName("is_hypertension")
    val isHypertension: Boolean,
    @SerializedName("physical_activity_frequency")
    val physicalActivityFrequency: Int,
    @SerializedName("is_cholesterol")
    val isCholesterol: Boolean,
)