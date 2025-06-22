package com.itb.diabetify.domain.usecases.reminder

import com.itb.diabetify.domain.model.Reminder
import com.itb.diabetify.domain.repository.ReminderRepository
import javax.inject.Inject

class AddManualReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(title: String, description: String): Long {
        val reminder = Reminder(
            title = title,
            description = description,
            isRead = false,
            createdAt = System.currentTimeMillis()
        )
        return reminderRepository.insertReminder(reminder)
    }
} 