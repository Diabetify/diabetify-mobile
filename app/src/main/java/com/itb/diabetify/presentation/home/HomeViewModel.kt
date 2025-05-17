package com.itb.diabetify.presentation.home

import androidx.lifecycle.ViewModel
import com.itb.diabetify.presentation.home.risk_factor_detail.RiskFactor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
): ViewModel() {
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
}