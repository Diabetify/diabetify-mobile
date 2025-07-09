package com.itb.diabetify.presentation.whatif

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.home.components.BarChartEntry
import com.itb.diabetify.presentation.home.components.BarChart
import com.itb.diabetify.presentation.home.components.HomeCard
import com.itb.diabetify.presentation.home.components.PieChart
import com.itb.diabetify.presentation.home.components.RiskIndicator
import com.itb.diabetify.presentation.home.components.RiskCategory
import com.itb.diabetify.presentation.home.components.getRiskCategoryColor
import com.itb.diabetify.presentation.home.components.getRiskCategoryDescription
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun WhatIfResultScreen(
    navController: NavController,
    viewModel: WhatIfViewModel
) {
    // States
    val predictionPercentage by viewModel.predictionScore
    val riskFactors by viewModel.riskFactors
    val scrollState = rememberScrollState()

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
                riskPercentage = predictionPercentage
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
                        color = getRiskCategoryColor(predictionPercentage),
                        description = getRiskCategoryDescription(predictionPercentage),
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
                    var selectedTabIndex by remember { mutableIntStateOf(0) }
                    val tabTitles = listOf("Grafik Batang", "Grafik Lingkaran")

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        selectedTabIndex = index
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedTabIndex == index) {
                                        colorResource(id = R.color.primary)
                                    } else {
                                        Color(0xFFF3F4F6)
                                    }
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = title,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.SemiBold else FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = if (selectedTabIndex == index) {
                                        Color.White
                                    } else {
                                        Color(0xFF6B7280)
                                    },
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 4.dp)
                                )
                            }
                        }
                    }

                    // Chart
                    when (selectedTabIndex) {
                        0 -> {
                            BarChart(
                                entries = riskFactors.mapIndexed { _, riskFactor ->
                                    BarChartEntry(
                                        label = riskFactor.name,
                                        abbreviation = riskFactor.abbreviation,
                                        value = riskFactor.percentage,
                                        isNegative = riskFactor.percentage < 0
                                    )
                                }
                            )
                        }
                        1 -> {
                            PieChart(
                                riskFactors = riskFactors,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}