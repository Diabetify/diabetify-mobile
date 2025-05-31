package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.domain.model.prediction.GetLatestPredictionResult
import com.itb.diabetify.domain.repository.PredictionRepository

class GetLatestPredictionUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(): GetLatestPredictionResult {
        return GetLatestPredictionResult(
            result = repository.fetchLatestPrediction()
        )
    }
}