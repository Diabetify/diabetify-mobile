package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.domain.model.Prediction
import com.itb.diabetify.domain.repository.PredictionRepository
import kotlinx.coroutines.flow.Flow

class GetLatestPredictionRepositoryUseCase(
    private val repository: PredictionRepository
) {
    operator fun invoke(): Flow<Prediction?> {
        return repository.getLatestPrediction()
    }
}