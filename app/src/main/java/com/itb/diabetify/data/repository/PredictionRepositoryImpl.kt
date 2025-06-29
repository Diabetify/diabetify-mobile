package com.itb.diabetify.data.repository

import com.itb.diabetify.data.remote.prediction.PredictionApiService
import com.itb.diabetify.data.remote.prediction.response.GetPredictionResponse
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

    override suspend fun predict(): Resource<Unit> {
        return try {
            val response = predictionApiService.predict()
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
    }

    override suspend fun explainPrediction(): Resource<Unit> {
        return try {
            val response = predictionApiService.explainPrediction()
            fetchLatestPrediction()
            Resource.Success(Unit)
        } catch (e: IOException) {
            Resource.Error("${e.message}")
        } catch (e: HttpException) {
            Resource.Error("${e.message}")
        }
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
                        ageExplanation = "",
                        bmi = "0.0",
                        bmiContribution = "0.0",
                        bmiExplanation = "",
                        brinkmanScore = "0",
                        brinkmanScoreContribution = "0.0",
                        brinkmanScoreExplanation = "",
                        isHypertension = "false",
                        isHypertensionContribution = "0.0",
                        isCholesterol = "false",
                        isCholesterolContribution = "0.0",
                        isCholesterolExplanation = "",
                        isBloodline = "false",
                        isBloodlineContribution = "0.0",
                        isBloodlineExplanation = "",
                        isHypertensionExplanation = "",
                        isMacrosomicBaby = "0",
                        isMacrosomicBabyContribution = "0.0",
                        isMacrosomicBabyExplanation = "",
                        smokingStatus = "never",
                        smokingStatusContribution = "0.0",
                        smokingStatusExplanation = "",
                        avgSmokeCount = "0",
                        physicalActivityFrequency = "0",
                        physicalActivityFrequencyContribution = "0.0",
                        physicalActivityFrequencyExplanation = "",
                        createdAt = response.data.firstOrNull()?.createdAt ?: ""
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
                val isCholesterolContribution = if (prediction.isCholesterolImpact == 1)
                    prediction.isCholesterolContribution else -prediction.isCholesterolContribution
                val isBloodlineContribution = if (prediction.isBloodlineImpact == 1)
                    prediction.isBloodlineContribution else -prediction.isBloodlineContribution
                val isMacrosomicBabyContribution = if (prediction.isMacrosomicBabyImpact == 1)
                    prediction.isMacrosomicBabyContribution else -prediction.isMacrosomicBabyContribution
                val smokingStatusContribution = if (prediction.smokingStatusImpact == 1)
                    prediction.smokingStatusContribution else -prediction.smokingStatusContribution
                val physicalActivityContribution = if (prediction.physicalActivityFrequencyImpact == 1)
                    prediction.physicalActivityFrequencyContribution else -prediction.physicalActivityFrequencyContribution

                predictionManager.savePrediction(
                    Prediction(
                        riskScore = prediction.riskScore.toString(),
                        age = prediction.age.toString(),
                        ageContribution = ageContribution.toString(),
                        ageExplanation = prediction.ageExplanation,
                        bmi = prediction.bmi.toString(),
                        bmiContribution = bmiContribution.toString(),
                        bmiExplanation = prediction.bmiExplanation,
                        brinkmanScore = prediction.brinkmanScore.toString(),
                        brinkmanScoreContribution = brinkmanContribution.toString(),
                        brinkmanScoreExplanation = prediction.brinkmanScoreExplanation,
                        isHypertension = prediction.isHypertension.toString(),
                        isHypertensionContribution = isHypertensionContribution.toString(),
                        isHypertensionExplanation = prediction.isHypertensionExplanation,
                        isCholesterol = prediction.isCholesterol.toString(),
                        isCholesterolContribution = isCholesterolContribution.toString(),
                        isCholesterolExplanation = prediction.isCholesterolExplanation,
                        isBloodline = prediction.isBloodline.toString(),
                        isBloodlineContribution = isBloodlineContribution.toString(),
                        isBloodlineExplanation = prediction.isBloodlineExplanation,
                        isMacrosomicBaby = prediction.isMacrosomicBaby.toString(),
                        isMacrosomicBabyContribution = isMacrosomicBabyContribution.toString(),
                        isMacrosomicBabyExplanation = prediction.isMacrosomicBabyExplanation,
                        smokingStatus = prediction.smokingStatus.toString(),
                        smokingStatusContribution = smokingStatusContribution.toString(),
                        avgSmokeCount = prediction.avgSmokeCount.toString(),
                        smokingStatusExplanation = prediction.smokingStatusExplanation,
                        physicalActivityFrequency = prediction.physicalActivityFrequency.toString(),
                        physicalActivityFrequencyContribution = physicalActivityContribution.toString(),
                        physicalActivityFrequencyExplanation = prediction.physicalActivityFrequencyExplanation,
                        createdAt = prediction.createdAt
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

    override suspend fun fetchPredictionByDate(
        startDate: String,
        endDate: String
    ): Resource<GetPredictionResponse> {
        return try {
            val response = predictionApiService.getPredictionByDate(startDate, endDate)
            if (response.data.isEmpty()) {
                Resource.Success(GetPredictionResponse(emptyList(), "No predictions found", "success"))
            } else {
                Resource.Success(response)
            }
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