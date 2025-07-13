package com.itb.diabetify.domain.model.prediction

import com.itb.diabetify.domain.manager.WhatIfJobStatus
import kotlinx.coroutines.flow.Flow

data class AsyncWhatIfResult(
    val jobId: String? = null,
    val jobStatusFlow: Flow<WhatIfJobStatus>? = null,
    val error: String? = null,
    val onComplete: (() -> Unit)? = null
)
