package com.itb.diabetify.presentation.navgraph

sealed class Route(
    val route: String
) {
    object OnBoardingScreen : Route(route = "onBoardingScreen")
    object RegisterScreen : Route(route = "registerScreen")
    object BiodataScreen : Route(route = "biodataScreen")
    object OtpScreen : Route(route = "otpScreen")
    object RegisterSuccessScreen : Route(route = "registerSuccessScreen")
    object LoginScreen : Route(route = "loginScreen")
    object SurveyScreen : Route(route = "surveyScreen")
    object ForgotPasswordScreen : Route(route = "forgotPasswordScreen")
    object ChangePasswordScreen : Route(route = "changePasswordScreen")
    object ResetPasswordSuccessScreen : Route(route = "resetPasswordSuccessScreen")
    object MainScreen : Route(route = "mainScreen")
    object HomeScreen: Route(route = "homeScreen")
    object HistoryScreen: Route(route = "historyScreen")
    object RecommendationScreen: Route(route = "recommendationScreen")
    object SettingsScreen: Route(route = "settingsScreen")
    object AppStartNavigation : Route(route = "appStartNavigation")
    object AuthNavigation : Route(route = "authNavigation")
    object MainNavigation : Route(route = "mainNavigation")
}