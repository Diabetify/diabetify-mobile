package com.itb.diabetify.presentation.risk_detail

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
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun RiskDetailScreen(
    navController: NavController,
    viewModel: HomeViewModel,
) {
    val riskScore = 50
    val scrollState = rememberScrollState()

    val lowRiskColor = Color(0xFF8BC34A)    // Green
    val mediumRiskColor = Color(0xFFFFC107) // Yellow
    val highRiskColor = Color(0xFFFA821F)   // Orange
    val veryHighRiskColor = Color(0xFFF44336) // Red

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
                lowRiskColor = lowRiskColor,
                mediumRiskColor = mediumRiskColor,
                highRiskColor = highRiskColor,
                veryHighRiskColor = veryHighRiskColor
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
                color = lowRiskColor,
                title = "0 - 0.3: Rendah",
                description = "Diperkirakan 14 dari 100 orang dengan skor ini akan mengidap Diabetes"
            )

            RiskCategory(
                color = mediumRiskColor,
                title = "0.3 - 0.5: Sedang",
                description = "Diperkirakan 26 dari 100 orang dengan skor ini akan mengidap Diabetes"
            )

            RiskCategory(
                color = highRiskColor,
                title = "0.5 - 0.65: Tinggi",
                description = "Diperkirakan 43 dari 100 orang dengan skor ini akan mengidap Diabetes"
            )

            RiskCategory(
                color = veryHighRiskColor,
                title = "0.65 - 1: Sangat Tinggi",
                description = "Diperkirakan 63 dari 100 orang dengan skor ini akan mengidap Diabetes"
            )
        }
    }
}

@Composable
fun RiskScoreGauge(
    score: Int,
    lowRiskColor: Color,
    mediumRiskColor: Color,
    highRiskColor: Color,
    veryHighRiskColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0.0f to lowRiskColor,
                            0.3f to mediumRiskColor,
                            0.5f to highRiskColor,
                            0.65f to veryHighRiskColor,
                            1.0f to veryHighRiskColor
                        )
                    )
                )
        )

        val normalizedScore = score.coerceIn(0, 100) / 100f

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (normalizedScore > 0) {
                Spacer(
                    modifier = Modifier
                        .weight(normalizedScore)
                        .height(1.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = colorResource(R.color.primary), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = score.toString(),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.white)
                )
            }

            if (normalizedScore < 1) {
                Spacer(
                    modifier = Modifier
                        .weight(1f - normalizedScore)
                        .height(1.dp)
                )
            }
        }
    }
}

@Composable
fun RiskCategory(color: Color, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color, RoundedCornerShape(4.dp))
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
        ) {
            Text(
                text = title,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorResource(id = R.color.primary)
            )

            Text(
                text = description,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = colorResource(id = R.color.black)
            )
        }
    }
}