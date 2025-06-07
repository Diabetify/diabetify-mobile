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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.itb.diabetify.R
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun AddActionPopup(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    viewModel: AddActivityViewModel
) {
    val smokeValueState = viewModel.smokeValueState.value
    val workoutValueState = viewModel.workoutValueState.value
    val weightValueState = viewModel.weightValueState.value
    val heightValueState = viewModel.heightValueState.value
    val birthValueState = viewModel.birthValueState.value
    val hypertensionValueState = viewModel.hypertensionValueState.value

    val currentValues = mapOf(
        "weight" to weightValueState.text,
        "height" to heightValueState.text,
        "cigarette" to smokeValueState.text,
        "activity" to workoutValueState.text,
        "birth" to birthValueState.text,
        "hypertension" to hypertensionValueState.text
    )

    var showBottomSheet by remember { mutableStateOf(false) }
    var currentQuestionType by remember { mutableStateOf("weight") }

    if (showBottomSheet) {
        val isNumericQuestion = listOf("weight", "height", "cigarette", "activity").contains(currentQuestionType)

        BottomSheet(
            isVisible = showBottomSheet,
            onDismissRequest = { showBottomSheet = false },
            questionType = currentQuestionType,
            currentNumericValue = if (isNumericQuestion) currentValues[currentQuestionType] else null,
            currentSelectionValue = if (!isNumericQuestion) currentValues[currentQuestionType] else null,
            viewModel = viewModel
        )
    }

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
                // Menu content
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = Color.Transparent
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        // Monthly tracking options
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
                                    currentQuestionType = "weight"
                                    showBottomSheet = true
                                }
                            )

                            AnimatedTrackingButton(
                                icon = R.drawable.ic_height,
                                label = "Tinggi",
                                delayMillis = 200,
                                onClick = {
                                    currentQuestionType = "height"
                                    showBottomSheet = true
                                }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            AnimatedTrackingButton(
                                icon = R.drawable.ic_baby,
                                label = "Kehamilan",
                                delayMillis = 250,
                                onClick = {
                                    currentQuestionType = "birth"
                                    showBottomSheet = true
                                }
                            )

                            AnimatedTrackingButton(
                                icon = R.drawable.ic_hypertension,
                                label = "Hipertensi",
                                delayMillis = 300,
                                onClick = {
                                    currentQuestionType = "hypertension"
                                    showBottomSheet = true
                                }
                            )
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
                                    currentQuestionType = "cigarette"
                                    showBottomSheet = true
                                }
                            )

                            AnimatedTrackingButton(
                                icon = R.drawable.ic_walk,
                                label = "Aktivitas",
                                delayMillis = 100,
                                onClick = {
                                    currentQuestionType = "activity"
                                    showBottomSheet = true
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(8.dp))

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