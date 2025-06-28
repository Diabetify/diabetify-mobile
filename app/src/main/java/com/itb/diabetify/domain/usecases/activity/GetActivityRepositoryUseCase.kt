package com.itb.diabetify.domain.usecases.activity

import com.itb.diabetify.domain.model.Activity
import com.itb.diabetify.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow

class GetActivityRepositoryUseCase(
    private val repository: ActivityRepository
) {
    operator fun invoke(): Flow<Activity?> {
        return repository.getActivityToday()
    }
}