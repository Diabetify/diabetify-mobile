package com.itb.diabetify.presentation.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.itb.diabetify.presentation.common.PrimaryButton

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    onLogout: () -> Unit
) {
    val navigationEvent = viewModel.navigationEvent.value
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let {
            when (it) {
                "LOGIN_SCREEN" -> {
                    onLogout()
                    viewModel.onNavigationHandled()
                }
            }
        }
    }

    val context = LocalContext.current
    val errorMessage = viewModel.errorMessage.value
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.onErrorShown()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Home Screen")

        PrimaryButton(
            text = "Logout",
            onClick = {
                viewModel.logout()
            },
        )
    }
}