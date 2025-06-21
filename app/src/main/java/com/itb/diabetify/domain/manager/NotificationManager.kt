package com.itb.diabetify.domain.manager

interface NotificationManager {
    fun showDailyReminder()
    fun scheduleDailyNotification()
    fun cancelDailyNotification()
} 