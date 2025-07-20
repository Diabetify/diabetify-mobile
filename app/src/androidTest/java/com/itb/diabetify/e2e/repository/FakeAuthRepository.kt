package com.itb.diabetify.e2e.repository

import com.itb.diabetify.data.remote.auth.request.ChangePasswordRequest
import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.data.remote.auth.request.LoginRequest
import com.itb.diabetify.data.remote.auth.request.SendVerificationRequest
import com.itb.diabetify.data.remote.auth.request.VerifyOtpRequest
import com.itb.diabetify.domain.repository.AuthRepository
import com.itb.diabetify.util.Resource
import javax.inject.Inject

class FakeAuthRepository @Inject constructor() : AuthRepository {
    override suspend fun createAccount(
        createAccountRequest: CreateAccountRequest
    ): Resource<Unit> {
        return Resource.Success(Unit)
    }

    override suspend fun sendVerification(
        sendVerificationRequest: SendVerificationRequest,
        type: String
    ): Resource<Unit> {
        return when (type) {
            "register" -> Resource.Success(Unit)
            "reset-password" -> Resource.Success(Unit)
            else -> Resource.Error("Invalid type")
        }
    }

    override suspend fun verifyOtp(
        verifyOtpRequest: VerifyOtpRequest
    ): Resource<Unit> {
        return Resource.Success(Unit)
    }

    override suspend fun login(loginRequest: LoginRequest): Resource<Unit> {
        return Resource.Success(Unit)
    }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Resource<Unit> {
        return Resource.Success(Unit)
    }

    override suspend fun logout(): Resource<Unit> {
        return Resource.Success(Unit)
    }
}