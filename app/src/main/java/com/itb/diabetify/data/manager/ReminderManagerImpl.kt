package com.itb.diabetify.data.manager

import com.itb.diabetify.domain.manager.ReminderManager
import com.itb.diabetify.domain.usecases.reminder.ReminderUseCases
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderManagerImpl @Inject constructor(
    private val reminderUseCases: ReminderUseCases
) : ReminderManager {
    
    override suspend fun createDailyReminderIfNotExists() {
        reminderUseCases.createDailyReminder()
    }
} 