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

data class DailySummaryData(
    val date: LocalDate,
    val riskPercentage: Float,
    val riskFactorContributions: List<RiskFactorContribution>,
    val dailyInputs: List<DailyInput>
)

data class RiskFactorContribution(
    val name: String,
    val percentage: Float,
    val isPositive: Boolean
)

data class DailyInput(
    val name: String,
    val value: String,
    val isCompleted: Boolean
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
                            riskPercentage < 30 -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                            riskPercentage < 70 -> Color(0xFFFF9800).copy(alpha = 0.1f)
                            else -> Color(0xFFF44336).copy(alpha = 0.1f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Risk Assessment",
                    tint = when {
                        riskPercentage < 30 -> Color(0xFF4CAF50)
                        riskPercentage < 70 -> Color(0xFFFF9800)
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
                    text = "${riskPercentage.toInt()}%",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = when {
                        riskPercentage < 30 -> Color(0xFF4CAF50)
                        riskPercentage < 70 -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
                    }
                )
                Text(
                    text = when {
                        riskPercentage < 30 -> "Risiko Rendah"
                        riskPercentage < 70 -> "Risiko Sedang"
                        else -> "Risiko Tinggi"
                    },
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = when {
                        riskPercentage < 30 -> Color(0xFF4CAF50)
                        riskPercentage < 70 -> Color(0xFFFF9800)
                        else -> Color(0xFFF44336)
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

            contributions.forEach { contribution ->
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
            text = "${if (contribution.isPositive) "+" else ""}${contribution.percentage.toInt()}%",
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
                    text = "Data Input Harian",
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
            Icon(
                imageVector = if (input.isCompleted) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                contentDescription = if (input.isCompleted) "Completed" else "Not completed",
                tint = if (input.isCompleted) Color(0xFF4CAF50) else Color(0xFFFF9800),
                modifier = Modifier.size(16.dp)
            )
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
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = if (input.isCompleted) Color(0xFF4CAF50) else Color.Gray
        )
    }
} 