package com.itb.diabetify.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.itb.diabetify.domain.manager.UserManager
import com.itb.diabetify.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

@Singleton
class UserManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : UserManager {

    private object PreferencesKeys {
        val USER_DATA = stringPreferencesKey("user_data")
    }

    override suspend fun saveUser(user: User) {
        context.userDataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_DATA] = gson.toJson(user)
        }
    }

    override fun getUser(): Flow<User?> {
        return context.userDataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_DATA]?.let {
                gson.fromJson(it, User::class.java)
            }
        }
    }

    override suspend fun clearUser() {
        context.userDataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_DATA)
        }
    }
} 