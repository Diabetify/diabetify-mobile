package com.itb.diabetify.data.manager

import com.itb.diabetify.data.remote.prediction.PredictionApiService
import com.itb.diabetify.domain.manager.PredictionJobManager
import com.itb.diabetify.domain.manager.PredictionJobStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PredictionJobManagerImpl @Inject constructor(
    private val predictionApiService: PredictionApiService
) : PredictionJobManager {
    
    private val _jobStatus = MutableStateFlow<PredictionJobStatus>(PredictionJobStatus.Pending)
    private var currentJobId: String? = null
    private var isCancelled = false

    override suspend fun pollJobStatus(jobId: String, pollingIntervalMs: Long): StateFlow<PredictionJobStatus> {
        currentJobId = jobId
        isCancelled = false
        _jobStatus.value = PredictionJobStatus.Pending
        
        try {
            while (!isCancelled) {
                val response = predictionApiService.getPredictionJobStatus(jobId)
                
                when (response.data.status.lowercase()) {
                    "pending", "submitted" -> {
                        _jobStatus.value = PredictionJobStatus.Pending
                    }
                    "processing" -> {
                        _jobStatus.value = PredictionJobStatus.InProgress(response.data.progress)
                    }
                    "completed" -> {
                        _jobStatus.value = PredictionJobStatus.Completed
                        break
                    }
                    "failed" -> {
                        _jobStatus.value = PredictionJobStatus.Failed("Prediction job failed")
                        break
                    }
                    "cancelled" -> {
                        _jobStatus.value = PredictionJobStatus.Failed("Prediction job was cancelled")
                        break
                    }
                    else -> {
                        _jobStatus.value = PredictionJobStatus.InProgress(response.data.progress)
                    }
                }
                
                delay(pollingIntervalMs)
            }
        } catch (e: Exception) {
            if (!isCancelled) {
                _jobStatus.value = PredictionJobStatus.Failed(e.message ?: "Unknown error occurred")
            }
        }
        
        return _jobStatus.asStateFlow()
    }

    override suspend fun cancelJob(jobId: String) {
        if (currentJobId == jobId) {
            isCancelled = true
        }
    }
}
