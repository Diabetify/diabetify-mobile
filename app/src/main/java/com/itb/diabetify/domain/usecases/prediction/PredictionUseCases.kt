package com.itb.diabetify.domain.usecases.prediction

data class PredictionUseCases(
    val getLatestPredictionRepository: GetLatestPredictionRepositoryUseCase,
    val getLatestPrediction: GetLatestPredictionUseCase,
    val getPredictionByDate: GetPredictionByDateUseCase,
    val getPredictionScoreByDate: GetPredictionScoreByDateUseCase,
    val predict: PredictUseCase,
    val whatIfPrediction: WhatIfPredictionUseCase
)