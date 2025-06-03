package com.itb.diabetify.presentation.home.risk_factor_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.home.HomeViewModel
import com.itb.diabetify.presentation.home.components.PieChart
import com.itb.diabetify.presentation.home.risk_factor_detail.components.RiskFactorCard
import com.itb.diabetify.ui.theme.poppinsFontFamily
import kotlin.math.abs

@Composable
fun RiskFactorDetailScreen(
    navController: NavController,
    viewModel: HomeViewModel,
) {
    val scrollState = rememberScrollState()

    val sortedRiskFactorDetails = viewModel.riskFactorDetails.value
        .sortedByDescending { abs(it.impactPercentage) }

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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            if (viewModel.latestPredictionState.value.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp),
                        color = colorResource(id = R.color.primary)
                    )
                }
            } else {
                SummarySection(viewModel.riskFactors.value)

                Spacer(modifier = Modifier.height(10.dp))

                sortedRiskFactorDetails.forEach { factor ->
                    RiskFactorCard(
                        riskFactor = factor,
                        riskFactors = viewModel.riskFactors.value
                    )
                }
            }
        }
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
            // Risk Pie Chart
            PieChart(
                riskFactors = riskFactors,
                centerText = "Faktor\nRisiko",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Penjelasan:",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary)
            )


            Text(
                text = "🟢 Hijau = Menurunkan risiko (faktor pelindung)\n🔴 Merah = Meningkatkan risiko (faktor berbahaya)",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = colorResource(id = R.color.primary)
            )
        }
    }
}