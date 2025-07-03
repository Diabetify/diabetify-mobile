package com.itb.diabetify.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.itb.diabetify.domain.manager.NotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    
    @Inject
    lateinit var notificationManager: NotificationManager
    
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            context?.let {
                if (notificationManager.isDailyReminderEnabled()) {
                    notificationManager.scheduleDailyNotification()
                }
            }
        }
    }
}
