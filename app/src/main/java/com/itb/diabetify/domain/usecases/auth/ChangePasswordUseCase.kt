package com.itb.diabetify.domain.usecases.auth

import android.util.Patterns
import com.itb.diabetify.data.remote.auth.request.ChangePasswordRequest
import com.itb.diabetify.domain.model.auth.ChangePasswordResult
import com.itb.diabetify.domain.repository.AuthRepository

class ChangePasswordUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        newPassword: String,
        code: String
    ): ChangePasswordResult {
        val emailError: String? = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Email tidak valid" else null
        val newPasswordError: String? = if (newPassword.length < 8) "Password harus lebih dari 8 karakter" else null
        val codeError: String? = if (code.isEmpty()) "Kode tidak boleh kosong" else null

        if (emailError != null) {
            return ChangePasswordResult(
                emailError = emailError
            )
        }

        if (newPasswordError != null) {
            return ChangePasswordResult(
                newPasswordError = newPasswordError
            )
        }

        if (codeError != null) {
            return ChangePasswordResult(
                codeError = codeError
            )
        }

        val changePasswordRequest = ChangePasswordRequest(
            email = email.trim(),
            newPassword = newPassword.trim(),
            code = code.trim()
        )

        return ChangePasswordResult(
            result = repository.changePassword(changePasswordRequest)
        )
    }
}