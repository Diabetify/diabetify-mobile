package com.itb.diabetify.domain.usecases.auth

import android.util.Patterns
import com.itb.diabetify.data.remote.auth.request.VerifyOtpRequest
import com.itb.diabetify.domain.model.auth.VerifyOtpResult
import com.itb.diabetify.domain.repository.AuthRepository

class VerifyOtpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        code: String
    ): VerifyOtpResult {
        val emailError: String? = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Email tidak valid" else null
        val codeError: String? = if (code.length != 6) "Kode OTP harus terdiri dari 6 karakter" else null

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