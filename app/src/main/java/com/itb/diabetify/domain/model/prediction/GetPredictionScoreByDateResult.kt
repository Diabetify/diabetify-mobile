package com.itb.diabetify.domain.model.prediction

import com.itb.diabetify.data.remote.prediction.response.GetPredictionScoreResponse
import com.itb.diabetify.util.Resource

class GetPredictionScoreByDateResult (
    val result: Resource<GetPredictionScoreResponse>? = null
)