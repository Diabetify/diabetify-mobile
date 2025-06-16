package com.itb.diabetify.data.remote.activity

import com.itb.diabetify.data.remote.activity.request.AddActivityRequest
import com.itb.diabetify.data.remote.activity.request.UpdateActivityRequest
import com.itb.diabetify.data.remote.activity.response.ActivityResponse
import com.itb.diabetify.data.remote.activity.response.GetActivityResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ActivityApiService {
    @POST("activity")
    suspend fun addActivity(
        @Body addActivityRequest: AddActivityRequest
    ): ActivityResponse

    @PUT("activity/{activityId}")
    suspend fun updateActivity(
        @Path("activityId") activityId: String,
        @Body updateActivityRequest: UpdateActivityRequest
    ): ActivityResponse

    @GET("activity/me/date-range")
    suspend fun getActivityByDate(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): GetActivityResponse
}