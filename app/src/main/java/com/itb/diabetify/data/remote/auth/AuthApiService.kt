package com.itb.diabetify.data.remote.auth

import com.itb.diabetify.data.remote.auth.request.ChangePasswordRequest
import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.data.remote.auth.request.GoogleLoginRequest
import com.itb.diabetify.data.remote.auth.request.LoginRequest
import com.itb.diabetify.data.remote.auth.request.SendVerificationRequest
import com.itb.diabetify.data.remote.auth.request.VerifyOtpRequest
import com.itb.diabetify.data.remote.auth.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("users")
    suspend fun createAccount(
        @Body createAccountRequest: CreateAccountRequest
    ) : AuthResponse

    @POST("verification/get-code")
    suspend fun sendVerification(
        @Body sendVerificationRequest: SendVerificationRequest
    ) : AuthResponse

    @POST("verification/verify")
    suspend fun verifyOtp(
        @Body verifyOtpRequest: VerifyOtpRequest
    ) : AuthResponse

    @POST("users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ) : AuthResponse

    @POST("oauth/auth/google")
    suspend fun googleLogin(
        @Body loginRequest: GoogleLoginRequest
    ) : AuthResponse

    @POST("users/reset-password")
    suspend fun sendResetPasswordVerification(
        @Body sendVerificationRequest: SendVerificationRequest
    ) : AuthResponse

    @POST("users/change-password")
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ) : AuthResponse
}