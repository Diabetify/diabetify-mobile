package com.itb.diabetify.domain.usecases.profile

import com.itb.diabetify.domain.model.Profile
import com.itb.diabetify.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetProfileRepositoryUseCase(
    private val repository: ProfileRepository
) {
    operator fun invoke(): Flow<Profile?> {
        return repository.getProfile()
    }
}