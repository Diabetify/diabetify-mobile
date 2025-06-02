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
                        ageImpact = "0",
                        bmi = "0.0",
                        bmiContribution = "0.0",
                        bmiImpact = "0",
                        brinkmanScore = "0.0",
                        brinkmanScoreContribution = "0.0",
                        brinkmanScoreImpact = "0",
                        isHypertension = "false",
                        isHypertensionContribution = "0.0",
                        isHypertensionImpact = "0",
                        isMacrosomicBaby = "false",
                        isMacrosomicBabyContribution = "0.0",
                        isMacrosomicBabyImpact = "0",
                        smokingStatus = "never",
                        smokingStatusContribution = "0.0",
                        smokingStatusImpact = "0",
                        physicalActivityMinutes = "0",
                        physicalActivityMinutesContribution = "0.0",
                        physicalActivityMinutesImpact = "0"
                    )
                )
                return Resource.Success(Unit)
            }

            response.data.firstOrNull()?.let { prediction ->
                predictionManager.savePrediction(
                    Prediction(
                        riskScore = prediction.riskScore.toString(),
                        age = prediction.age.toString(),
                        ageContribution = prediction.ageContribution.toString(),
                        ageImpact = prediction.ageImpact.toString(),
                        bmi = prediction.bmi.toString(),
                        bmiContribution = prediction.bmiContribution.toString(),
                        bmiImpact = prediction.bmiImpact.toString(),
                        brinkmanScore = prediction.brinkmanScore.toString(),
                        brinkmanScoreContribution = prediction.brinkmanScoreContribution.toString(),
                        brinkmanScoreImpact = prediction.brinkmanScoreImpact.toString(),
                        isHypertension = prediction.isHypertension.toString(),
                        isHypertensionContribution = prediction.isHypertensionContribution.toString(),
                        isHypertensionImpact = prediction.isHypertensionImpact.toString(),
                        isMacrosomicBaby = prediction.isMacrosomicBaby.toString(),
                        isMacrosomicBabyContribution = prediction.isMacrosomicBabyContribution.toString(),
                        isMacrosomicBabyImpact = prediction.isMacrosomicBabyImpact.toString(),
                        smokingStatus = prediction.smokingStatus.toString(),
                        smokingStatusContribution = prediction.smokingStatusContribution.toString(),
                        smokingStatusImpact = prediction.smokingStatusImpact.toString(),
                        physicalActivityMinutes = prediction.physicalActivityMinutes.toString(),
                        physicalActivityMinutesContribution = prediction.physicalActivityMinutesContribution.toString(),
                        physicalActivityMinutesImpact = prediction.physicalActivityMinutesImpact.toString()
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