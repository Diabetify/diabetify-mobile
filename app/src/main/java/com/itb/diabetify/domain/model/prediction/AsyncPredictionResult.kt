package com.itb.diabetify.domain.model.prediction

import com.itb.diabetify.domain.manager.PredictionJobStatus
import kotlinx.coroutines.flow.Flow

data class AsyncPredictionResult(
    val jobId: String? = null,
    val jobStatusFlow: Flow<PredictionJobStatus>? = null,
    val error: String? = null
)
