package com.itb.diabetify.domain.usecases.reminder

import com.itb.diabetify.domain.repository.ReminderRepository
import javax.inject.Inject

class MarkAllRemindersAsReadUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke() {
        reminderRepository.markAllRemindersAsRead()
    }
} 