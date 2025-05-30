package com.itb.diabetify.domain.manager

import com.itb.diabetify.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityManager {
    suspend fun saveActivity(activity: Activity)
    fun getActivityToday(): Flow<Activity?>
    suspend fun clearActivity()
}