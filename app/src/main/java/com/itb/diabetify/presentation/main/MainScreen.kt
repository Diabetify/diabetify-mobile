package com.itb.diabetify.presentation.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.itb.diabetify.presentation.home.HomeScreen
import com.itb.diabetify.presentation.home.HomeViewModel
import com.itb.diabetify.presentation.login.LoginScreen
import com.itb.diabetify.presentation.login.LoginViewModel
import com.itb.diabetify.presentation.navbar.BottomNavigationBar
import com.itb.diabetify.presentation.navgraph.Route
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    homeViewModel: HomeViewModel
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier.fillMaxWidth(),
                onItemSelected = { route ->
                    navController.navigate(route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                onAddButtonClicked = {
                    // Handle the add button click
                    scope.launch {
                        snackbarHostState.showSnackbar("Add action selected")
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.MainNavigation.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation(
                route = Route.MainNavigation.route,
                startDestination = Route.HomeScreen.route
            ) {
                composable(
                    route = Route.HomeScreen.route
                ) {
                    HomeScreen(
                        navController = navController,
                        viewModel = homeViewModel
                    )
                }

                composable(
                    route = Route.LoginScreen.route
                ) {
                    val loginViewModel: LoginViewModel = hiltViewModel(
                    )

                    LoginScreen(
                        navController = navController,
                        viewModel = loginViewModel
                    )
                }
            }
        }
    }
}