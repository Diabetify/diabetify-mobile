package com.itb.diabetify.domain.usecases.notification

import com.itb.diabetify.domain.manager.NotificationManager
import javax.inject.Inject

class CancelNotificationUseCase @Inject constructor(
    private val notificationManager: NotificationManager
) {
    operator fun invoke() {
        notificationManager.cancelDailyNotification()
    }
} 