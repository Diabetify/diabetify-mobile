package com.itb.diabetify.domain.usecases.profile

import com.itb.diabetify.data.remote.profile.request.UpdateProfileRequest
import com.itb.diabetify.domain.model.profile.UpdateProfileResult
import com.itb.diabetify.domain.repository.ProfileRepository

class UpdateProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        weight: String,
        height: String,
        hypertension: Boolean,
        macrosomicBaby: Int,
        bloodline: Boolean,
        cholesterol: Boolean,
    ): UpdateProfileResult {
        val weightError = if (weight.isBlank()) "Weight cannot be empty" else null
        val heightError = if (height.isBlank()) "Height cannot be empty" else null

        if (weightError != null) {
            return UpdateProfileResult(
                weightError = weightError
            )
        }

        if (heightError != null) {
            return UpdateProfileResult(
                heightError = heightError
            )
        }

        val updateProfileResult = UpdateProfileRequest(
            weight = weight.toIntOrNull(),
            height = height.toIntOrNull(),
            hypertension = hypertension,
            macrosomicBaby = macrosomicBaby,
            bloodline = bloodline,
            cholesterol = cholesterol
        )

        return UpdateProfileResult(
            result = repository.updateProfile(updateProfileResult)
        )
    }
}