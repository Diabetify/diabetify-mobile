package com.itb.diabetify

import android.app.Application
import com.itb.diabetify.domain.usecases.notification.NotificationUseCases
import com.itb.diabetify.domain.usecases.reminder.ReminderUseCases
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class DiabetifyApplication: Application() {
    
    @Inject
    lateinit var notificationUseCases: NotificationUseCases
    
    @Inject
    lateinit var reminderUseCases: ReminderUseCases
    
    override fun onCreate() {
        super.onCreate()
        
        notificationUseCases.scheduleNotification()
        
        CoroutineScope(Dispatchers.IO).launch {
            reminderUseCases.createDailyReminder()
        }
    }
}