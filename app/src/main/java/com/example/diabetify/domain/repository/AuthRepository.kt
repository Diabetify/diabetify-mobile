package com.example.diabetify.domain.repository

import com.example.diabetify.data.remote.auth.request.CreateAccountRequest
import com.example.diabetify.util.Resource

interface AuthRepository {
    suspend fun createAccount(createAccountRequest: CreateAccountRequest): Resource<Unit>
}