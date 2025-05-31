package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.domain.model.prediction.GetPredictionScoreByDateResult
import com.itb.diabetify.domain.repository.PredictionRepository

class GetPredictionScoreByDateUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(
        startDate: String,
        endDate: String
    ): GetPredictionScoreByDateResult {
        return GetPredictionScoreByDateResult(
            result = repository.fetchPredictionScoreByDate(startDate, endDate)
        )
    }
}