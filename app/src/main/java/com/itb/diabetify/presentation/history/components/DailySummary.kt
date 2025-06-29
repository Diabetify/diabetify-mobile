package com.itb.diabetify.presentation.history.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.ui.theme.poppinsFontFamily
import java.time.LocalDate
import kotlin.math.abs

data class DailySummaryData(
    val date: LocalDate,
    val riskPercentage: Float,
    val riskFactorContributions: List<RiskFactorContribution>,
    val dailyInputs: List<DailyInput>
)

data class RiskFactorContribution(
    val name: String,
    val percentage: String,
    val isPositive: Boolean
)

data class DailyInput(
    val name: String,
    val value: String,
)

@SuppressLint("NewApi")
@Composable
fun DailySummary(
    summaryData: DailySummaryData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Risk Percentage Card
        RiskPercentageCard(
            riskPercentage = summaryData.riskPercentage,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Risk Factor Contributions Card
        RiskFactorContributionsCard(
            contributions = summaryData.riskFactorContributions,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Daily Inputs Card
        DailyInputsCard(
            inputs = summaryData.dailyInputs
        )
    }
}

@Composable
private fun RiskPercentageCard(
    riskPercentage: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            riskPercentage <= 35 -> Color(0xFF8BC34A).copy(alpha = 0.1f)
                            riskPercentage <= 55 -> Color(0xFFFFC107).copy(alpha = 0.1f)
                            riskPercentage <= 70 -> Color(0xFFFA821F).copy(alpha = 0.1f)
                            else -> Color(0xFFF44336).copy(alpha = 0.1f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Risk Assessment",
                    tint = when {
                        riskPercentage <= 35 -> Color(0xFF8BC34A)
                        riskPercentage <= 55 -> Color(0xFFFFC107)
                        riskPercentage <= 70 -> Color(0xFFFA821F)
                        else -> Color(0xFFF44336)
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Persentase Risiko",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${String.format("%.1f", riskPercentage)}%",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = when {
                        riskPercentage <= 35f -> Color(0xFF8BC34A) // Low risk - Green
                        riskPercentage <= 55f -> Color(0xFFFFC107) // Medium risk - Yellow
                        riskPercentage <= 70f -> Color(0xFFFA821F) // High risk - Orange
                        else -> Color(0xFFF44336) // Very high risk - Red
                    }
                )
                Text(
                    text = when {
                        riskPercentage <= 35 -> "Risiko Rendah"
                        riskPercentage <= 55 -> "Risiko Sedang"
                        riskPercentage <= 70 -> "Risiko Tinggi"
                        else -> "Risiko Sangat Tinggi"
                    },
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = when {
                        riskPercentage <= 35f -> Color(0xFF8BC34A) // Low risk - Green
                        riskPercentage <= 55f -> Color(0xFFFFC107) // Medium risk - Yellow
                        riskPercentage <= 70f -> Color(0xFFFA821F) // High risk - Orange
                        else -> Color(0xFFF44336) // Very high risk - Red
                    }
                )
            }
        }
    }
}

@Composable
private fun RiskFactorContributionsCard(
    contributions: List<RiskFactorContribution>,
    modifier: Modifier = Modifier
) {
    val sortedContributions = contributions.sortedByDescending { contribution ->
        abs(contribution.percentage.replace("%", "").replace("+", "").replace("-", "").toFloatOrNull() ?: 0f)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.primary).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Risk Factors",
                        tint = colorResource(id = R.color.primary),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Kontribusi Faktor Risiko",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.primary)
                )
            }

            sortedContributions.forEach { contribution ->
                RiskFactorItem(contribution = contribution)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun RiskFactorItem(
    contribution: RiskFactorContribution
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = contribution.name,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${if (contribution.isPositive) "+" else "-"}${contribution.percentage}%",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (contribution.isPositive) Color(0xFFF44336) else Color(0xFF4CAF50)
        )
    }
}

@Composable
private fun DailyInputsCard(
    inputs: List<DailyInput>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(colorResource(id = R.color.primary).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Daily Inputs",
                        tint = colorResource(id = R.color.primary),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Data Prediksi",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.primary)
                )
            }

            inputs.forEach { input ->
                DailyInputItem(input = input)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun DailyInputItem(
    input: DailyInput
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = input.name,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color.Black
            )
        }

        Text(
            text = input.value,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = colorResource(R.color.primary)
        )
    }
}