package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.domain.model.prediction.PredictionResult
import com.itb.diabetify.domain.repository.PredictionRepository

class PredictUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(): PredictionResult {
        return PredictionResult(
            result = repository.predict()
        )
    }
}