package com.itb.diabetify.domain.usecases.activity

data class ActivityUseCases(
    val addActivity: AddActivityUseCase,
    val getActivityToday: GetActivityTodayUseCase,
    val getActivityRepository: GetActivityRepositoryUseCase,
    val updateActivity: UpdateActivityUseCase,
)