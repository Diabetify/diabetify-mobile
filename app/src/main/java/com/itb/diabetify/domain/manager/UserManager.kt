package com.itb.diabetify.domain.manager

import com.itb.diabetify.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserManager {
    suspend fun saveUser(user: User)
    fun getUser(): Flow<User?>
    suspend fun clearUser()
}