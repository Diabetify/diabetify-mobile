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
import com.itb.diabetify.presentation.home.risk_detail.components.RiskCategory
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun WhatIfResultScreen(
    navController: NavController,
    viewModel: WhatIfViewModel
) {
    val scrollState = rememberScrollState()
    val state = viewModel.state.value
    
    val predictionPercentage = ((state.predictionResult ?: 0f) * 100).toInt()

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
                title = "Persentase Risiko Simulasi",
                hasWarning = predictionPercentage > 30,
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
                            predictionPercentage <= 30 -> Color(0xFF8BC34A) // Green
                            predictionPercentage <= 50 -> Color(0xFFFFC107) // Yellow  
                            predictionPercentage <= 65 -> Color(0xFFFA821F) // Orange
                            else -> Color(0xFFF44336) // Red
                        },
                        description = when {
                            predictionPercentage <= 30 -> "Diperkirakan 14 dari 100 orang dengan skor ini akan mengidap Diabetes"
                            predictionPercentage <= 50 -> "Diperkirakan 26 dari 100 orang dengan skor ini akan mengidap Diabetes"
                            predictionPercentage <= 65 -> "Diperkirakan 43 dari 100 orang dengan skor ini akan mengidap Diabetes"
                            else -> "Diperkirakan 63 dari 100 orang dengan skor ini akan mengidap Diabetes"
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
                    val riskFactors = getRiskFactorsFromState(state)
                    
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
                        enabled = !state.isLoading,
                        isLoading = state.isLoading,
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

data class RiskFactor(
    val name: String,
    val abbreviation: String,
    val percentage: Float
)

private fun getRiskFactorsFromState(state: WhatIfState): List<RiskFactor> {
    val factors = mutableListOf<RiskFactor>()
    
    // Age factor
    if (state.age > 45) {
        factors.add(RiskFactor("Usia", "U", 15f))
    }
    
    // Weight/BMI factor
    val weightValue = state.weight.toFloatOrNull() ?: 0f
    if (weightValue > 25) {
        factors.add(RiskFactor("Berat Badan", "BB", if (weightValue > 30) 25f else 15f))
    }
    
    // Smoking factor
    when (state.smokingStatus) {
        1 -> factors.add(RiskFactor("Bekas Perokok", "BP", 8f))
        2 -> factors.add(RiskFactor("Perokok Aktif", "PA", 18f))
    }
    
    // Hypertension factor
    if (state.isHypertension) {
        factors.add(RiskFactor("Hipertensi", "H", 12f))
    }
    
    // Cholesterol factor
    if (state.isCholesterol) {
        factors.add(RiskFactor("Kolesterol", "K", 10f))
    }
    
    // Physical activity factor (protective)
    val physicalActivityValue = state.physicalActivityFrequency.toFloatOrNull() ?: 0f
    if (physicalActivityValue >= 3) {
        factors.add(RiskFactor("Aktivitas Fisik", "AF", -8f))
    }
    
    // Bloodline factor
    if (state.isBloodline) {
        factors.add(RiskFactor("Riwayat Keluarga", "RK", 14f))
    }
    
    // Macrosomic baby factor
    if (state.isMacrosomicBaby == 1) {
        factors.add(RiskFactor("Bayi Makrosomik", "BM", 10f))
    }
    
    return factors
}

private fun getSimulationSummary(state: WhatIfState): String {
    val riskLevel = when {
        ((state.predictionResult ?: 0f) * 100).toInt() <= 30 -> "rendah"
        ((state.predictionResult ?: 0f) * 100).toInt() <= 50 -> "sedang"
        ((state.predictionResult ?: 0f) * 100).toInt() <= 65 -> "tinggi"
        else -> "sangat tinggi"
    }
    
    val suggestions = mutableListOf<String>()
    
    if (state.smokingStatus == 2) {
        suggestions.add("berhenti merokok")
    }
    
    val physicalActivityValue = state.physicalActivityFrequency.toFloatOrNull() ?: 0f
    if (physicalActivityValue < 3) {
        suggestions.add("meningkatkan aktivitas fisik")
    }
    
    val weightValue = state.weight.toFloatOrNull() ?: 0f
    if (weightValue > 25) {
        suggestions.add("menurunkan berat badan")
    }
    
    if (state.isHypertension) {
        suggestions.add("mengontrol tekanan darah")
    }
    
    if (state.isCholesterol) {
        suggestions.add("mengontrol kolesterol")
    }
    
    val baseText = "Berdasarkan simulasi, risiko diabetes Anda tergolong $riskLevel dengan persentase ${((state.predictionResult ?: 0f) * 100).toInt()}%."
    
    return if (suggestions.isNotEmpty()) {
        val suggestionText = suggestions.joinToString(", ")
        "$baseText\n\nUntuk menurunkan risiko, disarankan untuk $suggestionText."
    } else {
        "$baseText\n\nPertahankan gaya hidup sehat Anda saat ini!"
    }
} 