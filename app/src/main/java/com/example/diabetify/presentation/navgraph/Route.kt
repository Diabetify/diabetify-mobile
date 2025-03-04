package com.example.diabetify.presentation.navgraph

sealed class Route(
    val route: String
) {
    object OnBoardingScreen : Route(route = "onBoardingScreen")
    object RegisterScreen : Route(route = "registerScreen")
    object LoginScreen : Route(route = "loginScreen")
    object AppStartNavigation : Route(route = "appStartNavigation")
    object RegisterNavigation : Route(route = "registerNavigation")
    object LoginNavigation : Route(route = "loginNavigation")
}