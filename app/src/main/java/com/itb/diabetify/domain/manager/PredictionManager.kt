package com.itb.diabetify.domain.manager

import com.itb.diabetify.domain.model.Prediction
import kotlinx.coroutines.flow.Flow

interface PredictionManager {
    suspend fun savePrediction(prediction: Prediction)
    fun getLatestPrediction(): Flow<Prediction?>
    suspend fun clearPrediction()
}