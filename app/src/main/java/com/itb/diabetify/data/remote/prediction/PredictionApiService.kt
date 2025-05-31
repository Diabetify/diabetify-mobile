package com.itb.diabetify.data.remote.prediction

import com.itb.diabetify.data.remote.prediction.response.GetPredictionResponse
import com.itb.diabetify.data.remote.prediction.response.GetPredictionScoreResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PredictionApiService {
    @GET("prediction/me")
    suspend fun getPrediction(
        @Query("limit") limit: Int = 1,
    ): GetPredictionResponse

    @GET("prediction/me/score")
    suspend fun getPredictionScoreByDate(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): GetPredictionScoreResponse
}