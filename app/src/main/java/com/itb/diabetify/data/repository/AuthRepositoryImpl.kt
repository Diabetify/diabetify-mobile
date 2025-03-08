package com.itb.diabetify.data.repository

import com.itb.diabetify.data.remote.auth.ApiService
import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.data.remote.auth.request.SendVerificationRequest
import com.itb.diabetify.domain.repository.AuthRepository
import com.itb.diabetify.util.Resource
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

    override suspend fun sendVerification(
        sendVerificationRequest: SendVerificationRequest
    ): Resource<Unit> {
        return try {
            val response = apiService.sendVerification(sendVerificationRequest)
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }
}