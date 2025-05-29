package com.itb.diabetify.data.repository

import com.itb.diabetify.data.remote.user.UserApiService
import com.itb.diabetify.data.remote.user.request.EditUserRequest
import com.itb.diabetify.domain.manager.TokenManager
import com.itb.diabetify.domain.repository.UserRepository
import com.itb.diabetify.util.Resource
import retrofit2.HttpException
import okio.IOException

class UserRepositoryImpl(
    private val userApiService: UserApiService,
    private val tokenManager: TokenManager
): UserRepository {
    override suspend fun editUser(
        editUserRequest: EditUserRequest
    ): Resource<Unit> {
        return try {
            val response = userApiService.editUser(editUserRequest)
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    override suspend fun getToken(): String? {
        return tokenManager.getToken()
    }
}