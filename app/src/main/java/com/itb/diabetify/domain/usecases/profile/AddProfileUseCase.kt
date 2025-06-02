package com.itb.diabetify.domain.usecases.profile

import com.itb.diabetify.data.remote.profile.request.AddProfileRequest
import com.itb.diabetify.domain.model.profile.AddProfileResult
import com.itb.diabetify.domain.repository.ProfileRepository

class AddProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        weight: String,
        height: String,
        hypertension: Boolean,
        macrosomicBaby: Boolean,
        smoking: Boolean,
        yearOfSmoking: Int?
    ): AddProfileResult {
        val weightError = if (weight.isBlank()) "Weight cannot be empty" else null
        val heightError = if (height.isBlank()) "Height cannot be empty" else null
        val yearOfSmokingError = if (yearOfSmoking == null) "Year of smoking cannot be empty" else null

        if (weightError != null) {
            return AddProfileResult(
                weightError = weightError
            )
        }

        if (heightError != null) {
            return AddProfileResult(
                heightError = heightError
            )
        }

        if (yearOfSmokingError != null) {
            return AddProfileResult(
                yearOfSmokingError = yearOfSmokingError
            )
        }

        val addProfileResult = AddProfileRequest(
            weight = weight.toIntOrNull(),
            height = height.toIntOrNull(),
            hypertension = hypertension,
            macrosomicBaby = macrosomicBaby,
            smoking = smoking,
            yearOfSmoking = yearOfSmoking
        )

        return AddProfileResult(
            result = repository.addProfile(addProfileResult)
        )
    }
}