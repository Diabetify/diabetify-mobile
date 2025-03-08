package com.itb.diabetify.domain.usecases.auth

import com.itb.diabetify.data.remote.auth.request.VerifyOtpRequest
import com.itb.diabetify.domain.model.VerifyOtpResult
import com.itb.diabetify.domain.repository.AuthRepository

class VerifyOtpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        code: String
    ): VerifyOtpResult {
        val emailError: String? = if (email.isEmpty()) "Email tidak boleh kosong" else null
        val codeError: String? = if (code.isEmpty()) "Kode tidak boleh kosong" else null

        if (emailError != null) {
            return VerifyOtpResult(
                emailError = emailError
            )
        }

        if (codeError != null) {
            return VerifyOtpResult(
                codeError = codeError
            )
        }

        val verifyOtpRequest = VerifyOtpRequest(
            email = email.trim(),
            code = code.trim()
        )

        return VerifyOtpResult(
            result = repository.verifyOtp(verifyOtpRequest)
        )
    }
}