package com.itb.diabetify.domain.usecases.reminder

import com.itb.diabetify.domain.model.Reminder
import com.itb.diabetify.domain.repository.ReminderRepository
import javax.inject.Inject

class AddReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(reminder: Reminder): Long {
        return reminderRepository.insertReminder(reminder)
    }
} 