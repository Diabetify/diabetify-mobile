package com.itb.diabetify.data.remote.auth

import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.data.remote.auth.request.SendVerificationRequest
import com.itb.diabetify.data.remote.auth.request.VerifyOtpRequest
import com.itb.diabetify.data.remote.auth.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
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
}