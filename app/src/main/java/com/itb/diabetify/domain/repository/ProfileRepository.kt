package com.itb.diabetify.domain.repository

import com.itb.diabetify.data.remote.profile.request.UpdateProfileRequest
import com.itb.diabetify.domain.model.Profile
import com.itb.diabetify.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getToken(): String?
    suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest): Resource<Unit>
    suspend fun fetchProfile(): Resource<Unit>
    fun getProfile(): Flow<Profile?>
}