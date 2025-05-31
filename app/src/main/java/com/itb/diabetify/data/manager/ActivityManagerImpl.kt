package com.itb.diabetify.data.manager

import com.itb.diabetify.domain.manager.ActivityManager
import com.itb.diabetify.domain.model.Activity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityManagerImpl @Inject constructor() : ActivityManager {
    private val _activityData = MutableStateFlow<Activity?>(null)

    override suspend fun saveActivity(activity: Activity) {
        _activityData.value = activity
    }

    override fun getActivityToday(): Flow<Activity?> {
        return _activityData.asStateFlow()
    }

    override suspend fun clearActivity() {
        _activityData.value = null
    }
}