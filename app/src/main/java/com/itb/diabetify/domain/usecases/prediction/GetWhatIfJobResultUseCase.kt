package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.data.remote.prediction.response.WhatIfJobResultResponse
import com.itb.diabetify.domain.repository.PredictionRepository
import com.itb.diabetify.util.Resource

class GetWhatIfJobResultUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(jobId: String): Resource<WhatIfJobResultResponse> {
        return repository.getWhatIfJobResult(jobId)
    }
}
