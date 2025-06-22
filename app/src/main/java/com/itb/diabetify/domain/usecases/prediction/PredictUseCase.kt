package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.domain.model.prediction.PredictionResult
import com.itb.diabetify.domain.repository.PredictionRepository
import com.itb.diabetify.util.Resource

class PredictUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(): PredictionResult {
        return when (val predict = repository.predict()) {
            is Resource.Success -> {
                when (val explain = repository.explainPrediction()) {
                    is Resource.Success -> PredictionResult(Resource.Success(Unit))
                    is Resource.Error -> PredictionResult(Resource.Error(explain.message ?: "Explanation failed"))
                    else -> PredictionResult(Resource.Error("Unexpected error during explanation"))
                }
            }
            is Resource.Error -> PredictionResult(Resource.Error(predict.message ?: "Prediction failed"))
            else -> PredictionResult(Resource.Error("Unexpected error during prediction"))
        }
    }
}