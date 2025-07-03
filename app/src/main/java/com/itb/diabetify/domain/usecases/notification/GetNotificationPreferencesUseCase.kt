package com.itb.diabetify.domain.usecases.notification

import com.itb.diabetify.domain.manager.NotificationManager
import javax.inject.Inject

class GetNotificationPreferencesUseCase @Inject constructor(
    private val notificationManager: NotificationManager
) {
    operator fun invoke(): Boolean {
        return notificationManager.isDailyReminderEnabled()
    }
}
