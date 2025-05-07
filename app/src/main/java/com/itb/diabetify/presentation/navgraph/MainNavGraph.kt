package com.itb.diabetify.presentation.navgraph

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itb.diabetify.presentation.history.HistoryScreen
import com.itb.diabetify.presentation.home.HomeScreen
import com.itb.diabetify.presentation.home.HomeViewModel
import com.itb.diabetify.presentation.navbar.BottomNavigationBar
import com.itb.diabetify.presentation.navbar.NavigationViewModel
import com.itb.diabetify.presentation.recommendation.RecommendationScreen
import com.itb.diabetify.presentation.settings.SettingsScreen
import com.itb.diabetify.presentation.settings.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun MainNavGraph(
    navController: NavController,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val navigationViewModel: NavigationViewModel = hiltViewModel()
    val mainNavController = rememberNavController()

    var shouldNavigateToLogin by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = true){}

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
                },
                onAddButtonClicked = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Add action selected")
                    }
                },
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