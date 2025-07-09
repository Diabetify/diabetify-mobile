package com.itb.diabetify.presentation.home.components

import android.annotation.SuppressLint
import androidx.compose.ui.graphics.Color
import com.itb.diabetify.presentation.home.HomeViewModel
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

fun getBmiCategory(bmi: Double): String {
    return when {
        bmi < 18.5 -> "Kurus"
        bmi < 23 -> "Normal"
        bmi < 25 -> "Beresiko Obesitas"
        bmi < 30 -> "Obesitas I"
        else -> "Obesitas II"
    }
}

fun getBmiCategoryColor(bmi: Double): Color {
    return when {
        bmi < 18.5 -> Color(0xFFF59E0B)
        bmi < 23 -> Color(0xFF10B981)
        bmi < 25 -> Color(0xFFF7B13D)
        bmi < 30 -> Color(0xFFF17C0B)
        else -> Color(0xFFEF4444)
    }
}

fun getActivityAverageColor(days: Int): Color {
    return when {
        days >= 5 -> Color(0xFF10B981)
        days >= 3 -> Color(0xFFF7B13D)
        days >= 1 -> Color(0xFFF17C0B)
        else -> Color(0xFFEF4444)
    }
}

fun calculateRiskFactorColor(
    percentage: Double,
    riskFactors: List<HomeViewModel.RiskFactor>
): Color {
    val dataPercentages = riskFactors.map { it.percentage }

    val maxPositiveValue = dataPercentages.filter { it >= 0 }.maxOrNull() ?: 1.0
    val maxNegativeValue = dataPercentages.filter { it < 0 }.minOrNull()?.let { abs(it) } ?: 1.0

    return when {
        percentage >= 0 -> {
            val intensity = if (maxPositiveValue > 0) percentage / maxPositiveValue else 0.0
            val red = (200 * (0.6 + 0.4 * intensity)).toInt()
            val green = (80 * (1 - intensity)).toInt()
            Color(red, green, green)
        }
        else -> {
            val intensity = abs(percentage) / maxNegativeValue
            val green = (180 * (0.6 + 0.4 * intensity)).toInt()
            val red = (80 * (1 - intensity)).toInt()
            Color(red, green, red)
        }
    }
}

fun getSmokingBackgroundColor(count: Int): Color {
    return when {
        count == 0 -> Color(0xFFDCFCE7)
        count < 10 -> Color(0xFFFEF9C3)
        else -> Color(0xFFFEE2E2)
    }
}

fun getSmokingTextColor(count: Int): Color {
    return when {
        count == 0 -> Color(0xFF059669)
        count < 10 -> Color(0xFFCA8A04)
        else -> Color(0xFFDC2626)
    }
}

@SuppressLint("NewApi")
fun formatRelativeTime(timestamp: String): String {
    if (timestamp.isEmpty() || timestamp == "Belum ada prediksi") return "Belum ada pemeriksaan"

    try {
        val utcDateTime = ZonedDateTime.parse(timestamp)

        val deviceTimezone = ZoneId.systemDefault()
        val localTime = utcDateTime.withZoneSameInstant(deviceTimezone)
        val now = ZonedDateTime.now(deviceTimezone)

        val duration = Duration.between(localTime, now)
        val seconds = duration.seconds.coerceAtLeast(0)
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> "Baru saja"
            days > 0 -> "$days hari lalu"
            hours > 0 -> "$hours jam lalu"
            minutes > 0 -> "$minutes menit lalu"
            else -> "Baru saja"
        }
    } catch (e: Exception) {
        return "Waktu tidak valid"
    }
}

@SuppressLint("NewApi")
fun formatDisplayTime(timestamp: String, format: String = "dd/MM/yyyy HH:mm"): String {
    if (timestamp.isEmpty() || timestamp == "Belum ada prediksi") return "Belum ada prediksi"

    try {
        val utcDateTime = ZonedDateTime.parse(timestamp)

        val deviceTimezone = ZoneId.systemDefault()
        val localTime = utcDateTime.withZoneSameInstant(deviceTimezone)

        val displayFormatter = DateTimeFormatter.ofPattern(format)
        return localTime.format(displayFormatter)

    } catch (e: Exception) {
        return "Format waktu tidak valid"
    }
}

fun getRiskCategoryColor(predictionScore: Double): Color {
    val lowRiskColor = Color(0xFF8BC34A)
    val mediumRiskColor = Color(0xFFFFC107)
    val highRiskColor = Color(0xFFFA821F)
    val veryHighRiskColor = Color(0xFFF44336)
    
    return when {
        predictionScore <= 35 -> lowRiskColor
        predictionScore <= 55 -> mediumRiskColor
        predictionScore <= 70 -> highRiskColor
        else -> veryHighRiskColor
    }
}

fun getRiskCategoryDescription(predictionScore: Double): String {
    return when {
        predictionScore <= 35 -> "Diperkirakan 15 dari 100 orang dengan skor ini akan mengidap Diabetes"
        predictionScore <= 55 -> "Diperkirakan 31 dari 100 orang dengan skor ini akan mengidap Diabetes"
        predictionScore <= 70 -> "Diperkirakan 55 dari 100 orang dengan skor ini akan mengidap Diabetes"
        else -> "Diperkirakan 69 dari 100 orang dengan skor ini akan mengidap Diabetes"
    }
}