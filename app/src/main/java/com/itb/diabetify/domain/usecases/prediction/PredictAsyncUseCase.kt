package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.domain.model.prediction.AsyncPredictionResult
import com.itb.diabetify.domain.repository.PredictionRepository
import com.itb.diabetify.util.Resource

class PredictAsyncUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(pollingIntervalMs: Long = 2000L): AsyncPredictionResult {
        val jobResult = repository.startPredictionJob()
        
        return when {
            jobResult is Resource.Success -> {
                val jobId = jobResult.data?.data?.jobId
                if (jobId != null) {
                    AsyncPredictionResult(
                        jobId = jobId,
                        jobStatusFlow = repository.pollPredictionJob(jobId, pollingIntervalMs)
                    )
                } else {
                    AsyncPredictionResult(
                        error = "Gagal mendapatkan job ID dari respons prediksi"
                    )
                }
            }
            jobResult is Resource.Error -> {
                AsyncPredictionResult(
                    error = jobResult.message ?: "Gagal memulai prediksi"
                )
            }
            else -> {
                AsyncPredictionResult(
                    error = "Terjadi kesalahan saat memulai prediksi"
                )
            }
        }
    }
}
