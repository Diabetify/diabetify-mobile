package com.itb.diabetify.data.remote.activity

import com.itb.diabetify.data.remote.activity.request.AddActivityRequest
import com.itb.diabetify.data.remote.activity.response.ActivityResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ActivityApiService {
    @POST("activity")
    suspend fun addActivity(
        @Body addActivityRequest: AddActivityRequest
    ) : ActivityResponse
}