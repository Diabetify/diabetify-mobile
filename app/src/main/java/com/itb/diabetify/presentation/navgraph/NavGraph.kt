package com.itb.diabetify.presentation.navgraph

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.itb.diabetify.presentation.login.ChangePasswordScreen
import com.itb.diabetify.presentation.login.ForgotPasswordScreen
import com.itb.diabetify.presentation.login.LoginScreen
import com.itb.diabetify.presentation.onboarding.OnBoardingScreen
import com.itb.diabetify.presentation.onboarding.OnBoardingViewModel
import com.itb.diabetify.presentation.register.BiodataScreen
import com.itb.diabetify.presentation.register.OtpScreen
import com.itb.diabetify.presentation.register.RegisterScreen
import com.itb.diabetify.presentation.register.RegisterViewModel
import com.itb.diabetify.presentation.register.SuccessScreen

@SuppressLint("UnrememberedGetBackStackEntry")
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
            composable(
                route = Route.RegisterScreen.route
            ) {
                val registerViewModel: RegisterViewModel = hiltViewModel(
                    navController.getBackStackEntry(Route.RegisterNavigation.route)
                )

                RegisterScreen(
                    navController = navController,
                    viewModel = registerViewModel,
                )
            }

            composable(
                route = Route.LoginScreen.route
            ) {
                LoginScreen(
                    navController = navController
                )
            }

            composable(
                route = Route.BiodataScreen.route
            ) {
                val registerViewModel: RegisterViewModel = hiltViewModel(
                    navController.getBackStackEntry(Route.RegisterNavigation.route)
                )

                BiodataScreen(
                    navController = navController,
                    viewModel = registerViewModel,
                )
            }

            composable(
                route = Route.OtpScreen.route
            ) {
                val registerViewModel: RegisterViewModel = hiltViewModel(
                    navController.getBackStackEntry(Route.RegisterNavigation.route)
                )

                OtpScreen(
                    navController = navController,
                    viewModel = registerViewModel,
                )
            }

            composable(
                route = Route.SuccessScreen.route
            ) {
                SuccessScreen()
            }
        }

        navigation(
            route = Route.LoginNavigation.route,
            startDestination = Route.LoginScreen.route
        ) {
            composable(
                route = Route.RegisterScreen.route
            ) {
                val registerViewModel: RegisterViewModel = hiltViewModel(
                    navController.getBackStackEntry(Route.LoginNavigation.route)
                )

                RegisterScreen(
                    navController = navController,
                    viewModel = registerViewModel,
                )
            }

            composable (
                route = Route.ForgotPasswordScreen.route
            ) {
                ForgotPasswordScreen(
                    navController = navController
                )
            }

            composable (
                route = Route.ChangePasswordScreen.route
            ) {
                ChangePasswordScreen(
                    navController = navController
                )
            }
        }
    }
}