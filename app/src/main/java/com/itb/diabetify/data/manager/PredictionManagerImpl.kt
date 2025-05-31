package com.itb.diabetify.data.manager

import com.itb.diabetify.domain.manager.PredictionManager
import com.itb.diabetify.domain.model.Prediction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PredictionManagerImpl @Inject constructor() : PredictionManager {
    private val _predictionData = MutableStateFlow<Prediction?>(null)

    override suspend fun savePrediction(prediction: Prediction) {
        _predictionData.value = prediction
    }

    override fun getLatestPrediction(): Flow<Prediction?> {
        return _predictionData.asStateFlow()
    }

    override suspend fun clearPrediction() {
        _predictionData.value = null
    }

}