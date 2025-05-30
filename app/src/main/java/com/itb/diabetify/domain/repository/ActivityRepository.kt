package com.itb.diabetify.domain.repository

import com.itb.diabetify.data.remote.activity.request.AddActivityRequest
import com.itb.diabetify.domain.model.Activity
import com.itb.diabetify.util.Resource
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    suspend fun getToken(): String?
    suspend fun addActivity(addActivityRequest: AddActivityRequest): Resource<Unit>
    suspend fun fetchActivityToday(): Resource<Unit>
    fun getActivityToday(): Flow<Activity?>
}