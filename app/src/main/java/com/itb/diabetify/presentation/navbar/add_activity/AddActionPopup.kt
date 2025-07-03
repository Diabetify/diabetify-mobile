package com.itb.diabetify.presentation.navbar.add_activity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.ErrorNotification
import com.itb.diabetify.presentation.common.SuccessNotification
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun AddActionPopup(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    viewModel: AddActivityViewModel
) {
    // States
    val smokeFieldState by viewModel.smokeFieldState
    val workoutFieldState by viewModel.workoutFieldState
    val weightFieldState by viewModel.weightFieldState
    val heightFieldState by viewModel.heightFieldState
    val birthFieldState by viewModel.birthFieldState
    val hypertensionFieldState by viewModel.hypertensionFieldState
    val cholesterolFieldState by viewModel.cholesterolFieldState
    val bloodlineFieldState by viewModel.bloodlineFieldState
    val userGender by viewModel.userGender
    val isFemale = userGender?.lowercase() == "perempuan" || userGender?.lowercase() == "female"
    val currentValues = mapOf(
        "weight" to weightFieldState.text,
        "height" to heightFieldState.text,
        "cigarette" to smokeFieldState.text,
        "activity" to workoutFieldState.text,
        "birth" to birthFieldState.text,
        "hypertension" to hypertensionFieldState.text,
        "cholesterol" to cholesterolFieldState.text,
        "bloodline" to bloodlineFieldState.text
    )
    val currentQuestionType by viewModel.currentQuestionType
    val showBottomSheet by viewModel.showBottomSheet
    val errorMessage = viewModel.errorMessage.value
    val successMessage = viewModel.successMessage.value

    // Bottom Sheet
    if (showBottomSheet) {
        val isNumericQuestion = listOf("weight", "height", "cigarette").contains(currentQuestionType)

        BottomSheet(
            isVisible = true,
            onDismissRequest = { viewModel.setShowBottomSheet(false) },
            questionType = currentQuestionType,
            currentNumericValue = if (isNumericQuestion) currentValues[currentQuestionType] else null,
            currentSelectionValue = if (!isNumericQuestion) currentValues[currentQuestionType] else null,
            viewModel = viewModel
        )
    }

    // Popup
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { onDismissRequest() },
                contentAlignment = Alignment.BottomCenter
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.Transparent
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        // Non-daily tracking options
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(300, delayMillis = 120)) +
                                    slideInVertically(
                                        animationSpec = tween(300, delayMillis = 120, easing = FastOutSlowInEasing),
                                        initialOffsetY = { it / 4 }
                                    )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "Laporan Non-Harian",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = colorResource(id = R.color.white)
                                )
                                Text(
                                    modifier = Modifier.padding(horizontal = 30.dp),
                                    text = "Perbarui data hanya jika ada perubahan",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    lineHeight = 16.sp,
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.Center,
                                    color = colorResource(id = R.color.white)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AnimatedTrackingButton(
                                icon = R.drawable.ic_weight,
                                label = "Berat",
                                delayMillis = 150,
                                onClick = {
                                    viewModel.setCurrentQuestionType("weight")
                                    viewModel.setShowBottomSheet(true)
                                }
                            )

                            AnimatedTrackingButton(
                                icon = R.drawable.ic_height,
                                label = "Tinggi",
                                delayMillis = 200,
                                onClick = {
                                    viewModel.setCurrentQuestionType("height")
                                    viewModel.setShowBottomSheet(true)
                                }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AnimatedTrackingButton(
                                icon = R.drawable.ic_hypertension,
                                label = "Hipertensi",
                                delayMillis = 300,
                                onClick = {
                                    viewModel.setCurrentQuestionType("hypertension")
                                    viewModel.setShowBottomSheet(true)
                                }
                            )

                            AnimatedTrackingButton(
                                icon = R.drawable.ic_cholesterol,
                                label = "Kolesterol",
                                delayMillis = 250,
                                onClick = {
                                    viewModel.setCurrentQuestionType("cholesterol")
                                    viewModel.setShowBottomSheet(true)
                                }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            horizontalArrangement = if (isFemale) Arrangement.SpaceEvenly else Arrangement.Center
                        ) {
                            AnimatedTrackingButton(
                                icon = R.drawable.ic_family,
                                label = "Keluarga",
                                delayMillis = 250,
                                onClick = {
                                    viewModel.setCurrentQuestionType("bloodline")
                                    viewModel.setShowBottomSheet(true)
                                }
                            )

                            if (isFemale) {
                                AnimatedTrackingButton(
                                    icon = R.drawable.ic_baby,
                                    label = "Kehamilan",
                                    delayMillis = 300,
                                    onClick = {
                                        viewModel.setCurrentQuestionType("birth")
                                        viewModel.setShowBottomSheet(true)
                                    }
                                )
                            }
                        }

                        // Daily tracking options
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(300)) +
                                    slideInVertically(
                                        animationSpec = tween(300, easing = FastOutSlowInEasing),
                                        initialOffsetY = { it / 4 }
                                    )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "Laporan Harian",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = colorResource(id = R.color.white)
                                )
                                Text(
                                    modifier = Modifier.padding(horizontal = 30.dp),
                                    text = "Perbarui data setiap hari",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    lineHeight = 16.sp,
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.Center,
                                    color = colorResource(id = R.color.white)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AnimatedTrackingButton(
                                icon = R.drawable.ic_smoking,
                                label = "Rokok",
                                delayMillis = 50,
                                onClick = {
                                    viewModel.setCurrentQuestionType("cigarette")
                                    viewModel.setShowBottomSheet(true)
                                }
                            )

                            AnimatedTrackingButton(
                                icon = R.drawable.ic_walk,
                                label = "Aktivitas",
                                delayMillis = 100,
                                onClick = {
                                    viewModel.setCurrentQuestionType("activity")
                                    viewModel.setShowBottomSheet(true)
                                }
                            )
                        }

                        // Close button
                        AnimatedVisibility(
                            visible = true,
                            enter = scaleIn(animationSpec = tween(300, delayMillis = 350)) +
                                    fadeIn(animationSpec = tween(300, delayMillis = 350))
                        ) {
                            FloatingActionButton(
                                onClick = { onDismissRequest() },
                                containerColor = colorResource(id = R.color.primary),
                                contentColor = Color.White,
                                shape = CircleShape,
                                modifier = Modifier.size(56.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close menu",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
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

                // Success notification
                SuccessNotification(
                    showSuccess = successMessage != null,
                    successMessage = successMessage,
                    onDismiss = { viewModel.onSuccessShown() },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(1000f)
                )
            }
        }
    }
}

@Composable
fun AnimatedTrackingButton(
    icon: Int,
    label: String,
    delayMillis: Int = 0,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = true,
        enter = scaleIn(animationSpec = tween(300, delayMillis = delayMillis)) +
                fadeIn(animationSpec = tween(300, delayMillis = delayMillis))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FloatingActionButton(
                onClick = onClick,
                containerColor = colorResource(id = R.color.tertiary),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    modifier = Modifier.size(28.dp),
                    colorFilter = ColorFilter.tint(colorResource(id = R.color.white))
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = R.color.white)
            )
        }
    }
}