package com.itb.diabetify.presentation.home.risk_factor_detail

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.ErrorNotification
import com.itb.diabetify.presentation.home.HomeViewModel
import com.itb.diabetify.presentation.home.components.BarChart
import com.itb.diabetify.presentation.home.components.BarChartEntry
import com.itb.diabetify.presentation.home.components.PieChart
import com.itb.diabetify.presentation.home.risk_factor_detail.components.RiskFactorCard
import com.itb.diabetify.ui.theme.poppinsFontFamily
import kotlin.math.abs

@Composable
fun RiskFactorDetailScreen(
    navController: NavController,
    viewModel: HomeViewModel,
) {
    val errorMessage = viewModel.errorMessage.value
    val isLoading = viewModel.latestPredictionState.value.isLoading || viewModel.explainPredictionState.value.isLoading
    val scrollState = rememberScrollState()
    val sortedRiskFactorDetails = viewModel.riskFactorDetails.value
        .sortedByDescending { abs(it.impactPercentage) }

    LaunchedEffect(Unit) {
        viewModel.loadExplanationData()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
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
                    text = "Perhitungan faktor risiko",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.primary)
                )
            }

            if (!isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp)
                ) {
                    SummarySection(viewModel.riskFactors.value)

                    Spacer(modifier = Modifier.height(10.dp))

                    val predictionSummary by viewModel.predictionSummary
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Ringkasan Prediksi",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.primary)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = predictionSummary,
                                fontFamily = poppinsFontFamily,
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                    }

                    sortedRiskFactorDetails.forEach { factor ->
                        RiskFactorCard(
                            riskFactor = factor,
                            riskFactors = viewModel.riskFactors.value
                        )
                    }
                }
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = colorResource(id = R.color.primary)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (viewModel.explainPredictionState.value.isLoading) {
                            "Memuat penjelasan..."
                        } else {
                            "Memuat data..."
                        },
                        fontFamily = poppinsFontFamily,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.primary),
                        textAlign = TextAlign.Center
                    )
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
    }
}

@Composable
fun SummarySection(riskFactors: List<HomeViewModel.RiskFactor>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
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
}