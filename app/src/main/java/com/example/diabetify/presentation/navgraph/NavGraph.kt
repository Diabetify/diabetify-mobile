package com.example.diabetify.presentation.navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.diabetify.presentation.onboarding.OnBoardingScreen
import com.example.diabetify.presentation.onboarding.OnBoardingViewModel

@Composable
fun NavGraph(
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(
                route = Route.OnBoardingScreen.route
            ) {
                val viewModel: OnBoardingViewModel = hiltViewModel()
                OnBoardingScreen(
                    event = viewModel::onEvent,
                )
            }
        }

        navigation(
            route = Route.LoginNavigation.route,
            startDestination = Route.LoginScreen.route
        ) {
            composable(
                route = Route.LoginScreen.route
            ) {
                // login screen
                Text(text = "Login Screen")
            }
        }
    }
}