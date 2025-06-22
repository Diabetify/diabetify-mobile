package com.itb.diabetify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itb.diabetify.domain.model.Reminder

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

fun ReminderEntity.toDomainModel(): Reminder {
    return Reminder(
        id = id,
        title = title,
        description = description,
        isRead = isRead,
        createdAt = createdAt
    )
}

fun Reminder.toEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        title = title,
        description = description,
        isRead = isRead,
        createdAt = createdAt
    )
} 