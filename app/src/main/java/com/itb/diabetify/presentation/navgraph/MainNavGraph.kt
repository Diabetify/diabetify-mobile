package com.itb.diabetify.presentation.navgraph

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.history.HistoryScreen
import com.itb.diabetify.presentation.home.HomeScreen
import com.itb.diabetify.presentation.home.HomeViewModel
import com.itb.diabetify.presentation.navbar.BottomNavigationBar
import com.itb.diabetify.presentation.navbar.NavigationViewModel
import com.itb.diabetify.presentation.recommendation.RecommendationScreen
import com.itb.diabetify.presentation.settings.SettingsScreen
import com.itb.diabetify.presentation.settings.SettingsViewModel

@Composable
fun MainNavGraph(
    navController: NavController,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navigationViewModel: NavigationViewModel = hiltViewModel()
    val mainNavController = rememberNavController()

    var shouldNavigateToLogin by rememberSaveable { mutableStateOf(false) }
    val navigationHistory = remember { mutableListOf<String>() }
    val currentRoute = mainNavController.currentBackStackEntryAsState().value?.destination?.route

    val systemUiController = rememberSystemUiController()
    val primaryColor = colorResource(id = R.color.primary)

    SideEffect {
        systemUiController.setStatusBarColor(
            color = primaryColor,
            darkIcons = false
        )
    }

    LaunchedEffect(currentRoute) {
        currentRoute?.let { route ->
            if (navigationViewModel.navigationItems.any { it.route == route }) {
                if (navigationHistory.isEmpty() || navigationHistory.last() != route) {
                    navigationHistory.add(route)
                }
                navigationViewModel.onNavigationItemSelected(route)
            }
        }
    }

    val homeRoute = Route.HomeScreen.route
    BackHandler {
        if (currentRoute != homeRoute) {
            navigationViewModel.onNavigationItemSelected(homeRoute)

            mainNavController.navigate(homeRoute) {
                popUpTo(mainNavController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    if (shouldNavigateToLogin) {
        shouldNavigateToLogin = false
        navController.navigate(Route.LoginScreen.route) {
            popUpTo(Route.MainNavigation.route) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier.fillMaxWidth(),
                viewModel = navigationViewModel,
                onItemSelected = { route ->
                    mainNavController.navigate(route) {
                        popUpTo(mainNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Route.HomeScreen.route) {
                val homeViewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    navController = mainNavController,
                    viewModel = homeViewModel,
                )
            }

            composable(route = Route.HistoryScreen.route) {
                HistoryScreen()
            }

            composable(route = Route.RecommendationScreen.route) {
                RecommendationScreen()
            }

            composable(route = Route.SettingsScreen.route) {
                val settingsViewModel: SettingsViewModel = hiltViewModel()
                SettingsScreen(
                    navController = mainNavController,
                    viewModel = settingsViewModel,
                    onLogout = {
                        shouldNavigateToLogin = true
                    }
                )
            }
        }
    }
}