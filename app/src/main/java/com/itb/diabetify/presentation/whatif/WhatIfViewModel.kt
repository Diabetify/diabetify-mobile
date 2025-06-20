package com.itb.diabetify.presentation.whatif

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class WhatIfState(
    // Non-modifiable fields (displayed but disabled)
    val age: Int = 0,
    val isMacrosomicBaby: Int = 0, // 0=no, 1=yes, 2=male/never pregnant
    val yearsSmokingOriginal: Int = 0,
    val isBloodline: Boolean = false,
    
    // Modifiable fields
    val smokingStatus: Int = 0, // 0=never, 1=quit, 2=active
    val originalSmokingStatus: Int = 0, // To determine allowed transitions
    val averageCigarettes: String = "",
    val weight: String = "",
    val isHypertension: Boolean = false,
    val physicalActivityFrequency: String = "",
    val isCholesterol: Boolean = false,
    
    // UI state
    val isLoading: Boolean = false,
    val predictionResult: Float? = null,
    val showResult: Boolean = false,
    val errorMessage: String = ""
)

@HiltViewModel
class WhatIfViewModel @Inject constructor(
    // TODO: Add use cases for getting current user data and prediction
): ViewModel() {
    
    private val _state = mutableStateOf(WhatIfState())
    val state: State<WhatIfState> = _state
    
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent
    
    init {
        loadCurrentUserData()
    }
    
    private fun loadCurrentUserData() {
        // TODO: Load current user data from repository
        // For now, using mock data
        _state.value = _state.value.copy(
            age = 45,
            isMacrosomicBaby = 0,
            yearsSmokingOriginal = 10,
            isBloodline = false,
            smokingStatus = 1,
            originalSmokingStatus = 1,
            averageCigarettes = "10",
            weight = "25.5",
            isHypertension = false,
            physicalActivityFrequency = "2",
            isCholesterol = false
        )
    }
    
    fun updateSmokingStatus(newStatus: Int) {
        val allowedTransitions = getAllowedSmokingTransitions(_state.value.originalSmokingStatus)
        if (allowedTransitions.contains(newStatus)) {
            _state.value = _state.value.copy(smokingStatus = newStatus)
            updateBrinkmanIndex()
        }
    }
    
    fun updateAverageCigarettes(value: String) {
        _state.value = _state.value.copy(averageCigarettes = value)
        updateBrinkmanIndex()
    }
    
    fun updateWeight(value: String) {
        _state.value = _state.value.copy(weight = value)
    }
    
    fun updateHypertension(hasHypertension: Boolean) {
        _state.value = _state.value.copy(isHypertension = hasHypertension)
    }
    
    fun updatePhysicalActivity(minutes: String) {
        _state.value = _state.value.copy(physicalActivityFrequency = minutes)
    }
    
    fun updateCholesterol(hasCholesterol: Boolean) {
        _state.value = _state.value.copy(isCholesterol = hasCholesterol)
    }
    
    private fun updateBrinkmanIndex() {
        val years = _state.value.yearsSmokingOriginal
        val avgCigarettes = _state.value.averageCigarettes.toFloatOrNull() ?: 0f
        val brinkmanValue = years * avgCigarettes
        
        val index = when {
            _state.value.smokingStatus == 0 -> 0 // Never smoked
            brinkmanValue == 0f -> 0
            brinkmanValue <= 200 -> 1 // Mild
            brinkmanValue <= 400 -> 2 // Moderate
            else -> 3 // Severe
        }
    }
    
    private fun getAllowedSmokingTransitions(originalStatus: Int): List<Int> {
        return when (originalStatus) {
            0 -> listOf(0, 1, 2) // Never -> can try quit(1) and active(2)
            1 -> listOf(1, 2) // Quit -> can try active(2)
            2 -> listOf(1, 2) // Active -> can try quit(1)
            else -> listOf(originalStatus)
        }
    }
    
    fun calculateWhatIfPrediction() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = "")
        
        // TODO: Implement actual prediction API call
        // For now, using mock calculation
        
        // Validate inputs
        val weightValue = _state.value.weight.toFloatOrNull()
        val physicalActivityValue = _state.value.physicalActivityFrequency.toFloatOrNull()
        val avgCigarettesValue = _state.value.averageCigarettes.toFloatOrNull()
        
        if (weightValue == null || weightValue <= 0) {
            _state.value = _state.value.copy(
                isLoading = false,
                errorMessage = "BMI harus berupa angka yang valid"
            )
            return
        }
        
        if (physicalActivityValue == null || physicalActivityValue < 0) {
            _state.value = _state.value.copy(
                isLoading = false,
                errorMessage = "Aktivitas fisik harus berupa angka yang valid"
            )
            return
        }
        
        if (_state.value.smokingStatus > 0 && (avgCigarettesValue == null || avgCigarettesValue < 0)) {
            _state.value = _state.value.copy(
                isLoading = false,
                errorMessage = "Rata-rata rokok harus berupa angka yang valid"
            )
            return
        }
        
        // Mock prediction calculation
        var riskScore = 0.3f // Base risk
        
        // Age factor
        if (_state.value.age > 45) riskScore += 0.1f
        
        // BMI factor
        if (weightValue!! > 25) riskScore += 0.15f
        if (weightValue > 30) riskScore += 0.1f
        
        // Smoking factor
        when (_state.value.smokingStatus) {
            1 -> riskScore += 0.05f // Quit
            2 -> riskScore += 0.15f // Active
        }
        
        // Hypertension factor
        if (_state.value.isHypertension) riskScore += 0.1f
        
        // Cholesterol factor
        if (_state.value.isCholesterol) riskScore += 0.08f
        
        // Physical activity factor (protective)
        if (physicalActivityValue!! >= 150) riskScore -= 0.05f
        
        // Bloodline factor
        if (_state.value.isBloodline) riskScore += 0.12f
        
        // Macrosomic baby factor
        if (_state.value.isMacrosomicBaby == 1) riskScore += 0.08f
        
        // Ensure score is between 0 and 1
        riskScore = riskScore.coerceIn(0f, 1f)
        
        _state.value = _state.value.copy(
            isLoading = false,
            predictionResult = riskScore,
            showResult = true
        )
        
        // Navigate to result screen
        _navigationEvent.value = "WHAT_IF_RESULT_SCREEN"
    }
    
    fun resetToOriginal() {
        loadCurrentUserData()
        _state.value = _state.value.copy(
            showResult = false,
            predictionResult = null,
            errorMessage = ""
        )
    }
    
    fun getSmokingStatusText(status: Int): String {
        return when (status) {
            0 -> "Tidak Pernah Merokok"
            1 -> "Berhenti Merokok"
            2 -> "Aktif Merokok"
            else -> "Unknown"
        }
    }
    
    fun getBrinkmanIndexText(index: Int): String {
        return when (index) {
            0 -> "Tidak Pernah Merokok"
            1 -> "Perokok Ringan"
            2 -> "Perokok Sedang"
            3 -> "Perokok Berat"
            else -> "Unknown"
        }
    }
    
    fun getMacrosomicBabyText(status: Int): String {
        return when (status) {
            0 -> "Tidak"
            1 -> "Pernah"
            2 -> "Laki-laki/Tidak Pernah Hamil"
            else -> "Unknown"
        }
    }
    
    fun getAllowedSmokingTransitionsPublic(originalStatus: Int): List<Int> {
        return getAllowedSmokingTransitions(originalStatus)
    }
    
    fun onNavigationHandled() {
        _navigationEvent.value = null
    }
}