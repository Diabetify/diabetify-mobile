package com.itb.diabetify.domain.usecases.user

import com.itb.diabetify.domain.model.user.GetUserResult
import com.itb.diabetify.domain.repository.UserRepository

class GetUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): GetUserResult {
        return GetUserResult(
            result = repository.fetchUser()
        )
    }
}