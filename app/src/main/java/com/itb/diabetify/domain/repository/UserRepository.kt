package com.itb.diabetify.domain.repository

import com.itb.diabetify.data.remote.user.request.EditUserRequest
import com.itb.diabetify.domain.model.User
import com.itb.diabetify.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getToken(): String?
    suspend fun editUser(editUserRequest: EditUserRequest): Resource<Unit>
    suspend fun fetchUser(): Resource<Unit>
    fun getUser(): Flow<User?>
}