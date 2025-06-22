package com.itb.diabetify.domain.repository

import com.itb.diabetify.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getAllReminders(): Flow<List<Reminder>>
    fun getUnreadReminders(): Flow<List<Reminder>>
    suspend fun getReminderById(id: Int): Reminder?
    suspend fun insertReminder(reminder: Reminder): Long
    suspend fun updateReminder(reminder: Reminder)
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun deleteReminderById(id: Int)
    suspend fun markReminderAsRead(id: Int, isRead: Boolean)
    suspend fun markAllRemindersAsRead()
    suspend fun deleteOldReminders()
    suspend fun createDailyReminderIfNotExists()
} 