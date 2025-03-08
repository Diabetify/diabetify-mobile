package com.itb.diabetify.domain.usecases.auth;

import android.util.Patterns
import com.itb.diabetify.data.remote.auth.request.SendVerificationRequest
import com.itb.diabetify.domain.model.SendVerificationResult;
import com.itb.diabetify.domain.repository.AuthRepository;

class SendVerificationUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String
    ): SendVerificationResult {
        val emailError: String? = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Email tidak valid" else null

        if (emailError != null) {
            return SendVerificationResult(
                emailError = emailError
            )
        }

        val sendVerificationRequest = SendVerificationRequest(
            email = email.trim()
        )

        return SendVerificationResult(
            result = repository.sendVerification(sendVerificationRequest)
        )
    }
}
