package com.itb.diabetify.domain.model.prediction

import com.itb.diabetify.data.remote.prediction.response.WhatIfJobResultResponse
import com.itb.diabetify.util.Resource

class WhatIfPredictionResult (
    val smokingStatusError: String? = null,
    val yearsOfSmokingError: String? = null,
    val avgSmokeCountError: String? = null,
    val weightError: String? = null,
    val physicalActivityFrequencyError: String? = null,
    val result: Resource<WhatIfJobResultResponse>? = null,
    val asyncResult: AsyncWhatIfResult? = null
)