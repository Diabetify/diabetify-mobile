package com.itb.diabetify.data.remote.profile

import com.itb.diabetify.data.remote.profile.response.GetProfileResponse
import retrofit2.http.GET

interface ProfileApiService {
    @GET("profile")
    suspend fun getProfile() : GetProfileResponse
}