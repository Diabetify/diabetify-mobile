package com.example.diabetify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.diabetify.presentation.onboarding.OnBoardingScreen
import com.example.diabetify.ui.theme.DiabetifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            DiabetifyTheme {
                OnBoardingScreen()
            }
        }
    }
}