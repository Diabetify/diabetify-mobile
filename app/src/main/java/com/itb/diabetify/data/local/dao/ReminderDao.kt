package com.itb.diabetify.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.itb.diabetify.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders ORDER BY createdAt DESC")
    fun getAllReminders(): Flow<List<ReminderEntity>>
    
    @Query("SELECT * FROM reminders WHERE isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadReminders(): Flow<List<ReminderEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long
    
    @Query("UPDATE reminders SET isRead = 1")
    suspend fun markAllRemindersAsRead()
    
    @Query("DELETE FROM reminders WHERE createdAt < :timestamp")
    suspend fun deleteRemindersOlderThan(timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM reminders WHERE title = :title AND DATE(createdAt/1000, 'unixepoch') = DATE(:timestamp/1000, 'unixepoch')")
    suspend fun checkIfDailyReminderExists(title: String, timestamp: Long): Int
} 