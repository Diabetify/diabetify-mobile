package com.itb.diabetify.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.itb.diabetify.data.local.dao.ReminderDao
import com.itb.diabetify.data.local.entity.ReminderEntity

@Database(
    entities = [ReminderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DiabetifyDatabase : RoomDatabase() {
    
    abstract fun reminderDao(): ReminderDao
    
    companion object {
        const val DATABASE_NAME = "diabetify_database"
    }
} 