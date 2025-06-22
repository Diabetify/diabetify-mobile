package com.itb.diabetify.domain.usecases.prediction

data class PredictionUseCases(
    val getLatestPrediction: GetLatestPredictionUseCase,
    val getPredictionByDate: GetPredictionByDateUseCase,
    val getPredictionScoreByDate: GetPredictionScoreByDateUseCase,
    val predict: PredictUseCase,
)