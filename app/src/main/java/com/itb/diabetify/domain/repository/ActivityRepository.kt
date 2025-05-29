package com.itb.diabetify.domain.repository

import com.itb.diabetify.data.remote.activity.request.AddActivityRequest
import com.itb.diabetify.util.Resource

interface ActivityRepository {
    suspend fun getToken(): String?
    suspend fun addActivity(addActivityRequest: AddActivityRequest): Resource<Unit>
}