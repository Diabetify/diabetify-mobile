package com.itb.diabetify.data.manager

import com.itb.diabetify.domain.manager.ProfileManager
import com.itb.diabetify.domain.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileManagerImpl @Inject constructor() : ProfileManager {
    private val _profileData = MutableStateFlow<Profile?>(null)

    override suspend fun saveProfile(profile: Profile) {
        _profileData.value = profile
    }

    override fun getProfile(): Flow<Profile?> {
        return _profileData.asStateFlow()
    }

    override suspend fun clearProfile() {
        _profileData.value = null
    }
}