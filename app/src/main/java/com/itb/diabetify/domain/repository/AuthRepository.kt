package com.itb.diabetify.domain.repository

import com.itb.diabetify.data.remote.auth.request.ChangePasswordRequest
import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.data.remote.auth.request.GoogleLoginRequest
import com.itb.diabetify.data.remote.auth.request.LoginRequest
import com.itb.diabetify.data.remote.auth.request.SendVerificationRequest
import com.itb.diabetify.data.remote.auth.request.VerifyOtpRequest
import com.itb.diabetify.util.Resource

interface AuthRepository {
    suspend fun createAccount(createAccountRequest: CreateAccountRequest): Resource<Unit>
    suspend fun sendVerification(sendVerificationRequest: SendVerificationRequest, type: String): Resource<Unit>
    suspend fun verifyOtp(verifyOtpRequest: VerifyOtpRequest): Resource<Unit>
    suspend fun login(loginRequest: LoginRequest): Resource<Unit>
    suspend fun googleLogin(googleLoginRequest: GoogleLoginRequest): Resource<Unit>
    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Resource<Unit>
    suspend fun logout(): Resource<Unit>
}