package com.itb.diabetify.domain.model.prediction

import com.itb.diabetify.data.remote.prediction.response.GetPredictionResponse
import com.itb.diabetify.util.Resource

class GetPredictionByDateResult (
    val result: Resource<GetPredictionResponse>? = null
)