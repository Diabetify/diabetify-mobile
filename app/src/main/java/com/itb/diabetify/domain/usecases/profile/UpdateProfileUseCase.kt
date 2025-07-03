package com.itb.diabetify.domain.usecases.profile

import com.itb.diabetify.data.remote.profile.request.UpdateProfileRequest
import com.itb.diabetify.domain.model.profile.UpdateProfileResult
import com.itb.diabetify.domain.repository.ProfileRepository

class UpdateProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        weight: Int,
        height: Int,
        hypertension: Boolean,
        macrosomicBaby: Int,
        bloodline: Boolean,
        cholesterol: Boolean,
    ): UpdateProfileResult {
        val weightError: String? = if (weight < 30 || weight > 300) "Berat badan tidak valid" else null
        val heightError: String? = if (height < 100 || height > 250) "Tinggi badan tidak valid" else null
        val macrosomicBabyError: String? = if (macrosomicBaby < 0 || macrosomicBaby > 2) "Status bayi makrosomia tidak valid" else null

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

        if (macrosomicBabyError != null) {
            return UpdateProfileResult(
                macrosomicBabyError = macrosomicBabyError
            )
        }

        val updateProfileResult = UpdateProfileRequest(
            weight = weight,
            height = height,
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