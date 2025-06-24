package com.itb.diabetify.domain.usecases.user

import com.itb.diabetify.domain.model.User
import com.itb.diabetify.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserRepositoryUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<User?> {
        return repository.getUser()
    }
}