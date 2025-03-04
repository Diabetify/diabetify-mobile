package com.example.diabetify.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.diabetify.presentation.login.LoginScreen
import com.example.diabetify.presentation.onboarding.OnBoardingScreen
import com.example.diabetify.presentation.onboarding.OnBoardingViewModel
import com.example.diabetify.presentation.register.BiodataScreen
import com.example.diabetify.presentation.register.RegisterScreen

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
            route = Route.RegisterNavigation.route,
            startDestination = Route.RegisterScreen.route
        ) {
            // Create a ViewModel scoped to this navigation graph
//            val sharedViewModel: RegisterSharedViewModel = hiltViewModel(
//                navController.getBackStackEntry(Route.RegisterNavigation.route)
//            )

            composable(
                route = Route.RegisterScreen.route
            ) {
                RegisterScreen(
                    navController = navController,
//                    viewModel = sharedViewModel,
//                    onNavigateToBiodata = {
//                        navController.navigate(Route.BiodataScreen.route)
//                    }
                )
            }

            composable(
                route = Route.BiodataScreen.route
            ) {
                BiodataScreen(
//                    viewModel = sharedViewModel,
//                    onSubmitRegistration = {
//                        // Handle submission and navigation to next screen
//                    }
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
                LoginScreen()
            }
        }
    }
}