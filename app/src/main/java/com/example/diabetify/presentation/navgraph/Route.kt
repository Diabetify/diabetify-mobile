package com.example.diabetify.presentation.navgraph

sealed class Route(
    val route: String
) {
    object OnBoardingScreen : Route(route = "onBoardingScreen")
    object LoginScreen : Route(route = "loginScreen")
    object AppStartNavigation : Route(route = "appStartNavigation")
    object LoginNavigation : Route(route = "loginNavigation")
}