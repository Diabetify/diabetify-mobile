package com.itb.diabetify

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.itb.diabetify.domain.usecases.notification.NotificationUseCases
import com.itb.diabetify.presentation.navgraph.AuthNavGraph
import com.itb.diabetify.ui.theme.DiabetifyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()
    
    @Inject
    lateinit var notificationUseCases: NotificationUseCases
    
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            notificationUseCases.scheduleNotification()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                viewModel.splashCondition
            }
        }
        
        requestNotificationPermission()

        setContent {
            DiabetifyTheme {
                val startDestination = viewModel.startDestination
                if (startDestination != null) {
                    AuthNavGraph(
                        startDestination = startDestination,
                        onRetryConnection = { viewModel.retryConnection() }
                    )
                }
            }
        }
    }
    
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    notificationUseCases.scheduleNotification()
                }
                else -> {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            notificationUseCases.scheduleNotification()
        }
    }
}