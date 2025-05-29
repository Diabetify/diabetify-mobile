package com.itb.diabetify.data.repository

import com.itb.diabetify.data.remote.activity.ActivityApiService
import com.itb.diabetify.data.remote.activity.request.AddActivityRequest
import com.itb.diabetify.domain.manager.TokenManager
import com.itb.diabetify.domain.repository.ActivityRepository
import com.itb.diabetify.util.Resource
import okio.IOException
import retrofit2.HttpException

class ActivityRepositoryImpl(
    private val activityApiService: ActivityApiService,
    private val tokenManager: TokenManager,
): ActivityRepository {

    override suspend fun getToken(): String? {
        return tokenManager.getToken()
    }

    override suspend fun addActivity(
        addActivityRequest: AddActivityRequest
    ): Resource<Unit> {
        return try {
            val response = activityApiService.addActivity(addActivityRequest)
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }
}