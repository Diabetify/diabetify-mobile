package com.itb.diabetify.data.repository

import com.itb.diabetify.data.remote.prediction.PredictionApiService
import com.itb.diabetify.data.remote.prediction.response.GetPredictionScoreResponse
import com.itb.diabetify.domain.manager.PredictionManager
import com.itb.diabetify.domain.manager.TokenManager
import com.itb.diabetify.domain.model.Prediction
import com.itb.diabetify.domain.repository.PredictionRepository
import com.itb.diabetify.util.Resource
import kotlinx.coroutines.flow.Flow
import okio.IOException
import retrofit2.HttpException

class PredictionRepositoryImpl (
    private val predictionApiService: PredictionApiService,
    private val tokenManager: TokenManager,
    private val predictionManager: PredictionManager,
) : PredictionRepository {
    override suspend fun getToken(): String? {
        return tokenManager.getToken()
    }

    override suspend fun fetchLatestPrediction(): Resource<Unit> {
        return try {
            val response = predictionApiService.getPrediction(1)
            if (response.data.isEmpty()) {
                predictionManager.savePrediction(
                    Prediction(
                        riskScore = "0.0",
                        age = "0",
                        ageContribution = "0.0",
                        bmi = "0.0",
                        bmiContribution = "0.0",
                        brinkmanScore = "0.0",
                        brinkmanScoreContribution = "0.0",
                        isHypertension = "false",
                        isHypertensionContribution = "0.0",
                        isMacrosomicBaby = "false",
                        isMacrosomicBabyContribution = "0.0",
                        smokingStatus = "never",
                        smokingStatusContribution = "0.0",
                        physicalActivityMinutes = "0",
                        physicalActivityMinutesContribution = "0.0",
                    )
                )
                return Resource.Success(Unit)
            }

            response.data.firstOrNull()?.let { prediction ->
                val ageContribution = if (prediction.ageImpact == 1) prediction.ageContribution else -prediction.ageContribution
                val bmiContribution = if (prediction.bmiImpact == 1) prediction.bmiContribution else -prediction.bmiContribution
                val brinkmanContribution = if (prediction.brinkmanScoreImpact == 1)
                    prediction.brinkmanScoreContribution else -prediction.brinkmanScoreContribution
                val isHypertensionContribution = if (prediction.isHypertensionImpact == 1)
                    prediction.isHypertensionContribution else -prediction.isHypertensionContribution
                val isMacrosomicBabyContribution = if (prediction.isMacrosomicBabyImpact == 1)
                    prediction.isMacrosomicBabyContribution else -prediction.isMacrosomicBabyContribution
                val smokingStatusContribution = if (prediction.smokingStatusImpact == 1)
                    prediction.smokingStatusContribution else -prediction.smokingStatusContribution
                val physicalActivityContribution = if (prediction.physicalActivityMinutesImpact == 1)
                    prediction.physicalActivityMinutesContribution else -prediction.physicalActivityMinutesContribution

                predictionManager.savePrediction(
                    Prediction(
                        riskScore = prediction.riskScore.toString(),
                        age = prediction.age.toString(),
                        ageContribution = ageContribution.toString(),
                        bmi = prediction.bmi.toString(),
                        bmiContribution = bmiContribution.toString(),
                        brinkmanScore = prediction.brinkmanScore.toString(),
                        brinkmanScoreContribution = brinkmanContribution.toString(),
                        isHypertension = prediction.isHypertension.toString(),
                        isHypertensionContribution = isHypertensionContribution.toString(),
                        isMacrosomicBaby = prediction.isMacrosomicBaby.toString(),
                        isMacrosomicBabyContribution = isMacrosomicBabyContribution.toString(),
                        smokingStatus = prediction.smokingStatus.toString(),
                        smokingStatusContribution = smokingStatusContribution.toString(),
                        physicalActivityMinutes = prediction.physicalActivityMinutes.toString(),
                        physicalActivityMinutesContribution = physicalActivityContribution.toString(),
                    )
                )
            }

            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    override suspend fun fetchPredictionScoreByDate(
        startDate: String,
        endDate: String
    ): Resource<GetPredictionScoreResponse> {
        return try {
            val response = predictionApiService.getPredictionScoreByDate(startDate, endDate)
            Resource.Success(response)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    override fun getLatestPrediction(): Flow<Prediction?> {
        return predictionManager.getLatestPrediction()
    }
}