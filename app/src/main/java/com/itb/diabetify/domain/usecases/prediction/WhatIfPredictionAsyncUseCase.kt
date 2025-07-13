package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.data.remote.prediction.request.WhatIfPredictionRequest
import com.itb.diabetify.domain.model.prediction.AsyncWhatIfResult
import com.itb.diabetify.domain.repository.PredictionRepository
import com.itb.diabetify.util.Resource

class WhatIfPredictionAsyncUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(
        whatIfRequest: WhatIfPredictionRequest,
        pollingIntervalMs: Long = 2000L
    ): AsyncWhatIfResult {
        val jobResult = repository.startWhatIfJob(whatIfRequest)
        
        return when {
            jobResult is Resource.Success -> {
                val jobId = jobResult.data?.data?.jobId
                if (jobId != null) {
                    AsyncWhatIfResult(
                        jobId = jobId,
                        jobStatusFlow = repository.pollWhatIfJob(jobId, pollingIntervalMs)
                    )
                } else {
                    AsyncWhatIfResult(
                        error = "Gagal mendapatkan job ID dari respons what-if"
                    )
                }
            }
            jobResult is Resource.Error -> {
                AsyncWhatIfResult(
                    error = jobResult.message ?: "Gagal memulai what-if prediksi"
                )
            }
            else -> {
                AsyncWhatIfResult(
                    error = "Terjadi kesalahan yang tidak diketahui"
                )
            }
        }
    }
}
