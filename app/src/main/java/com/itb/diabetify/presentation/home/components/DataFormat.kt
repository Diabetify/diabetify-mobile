package com.itb.diabetify.presentation.home.components

import androidx.compose.ui.graphics.Color

fun formatRiwayatKehamilan(status: String): String {
    return when(status) {
        "ya" -> "Ya"
        "tidak" -> "Tidak"
        "sedang" -> "Sedang Hamil"
        else -> status
    }
}

fun formatHipertensi(status: String): String {
    return when(status) {
        "ya" -> "Ya"
        "tidak" -> "Tidak"
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