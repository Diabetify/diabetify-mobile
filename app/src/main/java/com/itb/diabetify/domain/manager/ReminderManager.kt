package com.itb.diabetify.domain.manager

interface ReminderManager {
    suspend fun createDailyReminderIfNotExists()
}