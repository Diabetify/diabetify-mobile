package com.itb.diabetify.domain.usecases.notification

data class NotificationUseCases(
    val scheduleNotification: ScheduleNotificationUseCase,
    val cancelNotification: CancelNotificationUseCase
) 