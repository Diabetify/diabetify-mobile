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
        macrosomicBaby: Int,
        smoking: Int,
        yearOfSmoking: Int,
        cholesterol: Boolean,
        bloodline: Boolean,
        physicalActivityFrequency: Int,
        smokeCount: Int
    ): AddProfileResult {
        val weightError: String? = if (weight < 30 || weight > 300) "Berat badan tidak valid" else null
        val heightError: String? = if (height < 100 || height > 250) "Tinggi badan tidak valid" else null
        val yearOfSmokingError: String? = if (yearOfSmoking != 0 && (yearOfSmoking < 10 || yearOfSmoking > 80)) "Tahun merokok tidak valid" else null
        val smokeCountError: String? = if (smokeCount != 0 && (smokeCount < 0 || smokeCount > 60)) "Jumlah rokok tidak valid" else null
        val physicalActivityFrequencyError: String? = if (physicalActivityFrequency < 0 || physicalActivityFrequency > 7) "Frekuensi aktivitas fisik tidak valid" else null

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

        if (smokeCountError != null) {
            return AddProfileResult(
                smokeCountError = smokeCountError
            )
        }

        if (physicalActivityFrequencyError != null) {
            return AddProfileResult(
                physicalActivityFrequencyError = physicalActivityFrequencyError
            )
        }

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