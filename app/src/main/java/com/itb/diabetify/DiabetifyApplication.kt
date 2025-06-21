package com.itb.diabetify

import android.app.Application
import com.itb.diabetify.domain.usecases.notification.NotificationUseCases
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DiabetifyApplication: Application() {
    
    @Inject
    lateinit var notificationUseCases: NotificationUseCases
    
    override fun onCreate() {
        super.onCreate()
        
        notificationUseCases.scheduleNotification()
    }
}