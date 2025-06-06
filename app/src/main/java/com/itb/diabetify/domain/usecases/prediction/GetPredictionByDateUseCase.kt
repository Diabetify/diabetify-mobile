package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.domain.model.prediction.GetPredictionByDateResult
import com.itb.diabetify.domain.repository.PredictionRepository

class GetPredictionByDateUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(
        startDate: String,
        endDate: String
    ): GetPredictionByDateResult {
        return GetPredictionByDateResult(
            result = repository.fetchPredictionByDate(startDate, endDate)
        )
    }
}