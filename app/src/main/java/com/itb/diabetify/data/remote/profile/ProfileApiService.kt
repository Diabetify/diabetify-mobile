package com.itb.diabetify.data.remote.profile

import com.itb.diabetify.data.remote.profile.request.AddProfileRequest
import com.itb.diabetify.data.remote.profile.request.UpdateProfileRequest
import com.itb.diabetify.data.remote.profile.response.GetProfileResponse
import com.itb.diabetify.data.remote.profile.response.ProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ProfileApiService {
    @POST("profile")
    suspend fun addProfile(
        @Body addProfileRequest: AddProfileRequest
    ): ProfileResponse

    @PATCH("profile")
    suspend fun updateProfile(
        @Body updateProfileRequest: UpdateProfileRequest
    ): ProfileResponse

    @GET("profile")
    suspend fun getProfile() : GetProfileResponse
}