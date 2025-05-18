package com.itb.diabetify.presentation.home.risk_detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.home.HomeViewModel
import com.itb.diabetify.presentation.home.risk_detail.components.RiskCategory
import com.itb.diabetify.presentation.home.risk_detail.components.RiskScoreGauge
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun RiskDetailScreen(
    navController: NavController,
    viewModel: HomeViewModel,
) {
    val riskScore = 50
    val scrollState = rememberScrollState()

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
                text = "Perhitungan skor risiko",
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
            RiskScoreGauge(
                score = riskScore,
                lowRiskColor = viewModel.lowRiskColor,
                mediumRiskColor = viewModel.mediumRiskColor,
                highRiskColor = viewModel.highRiskColor,
                veryHighRiskColor = viewModel.veryHighRiskColor
            )

            Text(
                text = "Gunakan perhitungan ini sebagai acuanmu untuk mengurangi kemungkinan Diabetes",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = colorResource(id = R.color.primary),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Risk categories
            RiskCategory(
                color = viewModel.lowRiskColor,
                title = "0 - 0.3: Rendah",
                description = "Diperkirakan 14 dari 100 orang dengan skor ini akan mengidap Diabetes"
            )

            RiskCategory(
                color = viewModel.mediumRiskColor,
                title = "0.3 - 0.5: Sedang",
                description = "Diperkirakan 26 dari 100 orang dengan skor ini akan mengidap Diabetes"
            )

            RiskCategory(
                color = viewModel.highRiskColor,
                title = "0.5 - 0.65: Tinggi",
                description = "Diperkirakan 43 dari 100 orang dengan skor ini akan mengidap Diabetes"
            )

            RiskCategory(
                color = viewModel.veryHighRiskColor,
                title = "0.65 - 1: Sangat Tinggi",
                description = "Diperkirakan 63 dari 100 orang dengan skor ini akan mengidap Diabetes"
            )
        }
    }
}