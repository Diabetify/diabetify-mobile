package com.itb.diabetify.domain.usecases.prediction

import com.itb.diabetify.domain.manager.PredictionJobStatus
import com.itb.diabetify.domain.repository.PredictionRepository
import com.itb.diabetify.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PredictBackgroundUseCase(
    private val repository: PredictionRepository
) {
    suspend operator fun invoke(scope: CoroutineScope, pollingIntervalMs: Long = 5000L) {
        scope.launch {
            try {
                val jobResult = repository.startPredictionJob()
                
                if (jobResult is Resource.Success) {
                    val jobId = jobResult.data?.data?.jobId
                    if (jobId != null) {
                        val jobStatusFlow = repository.pollPredictionJob(jobId, pollingIntervalMs)
                        jobStatusFlow.collect { status ->
                            when (status) {
                                is PredictionJobStatus.Completed -> {
                                    repository.fetchLatestPrediction()
                                    return@collect
                                }
                                is PredictionJobStatus.Failed -> {
                                    return@collect
                                }
                                else -> {
                                    // Continue polling
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Silent failure
            }
        }
    }
}
