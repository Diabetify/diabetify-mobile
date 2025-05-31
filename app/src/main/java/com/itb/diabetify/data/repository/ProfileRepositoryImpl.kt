package com.itb.diabetify.data.repository

import com.itb.diabetify.data.remote.activity.ActivityApiService
import com.itb.diabetify.data.remote.profile.ProfileApiService
import com.itb.diabetify.domain.manager.ProfileManager
import com.itb.diabetify.domain.manager.TokenManager
import com.itb.diabetify.domain.model.Profile
import com.itb.diabetify.domain.repository.ProfileRepository
import com.itb.diabetify.util.Resource
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException

class ProfileRepositoryImpl(
    private val profileApiService: ProfileApiService,
    private val tokenManager: TokenManager,
    private val profileManager: ProfileManager
): ProfileRepository {
    override suspend fun getToken(): String? {
        return tokenManager.getToken()
    }

    override suspend fun fetchProfile(): Resource<Unit> {
        return try {
            val response = profileApiService.getProfile()
            response.data?.let { profile ->
                profileManager.saveProfile(
                    Profile(
                        hypertension = profile.hypertension,
                        weight = profile.weight.toString(),
                        height = profile.height.toString(),
                        bmi = profile.bmi.toString(),
                        smoking = profile.smoking,
                        yearOfSmoking = profile.yearOfSmoking,
                        macrosomicBaby = profile.macrosomicBaby
                    )
                )
            }
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    override fun getProfile(): Flow<Profile?> {
        return profileManager.getProfile()
    }
}