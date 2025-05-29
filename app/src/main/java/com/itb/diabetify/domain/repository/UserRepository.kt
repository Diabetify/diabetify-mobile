package com.itb.diabetify.domain.repository

import com.itb.diabetify.data.remote.user.request.EditUserRequest
import com.itb.diabetify.util.Resource

interface UserRepository {
    suspend fun editUser(editUserRequest: EditUserRequest): Resource<Unit>
    suspend fun getToken(): String?
}