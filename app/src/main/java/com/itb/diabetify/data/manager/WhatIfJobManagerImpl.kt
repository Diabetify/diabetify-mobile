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
    
    private val activeJobs = mutableMapOf<String, MutableStateFlow<WhatIfJobStatus>>()

    override suspend fun pollJobStatus(jobId: String, pollingIntervalMs: Long): StateFlow<WhatIfJobStatus> {
        val jobFlow = MutableStateFlow<WhatIfJobStatus>(WhatIfJobStatus.Pending)
        activeJobs[jobId] = jobFlow
        
        try {
            while (true) {
                val response = predictionApiService.getWhatIfJobStatus(jobId)
                
                when (response.data.status.lowercase()) {
                    "pending", "submitted" -> {
                        jobFlow.value = WhatIfJobStatus.Pending
                    }
                    "processing" -> {
                        jobFlow.value = WhatIfJobStatus.InProgress(50)
                    }
                    "completed" -> {
                        jobFlow.value = WhatIfJobStatus.Completed
                        activeJobs.remove(jobId)
                        break
                    }
                    "failed" -> {
                        jobFlow.value = WhatIfJobStatus.Failed("What-if prediction job failed")
                        activeJobs.remove(jobId)
                        break
                    }
                    "cancelled" -> {
                        jobFlow.value = WhatIfJobStatus.Failed("What-if prediction job was cancelled")
                        activeJobs.remove(jobId)
                        break
                    }
                    else -> {
                        jobFlow.value = WhatIfJobStatus.InProgress(50)
                    }
                }
                
                delay(pollingIntervalMs)
            }
        } catch (e: Exception) {
            jobFlow.value = WhatIfJobStatus.Failed(e.message ?: "Unknown error occurred")
            activeJobs.remove(jobId)
        }
        
        return jobFlow.asStateFlow()
    }

    override suspend fun cancelJob(jobId: String) {
        activeJobs[jobId]?.let { jobFlow ->
            jobFlow.value = WhatIfJobStatus.Failed("Job cancelled")
            activeJobs.remove(jobId)
        }
    }
}
