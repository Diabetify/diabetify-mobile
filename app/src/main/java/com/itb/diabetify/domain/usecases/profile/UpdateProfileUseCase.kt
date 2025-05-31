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
        macrosomicBaby: Boolean
    ): UpdateProfileResult {
        val weightError = if (weight.isBlank()) "Weight cannot be empty" else null
        val heightError = if (height.isBlank()) "Height cannot be empty" else null
        val hypertensionError = if (weight.isBlank()) "Hypertension cannot be empty" else null
        val macrosomicBabyError = if (weight.isBlank()) "Macrosomic baby cannot be empty" else null

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

        if (hypertensionError != null) {
            return UpdateProfileResult(
                hypertensionError = hypertensionError
            )
        }

        if (macrosomicBabyError != null) {
            return UpdateProfileResult(
                macrosomicBabyError = macrosomicBabyError
            )
        }

        val updateProfileResult = UpdateProfileRequest(
            weight = weight.toIntOrNull(),
            height = height.toIntOrNull(),
            hypertension = hypertension,
            macrosomicBaby = macrosomicBaby
        )

        return UpdateProfileResult(
            result = repository.updateProfile(updateProfileResult)
        )
    }
}