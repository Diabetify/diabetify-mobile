package com.itb.diabetify.domain.usecases.auth

import android.util.Patterns
import com.itb.diabetify.data.remote.auth.request.LoginRequest
import com.itb.diabetify.domain.model.LoginResult
import com.itb.diabetify.domain.repository.AuthRepository

class LoginUseCase (
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): LoginResult {
        val emailError: String? = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Email tidak valid" else null
        val passwordError: String? = if (password.length < 8) "Password harus lebih dari 8 karakter" else null

        if (emailError != null) {
            return LoginResult(
                emailError = emailError
            )
        }

        if (passwordError != null) {
            return LoginResult(
                passwordError = passwordError
            )
        }

        val loginRequest = LoginRequest(
            email = email.trim(),
            password = password.trim()
        )

        return LoginResult(
            result = repository.login(loginRequest)
        )
    }
}