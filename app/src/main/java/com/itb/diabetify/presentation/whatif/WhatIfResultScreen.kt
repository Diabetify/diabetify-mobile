package com.itb.diabetify.presentation.whatif

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.CustomizableButton
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.home.components.BarChartEntry
import com.itb.diabetify.presentation.home.components.BarChartV2
import com.itb.diabetify.presentation.home.components.HomeCard
import com.itb.diabetify.presentation.home.components.RiskIndicator
import com.itb.diabetify.presentation.home.components.RiskCategory
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun WhatIfResultScreen(
    navController: NavController,
    viewModel: WhatIfViewModel
) {
    val scrollState = rememberScrollState()
    val predictionPercentage by viewModel.predictionScore
    val riskFactors by viewModel.riskFactors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = colorResource(id = R.color.primary)
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Hasil Simulasi",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.primary)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            // Risk Percentage Card
            HomeCard(
                title = "Persentase Risiko",
                hasWarning = true,
                riskPercentage = predictionPercentage.toFloat()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RiskIndicator(
                        percentage = predictionPercentage,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RiskCategory(
                        modifier = Modifier.padding(vertical = 5.dp),
                        color = when {
                            predictionPercentage <= 35 -> Color(0xFF8BC34A)
                            predictionPercentage <= 55 -> Color(0xFFFFC107)
                            predictionPercentage <= 70 -> Color(0xFFFA821F)
                            else -> Color(0xFFF44336)
                        },
                        description = when {
                            predictionPercentage <= 35 -> "Diperkirakan 15 dari 100 orang dengan skor ini akan mengidap Diabetes"
                            predictionPercentage <= 55 -> "Diperkirakan 31 dari 100 orang dengan skor ini akan mengidap Diabetes"
                            predictionPercentage <= 70 -> "Diperkirakan 55 dari 100 orang dengan skor ini akan mengidap Diabetes"
                            else -> "Diperkirakan 69 dari 100 orang dengan skor ini akan mengidap Diabetes"
                        },
                        isHighlighted = true
                    )
                }
            }

            // Risk Factor Contribution Card
            HomeCard(
                title = "Kontribusi Faktor Risiko"
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BarChartV2(
                        entries = riskFactors.map { riskFactor ->
                            BarChartEntry(
                                label = riskFactor.name,
                                abbreviation = riskFactor.abbreviation,
                                value = riskFactor.percentage,
                                isNegative = riskFactor.percentage < 0
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CustomizableButton(
                        text = "Kembali",
                        onClick = { navController.popBackStack() },
                        backgroundColor = Color.Gray,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    )

                    PrimaryButton(
                        text = "Beranda",
                        onClick = { viewModel.calculateWhatIfPrediction() },
//                        enabled = !state.isLoading,
//                        isLoading = state.isLoading,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}