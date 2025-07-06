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
        ageOfSmoking: Int,
        ageOfStopSmoking: Int,
        cholesterol: Boolean,
        bloodline: Boolean,
        physicalActivityFrequency: Int,
        smokeCount: Int
    ): AddProfileResult {
        val weightError: String? = if (weight < 30 || weight > 300) "Berat badan tidak valid" else null
        val heightError: String? = if (height < 100 || height > 250) "Tinggi badan tidak valid" else null
        val macrosomicBabyError: String? = if (macrosomicBaby < 0 || macrosomicBaby > 2) "Status bayi makrosomia tidak valid" else null
        val smokingError: String? = if (smoking < 0 || smoking > 2) "Status merokok tidak valid" else null
        val ageOfSmokingError: String? = if (ageOfSmoking != 0 && (ageOfSmoking < 10 || ageOfSmoking > 80)) "Usia mulai merokok tidak valid" else null
        val ageOfStopSmokingError: String? = if (ageOfStopSmoking != 0 && (ageOfStopSmoking < 10 || ageOfStopSmoking > 80)) "Usia berhenti merokok tidak valid" else null
        val physicalActivityFrequencyError: String? = if (physicalActivityFrequency < 0 || physicalActivityFrequency > 7) "Frekuensi aktivitas fisik tidak valid" else null
        val smokeCountError: String? = if (smokeCount < 0 || smokeCount > 60) "Jumlah rokok tidak valid" else null

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

        if (macrosomicBabyError != null) {
            return AddProfileResult(
                macrosomicBabyError = macrosomicBabyError
            )
        }

        if (smokingError != null) {
            return AddProfileResult(
                smokingError = smokingError
            )
        }

        if (ageOfSmokingError != null) {
            return AddProfileResult(
                ageOfSmokingError = ageOfSmokingError
            )
        }

        if (ageOfStopSmokingError != null) {
            return AddProfileResult(
                ageOfStopSmokingError = ageOfStopSmokingError
            )
        }

        if (physicalActivityFrequencyError != null) {
            return AddProfileResult(
                physicalActivityFrequencyError = physicalActivityFrequencyError
            )
        }

        if (smokeCountError != null) {
            return AddProfileResult(
                smokeCountError = smokeCountError
            )
        }

        val addProfileResult = AddProfileRequest(
            weight = weight,
            height = height,
            hypertension = hypertension,
            macrosomicBaby = macrosomicBaby,
            smoking = smoking,
            ageOfSmoking = ageOfSmoking,
            ageOfStopSmoking = ageOfStopSmoking,
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