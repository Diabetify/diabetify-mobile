package com.itb.diabetify.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.ErrorNotification
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.presentation.settings.components.ConfirmationDialog
import com.itb.diabetify.presentation.settings.components.NotificationCard
import com.itb.diabetify.presentation.settings.components.NotificationItem
import com.itb.diabetify.presentation.settings.components.ProfileCard
import com.itb.diabetify.presentation.settings.components.SettingsCard
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
    onLogout: () -> Unit
) {
    // Get context
    val context = LocalContext.current
    
    // States
    val errorMessage = viewModel.errorMessage.value
    val isLoading = viewModel.logoutState.value.isLoading

    // Navigation Event
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

    // Logout Dialog
    var showLogoutDialog by remember { mutableStateOf(false) }
    if (showLogoutDialog) {
        ConfirmationDialog(
            onConfirm = {
                viewModel.logout()
                showLogoutDialog = false
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                colorResource(id = R.color.primary),
                                colorResource(id = R.color.primary).copy(alpha = 0.8f)
                            )
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Column {
                    Text(
                        text = "Profil",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {

                // User profile card
                ProfileCard(
                    name = viewModel.nameState.value.text,
                    email = viewModel.emailState.value.text,
                    onEditClick = {
                        navController.navigate(Route.EditProfileScreen.route) {
                            launchSingleTop = true
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                var dailyReminderEnabled by remember { mutableStateOf(true) }
                NotificationCard(
                    title = "Pengaturan Notifikasi",
                    notificationItems = listOf(
                        NotificationItem(
                            icon = R.drawable.ic_notification,
                            title = "Pengingat Harian",
                            isEnabled = dailyReminderEnabled,
                            onToggle = { dailyReminderEnabled = it }
                        )
                    )
                )

                // Cards section
                getSettingsCards(context).forEach { cardData ->
                    SettingsCard(cardData = cardData)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Logout button
                PrimaryButton(
                    text = "Logout",
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !isLoading,
                    isLoading = isLoading
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        // Error notification
        ErrorNotification(
            showError = errorMessage != null,
            errorMessage = errorMessage,
            onDismiss = { viewModel.onErrorShown() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1000f)
        )
    }
}