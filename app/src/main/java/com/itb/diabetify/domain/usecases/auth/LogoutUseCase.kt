package com.itb.diabetify.domain.usecases.auth

import com.itb.diabetify.domain.model.LogoutResult
import com.itb.diabetify.domain.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): LogoutResult {
        return LogoutResult(
            result = repository.logout()
        )
    }
}