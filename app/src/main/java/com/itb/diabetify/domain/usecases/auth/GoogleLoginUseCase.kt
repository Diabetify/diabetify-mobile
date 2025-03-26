package com.itb.diabetify.domain.usecases.auth

import com.itb.diabetify.data.remote.auth.request.GoogleLoginRequest
import com.itb.diabetify.domain.model.GoogleLoginResult
import com.itb.diabetify.domain.repository.AuthRepository

class GoogleLoginUseCase (
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        token: String
    ): GoogleLoginResult {
        val tokenError: String? = if (token.isEmpty()) "Token tidak boleh kosong" else null

        if (tokenError != null) {
            return GoogleLoginResult(
                tokenError = tokenError
            )
        }

        val loginRequest = GoogleLoginRequest(
            token = token.trim()
        )

        return GoogleLoginResult(
            result = repository.googleLogin(loginRequest)
        )
    }
}