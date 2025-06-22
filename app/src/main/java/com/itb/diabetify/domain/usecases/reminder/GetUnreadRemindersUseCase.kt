package com.itb.diabetify.domain.usecases.reminder

import com.itb.diabetify.domain.model.Reminder
import com.itb.diabetify.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUnreadRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository
) {
    operator fun invoke(): Flow<List<Reminder>> {
        return reminderRepository.getUnreadReminders()
    }
} 