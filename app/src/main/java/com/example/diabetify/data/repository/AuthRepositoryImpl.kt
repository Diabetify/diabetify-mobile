package com.example.diabetify.data.repository

import com.example.diabetify.data.remote.auth.ApiService
import com.example.diabetify.data.remote.auth.request.CreateAccountRequest
import com.example.diabetify.domain.repository.AuthRepository
import com.example.diabetify.util.Resource
import okio.IOException
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val apiService: ApiService
): AuthRepository {
    override suspend fun createAccount(
        createAccountRequest: CreateAccountRequest
    ): Resource<Unit> {
        return try {
            val response = apiService.createAccount(createAccountRequest)
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }
}