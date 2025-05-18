package com.itb.diabetify.presentation.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
): ViewModel() {
    val lowRiskColor = Color(0xFF8BC34A)    // Green
    val mediumRiskColor = Color(0xFFFFC107) // Yellow
    val highRiskColor = Color(0xFFFA821F)   // Orange
    val veryHighRiskColor = Color(0xFFF44336) // Red

    data class RiskFactor(
        val name: String,
        val abbreviation: String,
        val percentage: Float
    )
    val riskFactors = listOf(
        RiskFactor("Indeks Massa Tubuh", "IMT", 25.4f),
        RiskFactor("Hipertensi", "HTN", -15.2f),
        RiskFactor("Riwayat Kelahiran", "RK", 10.7f),
        RiskFactor("Aktivitas Fisik", "AF", -5.3f),
        RiskFactor("Usia", "U", 18.9f),
        RiskFactor("Indeks Merokok", "IM", -8.6f)
    )

    data class RiskFactorDetails(
        val name: String,
        val fullName: String,
        val impactPercentage: Float,
        val description: String,
        val idealValue: String,
        val currentValue: String,
        val isModifiable: Boolean = true
    )
    val riskFactorDetails = listOf(
        RiskFactorDetails(
            name = "IMT",
            fullName = "Indeks Massa Tubuh",
            impactPercentage = 25.4f,
            description = "Indeks Massa Tubuh adalah pengukuran yang menggunakan berat dan tinggi badan untuk mengestimasikan jumlah lemak tubuh. IMT yang lebih tinggi dikaitkan dengan risiko yang lebih besar untuk berbagai penyakit.",
            idealValue = "18.5 - 24.9 kg/m²",
            currentValue = "28.4 kg/m²"
        ),
        RiskFactorDetails(
            name = "HTN",
            fullName = "Hipertensi",
            impactPercentage = -15.2f,
            description = "Hipertensi atau tekanan darah tinggi adalah kondisi medis kronis dengan tekanan darah di arteri meningkat. Tanpa pengobatan, hipertensi meningkatkan risiko penyakit jantung dan stroke.",
            idealValue = "< 120/80 mmHg",
            currentValue = "145/90 mmHg"
        ),
        RiskFactorDetails(
            name = "RK",
            fullName = "Riwayat Kelahiran",
            impactPercentage = 10.7f,
            description = "Faktor riwayat kelahiran termasuk berat badan lahir, kelahiran prematur, atau komplikasi kelahiran lainnya yang dapat memengaruhi risiko kesehatan di masa depan.",
            idealValue = "Berat lahir normal, kelahiran cukup bulan",
            currentValue = "Riwayat kelahiran prematur"
        ),
        RiskFactorDetails(
            name = "AF",
            fullName = "Aktivitas Fisik",
            impactPercentage = -5.3f,
            description = "Aktivitas fisik mengacu pada tingkat olahraga dan gerakan fisik yang dilakukan secara rutin. Aktivitas fisik yang cukup membantu mengurangi risiko berbagai penyakit kronis.",
            idealValue = "Min. 150 menit aktivitas sedang per minggu",
            currentValue = "60 menit per minggu"
        ),
        RiskFactorDetails(
            name = "U",
            fullName = "Usia",
            impactPercentage = 18.9f,
            description = "Usia adalah faktor risiko yang tidak dapat dimodifikasi namun memiliki pengaruh signifikan terhadap risiko kesehatan. Risiko berbagai penyakit meningkat seiring bertambahnya usia.",
            idealValue = "-",
            currentValue = "58 tahun",
            isModifiable = false
        ),
        RiskFactorDetails(
            name = "IM",
            fullName = "Indeks Merokok",
            impactPercentage = -8.6f,
            description = "Indeks Merokok mengukur kebiasaan merokok seseorang termasuk jumlah dan durasi merokok. Merokok meningkatkan risiko berbagai penyakit kardiovaskular dan kanker.",
            idealValue = "0 (tidak merokok)",
            currentValue = "10 batang per hari"
        )
    )
}