package com.itb.diabetify.presentation.home.risk_factor_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.itb.diabetify.presentation.home.HomeViewModel.RiskFactorDetails
import com.itb.diabetify.ui.theme.poppinsFontFamily
import kotlin.math.abs

@Composable
fun RiskFactorCard(
    riskFactor: RiskFactorDetails,
) {
    val color = calculateRiskFactorColor(riskFactor.impactPercentage)

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(color, RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${riskFactor.name}: ${riskFactor.fullName}",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = color.copy(alpha = 0.15f)
                    )
                ) {
                    val formattedValue = if (riskFactor.impactPercentage >= 0)
                        "+${String.format("%.1f", riskFactor.impactPercentage)}%"
                    else
                        "${String.format("%.1f", riskFactor.impactPercentage)}%"

                    Text(
                        text = formattedValue,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = color,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Text(
                text = riskFactor.description,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = colorResource(id = R.color.primary),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (riskFactor.isModifiable) {
                ComparisonBar(riskFactor)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Current and Ideal Values
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nilai Ideal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        color = colorResource(id = R.color.primary)
                    )

                    Text(
                        text = riskFactor.idealValue,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        lineHeight = 22.sp,
                        color = colorResource(id = R.color.primary)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nilai Anda",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        color = colorResource(id = R.color.primary)
                    )

                    val isHighRisk = abs(riskFactor.impactPercentage) > 15f
                    Text(
                        text = riskFactor.currentValue,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        lineHeight = 22.sp,
                        color = if (isHighRisk && riskFactor.isModifiable) Color.Red else colorResource(id = R.color.primary)
                    )
                }
            }
        }
    }
}



@Composable
fun ComparisonBar(riskFactor: RiskFactorDetails) {
    val isNumber = riskFactor.currentValue.any { it.isDigit() }

    if (!isNumber) return

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Perbandingan dengan Nilai Ideal",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            color = colorResource(id = R.color.primary),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        val progress = calculateProgress(riskFactor)

        val color = when {
            progress > 80f -> Color.Green
            progress > 50f -> Color(0xFFFFA500)
            else -> Color.Red
        }

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress / 100)
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(color)
            )
        }

        // Progress label
        Text(
            text = when {
                progress > 80f -> "Baik"
                progress > 50f -> "Perlu Perbaikan"
                else -> "Jauh dari Ideal"
            },
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = color,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

fun calculateRiskFactorColor(percentage: Float): Color {
    return when {
        percentage >= 0 -> {
            val intensity = percentage / 25f
            val red = (255 * (0.5f + 0.5f * intensity)).coerceIn(0f, 255f).toInt()
            val green = (128 * (1 - intensity)).coerceIn(0f, 128f).toInt()
            Color(red, green, green)
        }
        else -> {
            val intensity = abs(percentage) / 15f
            val blue = (255 * (0.5f + 0.5f * intensity)).coerceIn(0f, 255f).toInt()
            val red = (128 * (1 - intensity)).coerceIn(0f, 128f).toInt()
            Color(red, red, blue)
        }
    }
}

fun extractNumericValue(value: String): Float? {
    val numericRegex = """(\d+\.?\d*)""".toRegex()
    val match = numericRegex.find(value)
    return match?.groupValues?.get(1)?.toFloatOrNull()
}

fun calculateProgress(riskFactor: RiskFactorDetails): Float {
    if (!riskFactor.isModifiable) return 50f

    when (riskFactor.name) {
        "IMT" -> {
            val currentBMI = extractNumericValue(riskFactor.currentValue) ?: return 0f

            val idealRange = """(\d+\.?\d*)\s*-\s*(\d+\.?\d*)""".toRegex()
                .find(riskFactor.idealValue)

            if (idealRange != null) {
                val lowerBound = idealRange.groupValues[1].toFloatOrNull() ?: 18.5f
                val upperBound = idealRange.groupValues[2].toFloatOrNull() ?: 24.9f

                return when {
                    currentBMI in lowerBound..upperBound -> 100f
                    currentBMI < lowerBound -> {
                        val ratio = currentBMI / lowerBound
                        (ratio * 100f).coerceIn(0f, 100f)
                    }
                    else -> {
                        val maxBMI = upperBound * 1.5f
                        val ratio = 1f - ((currentBMI - upperBound) / (maxBMI - upperBound))
                        (ratio * 100f).coerceIn(0f, 100f)
                    }
                }
            }
            return 50f
        }

        "HTN" -> {
            val currentSystolic = extractNumericValue(riskFactor.currentValue) ?: return 0f
            val idealSystolic = 120f

            val ratio = idealSystolic / currentSystolic
            return (ratio * 100f).coerceIn(0f, 100f)
        }

        "RK" -> {
            return if (riskFactor.currentValue.contains("prematur", ignoreCase = true)) {
                40f
            } else {
                90f
            }
        }

        "AF" -> {
            val currentMinutes = extractNumericValue(riskFactor.currentValue) ?: return 0f
            val idealMinutes = 150f

            val ratio = currentMinutes / idealMinutes
            return (ratio * 100f).coerceIn(0f, 100f)
        }

        "IM" -> {
            val currentCigarettes = extractNumericValue(riskFactor.currentValue) ?: return 0f
            val idealCigarettes = 0f

            val worstCase = 20f
            val ratio = 1f - (currentCigarettes / worstCase)
            return (ratio * 100f).coerceIn(0f, 100f)
        }

        else -> {
            val currentValue = extractNumericValue(riskFactor.currentValue) ?: return 50f
            val idealValue = extractNumericValue(riskFactor.idealValue) ?: return 50f

            val ratio = if (riskFactor.impactPercentage < 0) {
                idealValue / currentValue
            } else {
                currentValue / idealValue
            }

            return (ratio * 100f).coerceIn(0f, 100f)
        }
    }
}