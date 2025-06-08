package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.domain.model.prediction.PredictionResult
import com.itb.diabetify.domain.repository.PredictionRepository
import com.itb.diabetify.util.Resource

class PredictionUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(): PredictionResult {
        val predict = repository.predict()
        val explain = repository.explainPrediction()

        return PredictionResult(
            result = if (predict is Resource.Success && explain is Resource.Success) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Prediction or explanation failed")
            }
        )
    }
}