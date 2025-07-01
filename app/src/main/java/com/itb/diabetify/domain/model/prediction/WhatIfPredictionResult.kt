package com.itb.diabetify.domain.model.prediction

import com.itb.diabetify.data.remote.prediction.response.WhatIfPredictionResponse
import com.itb.diabetify.util.Resource

class WhatIfPredictionResult (
    val smokingStatusError: String? = null,
    val avgSmokeCountError: String? = null,
    val weightError: String? = null,
    val physicalActivityFrequencyError: String? = null,
    val result: Resource<WhatIfPredictionResponse>? = null
)