package com.itb.diabetify.domain.usecases.profile

import com.itb.diabetify.data.remote.profile.request.AddProfileRequest
import com.itb.diabetify.domain.model.profile.AddProfileResult
import com.itb.diabetify.domain.repository.ProfileRepository

class AddProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        weight: Int,
        height: Int,
        hypertension: Boolean,
        macrosomicBaby: Boolean,
        smoking: Boolean,
        yearOfSmoking: Int?,
        cholesterol: Boolean,
        bloodline: Boolean,
        physicalActivityFrequency: Int,
        smokeCount: Int?
    ): AddProfileResult {
        val addProfileResult = AddProfileRequest(
            weight = weight,
            height = height,
            hypertension = hypertension,
            macrosomicBaby = macrosomicBaby,
            smoking = smoking,
            yearOfSmoking = yearOfSmoking,
            cholesterol = cholesterol,
            bloodline = bloodline,
            physicalActivityFrequency = physicalActivityFrequency,
            smokeCount = smokeCount
        )

        return AddProfileResult(
            result = repository.addProfile(addProfileResult)
        )
    }
}