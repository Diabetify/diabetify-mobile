package com.itb.diabetify.domain.repository

import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.util.Resource

interface AuthRepository {
    suspend fun createAccount(createAccountRequest: CreateAccountRequest): Resource<Unit>
}