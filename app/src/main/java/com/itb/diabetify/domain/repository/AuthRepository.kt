package com.itb.diabetify.domain.repository

import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.data.remote.auth.request.LoginRequest
import com.itb.diabetify.data.remote.auth.request.SendVerificationRequest
import com.itb.diabetify.data.remote.auth.request.VerifyOtpRequest
import com.itb.diabetify.util.Resource

interface AuthRepository {
    suspend fun createAccount(createAccountRequest: CreateAccountRequest): Resource<Unit>
    suspend fun sendVerification(sendVerificationRequest: SendVerificationRequest): Resource<Unit>
    suspend fun verifyOtp(verifyOtpRequest: VerifyOtpRequest): Resource<Unit>
    suspend fun login(loginRequest: LoginRequest): Resource<Unit>
}