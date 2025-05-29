package com.itb.diabetify.data.remote.activity.request

import com.google.gson.annotations.SerializedName

data class AddActivityRequest(
    @SerializedName("activity_date")
    val activityDate: String,
    @SerializedName("activity_type")
    val activityType: String,
    @SerializedName("value")
    val value: Int
)