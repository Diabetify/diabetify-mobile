package com.itb.diabetify.domain.usecases.profile

import com.itb.diabetify.domain.model.profile.GetProfileResult
import com.itb.diabetify.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): GetProfileResult {
        return GetProfileResult(
            result = repository.fetchProfile()
        )
    }
}