package com.itb.diabetify.presentation.navgraph

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.itb.diabetify.presentation.forgot_password.ChangePasswordScreen
import com.itb.diabetify.presentation.forgot_password.ForgotPasswordScreen
import com.itb.diabetify.presentation.forgot_password.ForgotPasswordViewModel
import com.itb.diabetify.presentation.login.LoginScreen
import com.itb.diabetify.presentation.login.LoginViewModel
import com.itb.diabetify.presentation.onboarding.OnBoardingScreen
import com.itb.diabetify.presentation.onboarding.OnBoardingViewModel
import com.itb.diabetify.presentation.register.BiodataScreen
import com.itb.diabetify.presentation.register.OtpScreen
import com.itb.diabetify.presentation.register.RegisterScreen
import com.itb.diabetify.presentation.register.RegisterViewModel
import com.itb.diabetify.presentation.register.SuccessScreen
import com.itb.diabetify.presentation.survey.SurveyScreen
import com.itb.diabetify.presentation.survey.SurveyViewModel

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AuthNavGraph(
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
            route = Route.AuthNavigation.route,
            startDestination = Route.LoginScreen.route
        ) {
            // Login Screen
            composable(
                route = Route.LoginScreen.route
            ) {
                val loginViewModel: LoginViewModel = hiltViewModel()

                LoginScreen(
                    navController = navController,
                    viewModel = loginViewModel,
                )
            }

            // Register Flow
            composable(
                route = Route.RegisterScreen.route
            ) {
                val registerViewModel: RegisterViewModel = hiltViewModel(
                    navController.getBackStackEntry(Route.AuthNavigation.route)
                )

                RegisterScreen(
                    navController = navController,
                    viewModel = registerViewModel,
                )
            }

            composable(
                route = Route.BiodataScreen.route
            ) {
                val registerViewModel: RegisterViewModel = hiltViewModel(
                    navController.getBackStackEntry(Route.AuthNavigation.route)
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
                    navController.getBackStackEntry(Route.AuthNavigation.route)
                )

                OtpScreen(
                    navController = navController,
                    viewModel = registerViewModel,
                )
            }

            composable(
                route = Route.RegisterSuccessScreen.route
            ) {
                val registerViewModel: RegisterViewModel = hiltViewModel(
                    navController.getBackStackEntry(Route.AuthNavigation.route)
                )

                SuccessScreen(
                    navController = navController,
                    viewModel = registerViewModel,
                )
            }

            // Forgot Password Flow
            composable(
                route = Route.ForgotPasswordScreen.route
            ) {
                val forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel(
                    navController.getBackStackEntry(Route.AuthNavigation.route)
                )

                ForgotPasswordScreen(
                    navController = navController,
                    viewModel = forgotPasswordViewModel
                )
            }

            composable(
                route = Route.ChangePasswordScreen.route
            ) {
                val forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel(
                    navController.getBackStackEntry(Route.AuthNavigation.route)
                )

                ChangePasswordScreen(
                    navController = navController,
                    viewModel = forgotPasswordViewModel
                )
            }

            composable(
                route = Route.ResetPasswordSuccessScreen.route
            ) {
                com.itb.diabetify.presentation.forgot_password.SuccessScreen(
                    navController = navController
                )
            }

            composable(
                route = Route.SurveyScreen.route
            ) {
                val surveyViewModel: SurveyViewModel = hiltViewModel(
                    navController.getBackStackEntry(Route.AuthNavigation.route)
                )

                SurveyScreen(
                    navController = navController,
                    viewModel = surveyViewModel,
                )
            }

            composable(
                route = Route.SurveySuccessScreen.route
            ) {
                com.itb.diabetify.presentation.survey.SuccessScreen(
                    navController = navController
                )
            }
        }

        navigation(
            route = Route.MainNavigation.route,
            startDestination = Route.MainScreen.route
        ) {
            composable(
                route = Route.MainScreen.route
            ) {
                MainNavGraph(
                    navController = navController
                )
            }
        }
    }
}