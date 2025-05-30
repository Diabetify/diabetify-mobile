package com.itb.diabetify.domain.usecases.activity

import com.itb.diabetify.domain.model.activity.GetActivityTodayResult
import com.itb.diabetify.domain.repository.ActivityRepository

class GetActivityTodayUseCase(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(): GetActivityTodayResult {
        return GetActivityTodayResult(
            result = repository.fetchActivityToday()
        )
    }
}