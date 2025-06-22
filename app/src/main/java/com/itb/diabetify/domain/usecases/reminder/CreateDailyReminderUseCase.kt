package com.itb.diabetify.domain.usecases.reminder

import com.itb.diabetify.domain.repository.ReminderRepository
import javax.inject.Inject

class CreateDailyReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke() {
        reminderRepository.createDailyReminderIfNotExists()
        reminderRepository.deleteOldReminders()
    }
} 