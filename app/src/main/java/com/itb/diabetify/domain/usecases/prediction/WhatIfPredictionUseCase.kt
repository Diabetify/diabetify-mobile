package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.data.remote.prediction.request.WhatIfPredictionRequest
import com.itb.diabetify.domain.model.prediction.WhatIfPredictionResult
import com.itb.diabetify.domain.repository.PredictionRepository

class WhatIfPredictionUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(
        smokingStatus: Int,
        avgSmokeCount: Int,
        weight: Int,
        isHypertension: Boolean,
        isCholesterol: Boolean,
    ): WhatIfPredictionResult {
        val smokingStatusError: String? = if (smokingStatus < 0 || smokingStatus > 2) "Smoking status must be between 0 and 2" else null
        val avgSmokeCountError: String? = if (avgSmokeCount < 0) "Average smoke count cannot be negative" else null
        val weightError: String? = if (weight < 30 || weight > 300) "Berat badan tidak valid" else null

        if (smokingStatusError != null) {
            return WhatIfPredictionResult(
                smokingStatusError = smokingStatusError
            )
        }

        if (avgSmokeCountError != null) {
            return WhatIfPredictionResult(
                avgSmokeCountError = avgSmokeCountError
            )
        }

        if (weightError != null) {
            return WhatIfPredictionResult(
                weightError = weightError
            )
        }

        val whatIfPredictionResult = WhatIfPredictionRequest(
            smokingStatus = smokingStatus,
            avgSmokeCount = avgSmokeCount,
            weight = weight,
            isHypertension = isHypertension,
            isCholesterol = isCholesterol
        )

        return WhatIfPredictionResult(
            result = repository.whatIfPrediction(whatIfPredictionResult)
        )
    }
}