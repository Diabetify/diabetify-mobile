package com.itb.diabetify.data.manager

import com.itb.diabetify.domain.manager.ActivityManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.itb.diabetify.domain.model.Activity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.activityDataStore: DataStore<Preferences> by preferencesDataStore(name = "activity_data")

@Singleton
class ActivityManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : ActivityManager {

    private object PreferencesKeys {
        val ACTIVITY_DATA = stringPreferencesKey("activity_data")
    }

    override suspend fun saveActivity(activity: Activity) {
        context.activityDataStore.edit { preferences ->
            preferences[PreferencesKeys.ACTIVITY_DATA] = gson.toJson(activity)
        }
    }

    override fun getActivityToday(): Flow<Activity?> {
        return context.activityDataStore.data.map { preferences ->
            preferences[PreferencesKeys.ACTIVITY_DATA]?.let {
                gson.fromJson(it, Activity::class.java)
            }
        }
    }

    override suspend fun clearActivity() {
        context.activityDataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.ACTIVITY_DATA)
        }
    }
}