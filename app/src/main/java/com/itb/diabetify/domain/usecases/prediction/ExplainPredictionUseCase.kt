package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.domain.model.prediction.ExplainPredictionResult
import com.itb.diabetify.domain.repository.PredictionRepository

class ExplainPredictionUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(): ExplainPredictionResult {
        return ExplainPredictionResult(
            result = repository.explainPrediction()
        )
    }
}