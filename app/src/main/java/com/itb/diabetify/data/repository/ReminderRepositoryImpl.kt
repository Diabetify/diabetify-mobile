package com.itb.diabetify.data.repository

import com.itb.diabetify.data.local.dao.ReminderDao
import com.itb.diabetify.data.local.entity.toDomainModel
import com.itb.diabetify.data.local.entity.toEntity
import com.itb.diabetify.domain.model.Reminder
import com.itb.diabetify.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import java.util.concurrent.TimeUnit

@Singleton
class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository {
    
    override fun getAllReminders(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getUnreadReminders(): Flow<List<Reminder>> {
        return reminderDao.getUnreadReminders().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override suspend fun getReminderById(id: Int): Reminder? {
        return reminderDao.getReminderById(id)?.toDomainModel()
    }
    
    override suspend fun insertReminder(reminder: Reminder): Long {
        return reminderDao.insertReminder(reminder.toEntity())
    }
    
    override suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(reminder.toEntity())
    }
    
    override suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(reminder.toEntity())
    }
    
    override suspend fun deleteReminderById(id: Int) {
        reminderDao.deleteReminderById(id)
    }
    
    override suspend fun markReminderAsRead(id: Int, isRead: Boolean) {
        reminderDao.updateReminderReadStatus(id, isRead)
    }
    
    override suspend fun markAllRemindersAsRead() {
        reminderDao.markAllRemindersAsRead()
    }
    
    override suspend fun deleteOldReminders() {
        val sevenDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
        reminderDao.deleteRemindersOlderThan(sevenDaysAgo)
    }
    
    override suspend fun createDailyReminderIfNotExists() {
        val currentTime = System.currentTimeMillis()
        val reminderTitle = "Input Data Aktivitas Harian"
        
        val reminderExists = reminderDao.checkIfDailyReminderExists(reminderTitle, currentTime) > 0
        
        if (!reminderExists) {
            val dailyReminder = Reminder(
                title = reminderTitle,
                description = "Jangan lupa untuk mencatat aktivitas harian Anda (aktivitas fisik dan kebiasaan merokok)",
                isRead = false,
                createdAt = currentTime
            )
            reminderDao.insertReminder(dailyReminder.toEntity())
        }
    }
} 