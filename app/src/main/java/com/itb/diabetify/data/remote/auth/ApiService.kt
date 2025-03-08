package com.itb.diabetify.data.remote.auth

import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.data.remote.auth.response.CreateAccountResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users")
    suspend fun createAccount(
        @Body createAccountRequest: CreateAccountRequest
    ) : CreateAccountResponse
}