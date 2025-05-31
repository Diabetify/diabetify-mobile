package com.itb.diabetify.domain.repository

import com.itb.diabetify.domain.model.Prediction
import com.itb.diabetify.util.Resource
import kotlinx.coroutines.flow.Flow

interface PredictionRepository {
    suspend fun getToken(): String?
    suspend fun fetchLatestPrediction(): Resource<Unit>
    suspend fun fetchPredictionScoreByDate(startDate: String, endDate: String): Resource<Unit>
    fun getLatestPrediction(): Flow<Prediction?>
}