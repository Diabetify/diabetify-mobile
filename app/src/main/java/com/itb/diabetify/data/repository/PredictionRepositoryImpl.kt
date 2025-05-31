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
            response.data?.let { prediction ->
                predictionManager.savePrediction(
                    Prediction(
                        riskScore = prediction[0]?.riskScore.toString(),
                        age = prediction[0]?.age.toString(),
                        ageContribution = prediction[0]?.ageContribution.toString(),
                        ageImpact = prediction[0]?.ageImpact.toString(),
                        bmi = prediction[0]?.bmi.toString(),
                        bmiContribution = prediction[0]?.bmiContribution.toString(),
                        bmiImpact = prediction[0]?.bmiImpact.toString(),
                        brinkmanScore = prediction[0]?.brinkmanScore.toString(),
                        brinkmanScoreContribution = prediction[0]?.brinkmanScoreContribution.toString(),
                        brinkmanScoreImpact = prediction[0]?.brinkmanScoreImpact.toString(),
                        isHypertension = prediction[0]?.isHypertension.toString(),
                        isHypertensionContribution = prediction[0]?.isHypertensionContribution.toString(),
                        isHypertensionImpact = prediction[0]?.isHypertensionImpact.toString(),
                        isMacrosomicBaby = prediction[0]?.isMacrosomicBaby.toString(),
                        isMacrosomicBabyContribution = prediction[0]?.isMacrosomicBabyContribution.toString(),
                        isMacrosomicBabyImpact = prediction[0]?.isMacrosomicBabyImpact.toString(),
                        smokingStatus = prediction[0]?.smokingStatus.toString(),
                        smokingStatusContribution = prediction[0]?.smokingStatusContribution.toString(),
                        smokingStatusImpact = prediction[0]?.smokingStatusImpact.toString(),
                        physicalActivityMinutes = prediction[0]?.physicalActivityMinutes.toString(),
                        physicalActivityMinutesContribution = prediction[0]?.physicalActivityMinutesContribution.toString(),
                        physicalActivityMinutesImpact = prediction[0]?.physicalActivityMinutesImpact.toString()
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