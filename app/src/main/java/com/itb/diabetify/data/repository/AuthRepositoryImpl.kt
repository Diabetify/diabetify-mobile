package com.itb.diabetify.data.repository

import com.itb.diabetify.data.remote.auth.ApiService
import com.itb.diabetify.data.remote.auth.request.ChangePasswordRequest
import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.data.remote.auth.request.GoogleLoginRequest
import com.itb.diabetify.data.remote.auth.request.LoginRequest
import com.itb.diabetify.data.remote.auth.request.SendVerificationRequest
import com.itb.diabetify.data.remote.auth.request.VerifyOtpRequest
import com.itb.diabetify.domain.manager.TokenManager
import com.itb.diabetify.domain.repository.AuthRepository
import com.itb.diabetify.util.Resource
import okio.IOException
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
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
        sendVerificationRequest: SendVerificationRequest,
        type: String
    ): Resource<Unit> {
        return try {
            when (type) {
                "register" -> {
                    val response = apiService.sendVerification(sendVerificationRequest)
                }
                "reset-password" -> {
                    val response = apiService.sendResetPasswordVerification(sendVerificationRequest)
                }
                else -> {
                    return Resource.Error("Invalid type")
                }
            }
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    override suspend fun verifyOtp(
        verifyOtpRequest: VerifyOtpRequest
    ): Resource<Unit> {
        return try {
            val response = apiService.verifyOtp(verifyOtpRequest)
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    override suspend fun login(
        loginRequest: LoginRequest
    ): Resource<Unit> {
        return try {
            val response = apiService.login(loginRequest)
            tokenManager.saveToken(response.data.toString())
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    override suspend fun googleLogin(
        googleLoginRequest: GoogleLoginRequest
    ): Resource<Unit> {
        return try {
            val response = apiService.googleLogin(googleLoginRequest)
            tokenManager.saveToken(response.data.toString())
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    override suspend fun changePassword(
        changePasswordRequest: ChangePasswordRequest
    ): Resource<Unit> {
        return try {
            val response = apiService.changePassword(changePasswordRequest)
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    override suspend fun logout(): Resource<Unit> {
        return try {
            tokenManager.clearToken()
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }
}