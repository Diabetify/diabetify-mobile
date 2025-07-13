package com.itb.diabetify.domain.manager

import kotlinx.coroutines.flow.StateFlow

interface WhatIfJobManager {
    suspend fun pollJobStatus(jobId: String, pollingIntervalMs: Long = 2000L): StateFlow<WhatIfJobStatus>
    suspend fun cancelJob(jobId: String)
}

sealed class WhatIfJobStatus {
    object Pending : WhatIfJobStatus()
    data class InProgress(val progress: Int) : WhatIfJobStatus()
    object Completed : WhatIfJobStatus()
    data class Failed(val error: String) : WhatIfJobStatus()
}
