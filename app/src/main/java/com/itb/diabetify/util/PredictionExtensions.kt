package com.itb.diabetify.util

import com.itb.diabetify.domain.manager.PredictionJobStatus
import com.itb.diabetify.domain.manager.WhatIfJobStatus
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCases
import com.itb.diabetify.data.remote.prediction.request.WhatIfPredictionRequest
import kotlinx.coroutines.CoroutineScope

private var isWhatIfPredictionInProgress = false

suspend fun PredictionUseCases.handleAsyncPrediction(
    scope: CoroutineScope,
    pollingIntervalMs: Long = 2000L,
    onPending: () -> Unit = {},
    onProgress: (progress: Int) -> Unit = {},
    onCompleted: suspend () -> Unit = {},
    onFailed: (error: String) -> Unit = {}
) {
    val asyncResult = this.predictAsync(pollingIntervalMs)
    
    if (asyncResult.error != null) {
        onFailed(asyncResult.error)
        return
    }
    
    asyncResult.jobStatusFlow?.let { statusFlow ->
        statusFlow.collect { status ->
            when (status) {
                is PredictionJobStatus.Pending -> onPending()
                is PredictionJobStatus.InProgress -> onProgress(status.progress)
                is PredictionJobStatus.Completed -> {
                    this@handleAsyncPrediction.getLatestPrediction()
                    onCompleted()
                    return@collect
                }
                is PredictionJobStatus.Failed -> {
                    onFailed(status.error)
                    return@collect
                }
            }
        }
    }
}

suspend fun PredictionUseCases.handleAsyncWhatIfPrediction(
    scope: CoroutineScope,
    whatIfRequest: WhatIfPredictionRequest,
    pollingIntervalMs: Long = 2000L,
    onPending: () -> Unit = {},
    onProgress: (progress: Int) -> Unit = {},
    onCompleted: suspend (jobId: String) -> Unit = {},
    onFailed: (error: String) -> Unit = {}
) {
    if (isWhatIfPredictionInProgress) {
        onFailed("Sudah ada prediksi what-if yang sedang berjalan")
        return
    }
    
    isWhatIfPredictionInProgress = true
    
    val asyncResult = this.whatIfPredictionAsync(whatIfRequest, pollingIntervalMs)
    
    if (asyncResult.error != null) {
        isWhatIfPredictionInProgress = false
        onFailed(asyncResult.error)
        return
    }
    
    asyncResult.jobStatusFlow?.let { statusFlow ->
        statusFlow.collect { status ->
            when (status) {
                is WhatIfJobStatus.Pending -> onPending()
                is WhatIfJobStatus.InProgress -> onProgress(status.progress)
                is WhatIfJobStatus.Completed -> {
                        asyncResult.jobId?.let { jobId ->
                            onCompleted(jobId)
                        }
                        asyncResult.onComplete?.invoke()
                        isWhatIfPredictionInProgress = false
                        return@collect
                    }
                    is WhatIfJobStatus.Failed -> {
                        onFailed(status.error)
                        asyncResult.onComplete?.invoke()
                        isWhatIfPredictionInProgress = false
                        return@collect
                    }
            }
        }
    }
}
