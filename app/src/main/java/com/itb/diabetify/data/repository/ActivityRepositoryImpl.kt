package com.itb.diabetify.data.repository

import android.annotation.SuppressLint
import com.itb.diabetify.data.remote.activity.ActivityApiService
import com.itb.diabetify.data.remote.activity.request.AddActivityRequest
import com.itb.diabetify.data.remote.activity.request.UpdateActivityRequest
import com.itb.diabetify.domain.manager.ActivityManager
import com.itb.diabetify.domain.manager.TokenManager
import com.itb.diabetify.domain.model.Activity
import com.itb.diabetify.domain.repository.ActivityRepository
import com.itb.diabetify.util.Resource
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException

class ActivityRepositoryImpl(
    private val activityApiService: ActivityApiService,
    private val tokenManager: TokenManager,
    private val activityManager: ActivityManager
): ActivityRepository {
    override suspend fun getToken(): String? {
        return tokenManager.getToken()
    }

    override suspend fun addActivity(
        addActivityRequest: AddActivityRequest
    ): Resource<Unit> {
        return try {
            val response = activityApiService.addActivity(addActivityRequest)
            fetchActivityToday()
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    override suspend fun updateActivity(
        activityId: String,
        updateActivityRequest: UpdateActivityRequest
    ): Resource<Unit> {
        return try {
            val response = activityApiService.updateActivity(activityId, updateActivityRequest)
            fetchActivityToday()
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    @SuppressLint("NewApi")
    override suspend fun fetchActivityToday(): Resource<Unit> {
        return try {
            val currentDate = java.time.LocalDate.now()
            val startDate = currentDate.toString()
            val response = activityApiService.getActivityByDate(startDate, startDate)
            response.data?.let { activities ->
                val smokingValue = if (activities.smoke.isEmpty()) {
                    "0"
                } else {
                    activities.smoke[0].value.toString()
                }

                val workoutValue = if (activities.workout.isEmpty()) {
                    "0"
                } else {
                    activities.workout[0].value.toString()
                }

                val smokingId = if (activities.smoke.isEmpty()) {
                    null
                } else {
                    activities.smoke[0].id.toString()
                }

                val workoutId = if (activities.workout.isEmpty()) {
                    null
                } else {
                    activities.workout[0].id.toString()
                }

                activityManager.saveActivity(
                    Activity(
                        smokingId = smokingId,
                        workoutId = workoutId,
                        smokingValue = smokingValue,
                        workoutValue = workoutValue,
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

    override fun getActivityToday(): Flow<Activity?> {
        return activityManager.getActivityToday()
    }
}