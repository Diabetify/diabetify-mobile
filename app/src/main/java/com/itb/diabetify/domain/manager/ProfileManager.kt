package com.itb.diabetify.domain.manager

import com.itb.diabetify.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileManager {
    suspend fun saveProfile(profile: Profile)
    fun getProfile(): Flow<Profile?>
    suspend fun clearProfile()
}