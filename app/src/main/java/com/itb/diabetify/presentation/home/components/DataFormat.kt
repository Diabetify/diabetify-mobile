package com.itb.diabetify.presentation.home.components

import androidx.compose.ui.graphics.Color
import com.itb.diabetify.presentation.home.HomeViewModel.RiskFactorDetails
import kotlin.math.abs

fun formatMacrosomicBaby(status: String): String {
    return when(status) {
        "true" -> "Ya"
        "false" -> "Tidak"
        else -> status
    }
}

fun formatHypertension(status: String): String {
    return when(status) {
        "true" -> "Ya"
        "false" -> "Tidak"
        else -> status
    }
}

fun getBmiCategory(bmi: Double): String {
    return when {
        bmi < 18.5 -> "Berat Badan Kurang"
        bmi < 25 -> "Berat Badan Normal"
        bmi < 30 -> "Berat Badan Berlebih"
        else -> "Obesitas"
    }
}

fun getBmiCategoryColor(bmi: Double): Color {
    return when {
        bmi < 18.5 -> Color(0xFF2563EB)
        bmi < 25 -> Color(0xFF059669)
        bmi < 30 -> Color(0xFFD97706)
        else -> Color(0xFFDC2626)
    }
}

fun getActivityLevelColor(minutes: Int): Color {
    return when {
        minutes < 15 -> Color(0xFFDC2626)
        minutes < 30 -> Color(0xFFD97706)
        else -> Color(0xFF059669)
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

fun getSmokingBackgroundColor(smokingValue: Int): Color {
    return when {
        smokingValue == 0 -> Color(0xFFF0FDF4)
        smokingValue < 5 -> Color(0xFFFEF3C7)
        smokingValue < 10 -> Color(0xFFFEE2E2)
        else -> Color(0xFFFEF2F2)
    }
}

fun getSmokingTextColor(smokingValue: Int): Color {
    return when {
        smokingValue == 0 -> Color(0xFF059669)
        smokingValue < 5 -> Color(0xFFD97706)
        smokingValue < 10 -> Color(0xFFEF4444)
        else -> Color(0xFFDC2626)
    }
}