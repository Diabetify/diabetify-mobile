package com.itb.diabetify.domain.usecases.reminder

data class ReminderUseCases(
    val getAllReminders: GetAllRemindersUseCase,
    val getUnreadReminders: GetUnreadRemindersUseCase,
    val addReminder: AddReminderUseCase,
    val addManualReminder: AddManualReminderUseCase,
    val markAllAsRead: MarkAllRemindersAsReadUseCase,
    val createDailyReminder: CreateDailyReminderUseCase
) 