package com.example.diabetify

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.diabetify.domain.usecases.AppEntryUseCases
import com.example.diabetify.presentation.onboarding.OnBoardingScreen
import com.example.diabetify.ui.theme.DiabetifyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appEntryUseCases: AppEntryUseCases

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        lifecycleScope.launch {
            appEntryUseCases.readAppEntry().collect{
                Log.d("MainActivity", "App entry: ${it.toString()}")
            }

        }
        setContent {
            DiabetifyTheme {
                OnBoardingScreen()
            }
        }
    }
}