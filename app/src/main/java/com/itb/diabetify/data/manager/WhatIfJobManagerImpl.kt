package com.itb.diabetify.data.manager

import com.itb.diabetify.data.remote.prediction.PredictionApiService
import com.itb.diabetify.domain.manager.WhatIfJobManager
import com.itb.diabetify.domain.manager.WhatIfJobStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WhatIfJobManagerImpl @Inject constructor(
    private val predictionApiService: PredictionApiService
) : WhatIfJobManager {
    
    private val _jobStatus = MutableStateFlow<WhatIfJobStatus>(WhatIfJobStatus.Pending)
    private var currentJobId: String? = null
    private var isCancelled = false

    override suspend fun pollJobStatus(jobId: String, pollingIntervalMs: Long): StateFlow<WhatIfJobStatus> {
        currentJobId = jobId
        isCancelled = false
        _jobStatus.value = WhatIfJobStatus.Pending
        
        try {
            while (!isCancelled) {
                val response = predictionApiService.getWhatIfJobStatus(jobId)
                
                when (response.data.status.lowercase()) {
                    "pending", "submitted" -> {
                        _jobStatus.value = WhatIfJobStatus.Pending
                    }
                    "processing" -> {
                        _jobStatus.value = WhatIfJobStatus.InProgress(50)
                    }
                    "completed" -> {
                        _jobStatus.value = WhatIfJobStatus.Completed
                        break
                    }
                    "failed" -> {
                        _jobStatus.value = WhatIfJobStatus.Failed("What-if prediction job failed")
                        break
                    }
                    "cancelled" -> {
                        _jobStatus.value = WhatIfJobStatus.Failed("What-if prediction job was cancelled")
                        break
                    }
                    else -> {
                        _jobStatus.value = WhatIfJobStatus.InProgress(50)
                    }
                }
                
                delay(pollingIntervalMs)
            }
        } catch (e: Exception) {
            if (!isCancelled) {
                _jobStatus.value = WhatIfJobStatus.Failed(e.message ?: "Unknown error occurred")
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
