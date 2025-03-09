package com.itb.diabetify.presentation.navgraph

sealed class Route(
    val route: String
) {
    object OnBoardingScreen : Route(route = "onBoardingScreen")
    object RegisterScreen : Route(route = "registerScreen")
    object BiodataScreen : Route(route = "biodataScreen")
    object OtpScreen : Route(route = "otpScreen")
    object SuccessScreen : Route(route = "successScreen")
    object LoginScreen : Route(route = "loginScreen")
    object ForgotPasswordScreen : Route(route = "forgotPasswordScreen")
    object ChangePasswordScreen : Route(route = "changePasswordScreen")
    object AppStartNavigation : Route(route = "appStartNavigation")
    object RegisterNavigation : Route(route = "registerNavigation")
    object LoginNavigation : Route(route = "loginNavigation")
    object ForgotPasswordNavigation : Route(route = "forgotPasswordNavigation")
}