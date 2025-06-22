package com.itb.diabetify.domain.usecases.reminder

data class ReminderUseCases(
    val getAllReminders: GetAllRemindersUseCase,
    val getUnreadReminders: GetUnreadRemindersUseCase,
    val addReminder: AddReminderUseCase,
    val addManualReminder: AddReminderUseCase,
    val markAllAsRead: CompleteRemindersUseCase,
    val createDailyReminder: CreateDailyReminderUseCase
) 