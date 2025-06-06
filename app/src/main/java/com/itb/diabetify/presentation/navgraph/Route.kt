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
    object SurveySuccessScreen : Route(route = "surveySuccessScreen")
    object ForgotPasswordScreen : Route(route = "forgotPasswordScreen")
    object ChangePasswordScreen : Route(route = "changePasswordScreen")
    object ResetPasswordSuccessScreen : Route(route = "resetPasswordSuccessScreen")
    object MainScreen : Route(route = "mainScreen")
    object HomeScreen: Route(route = "homeScreen")
    object RiskDetailScreen: Route(route = "riskDetailScreen")
    object RiskFactorDetailScreen: Route(route = "riskFactorDetailScreen")
    object HistoryScreen: Route(route = "historyScreen")
    object GuideScreen: Route(route = "guideScreen")
    object GuideDetailScreen: Route(route = "guideDetail/{guideId}") {
        fun createRoute(guideId: String) = "guideDetail/$guideId"
    }
    object SettingsScreen: Route(route = "settingsScreen")
    object EditProfileScreen: Route(route = "editProfileScreen")
    object EditSurveyScreen: Route(route = "editSurveyScreen")
    object AppStartNavigation : Route(route = "appStartNavigation")
    object AuthNavigation : Route(route = "authNavigation")
    object MainNavigation : Route(route = "mainNavigation")
}