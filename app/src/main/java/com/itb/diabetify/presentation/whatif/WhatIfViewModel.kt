package com.itb.diabetify.presentation.whatif

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCases
import com.itb.diabetify.domain.usecases.profile.ProfileUseCases
import com.itb.diabetify.presentation.common.FieldState
import com.itb.diabetify.presentation.home.HomeViewModel.RiskFactor
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WhatIfViewModel @Inject constructor(
    private val predictionUseCases: PredictionUseCases,
    private val profileUseCases: ProfileUseCases
): ViewModel() {
    // Navigation and Error States
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage

    // Operational States
    private val _latestPredictionState = mutableStateOf(DataState())
    val latestPredictionState: State<DataState> = _latestPredictionState

    private var _profileState = mutableStateOf(DataState())
    val profileState: State<DataState> = _profileState

    private val _whatIfPredictionState = mutableStateOf(DataState())
    val whatIfPredictionState: State<DataState> = _whatIfPredictionState

    // UI States
    private val _predictionScore = mutableDoubleStateOf(0.0)
    val predictionScore: State<Double> = _predictionScore

    private val _riskFactors = mutableStateOf(listOf(
        RiskFactor("Indeks Massa Tubuh", "IMT", 0.0),
        RiskFactor("Hipertensi", "H", 0.0),
        RiskFactor("Riwayat Bayi Makrosomia", "RBM", 0.0),
        RiskFactor("Aktivitas Fisik", "AF", 0.0),
        RiskFactor("Usia", "U", 0.0),
        RiskFactor("Status Merokok", "SM", 0.0),
        RiskFactor("Indeks Brinkman", "IB", 0.0),
        RiskFactor("Riwayat Keluarga", "RK", 0.0),
        RiskFactor("Kolesterol", "K", 0.0),
    ))
    val riskFactors: State<List<RiskFactor>> = _riskFactors

    private val _age = mutableIntStateOf(0)
    val age: State<Int> = _age

    private val _macrosomicBaby = mutableIntStateOf(0)
    val macrosomicBaby: State<Int> = _macrosomicBaby

    private val _yearsSmoking = mutableIntStateOf(0)
    val yearsSmoking: State<Int> = _yearsSmoking

    private val _isBloodline = mutableStateOf(false)
    val isBloodline: State<Boolean> = _isBloodline

    // Field States
    private val _smokingStatusFieldState = mutableStateOf(FieldState())
    val smokingStatusFieldState: State<FieldState> = _smokingStatusFieldState

    private val _averageCigarettesFieldState = mutableStateOf(FieldState())
    val averageCigarettesFieldState: State<FieldState> = _averageCigarettesFieldState

    private val _weightFieldState = mutableStateOf(FieldState())
    val weightFieldState: State<FieldState> = _weightFieldState

    private val _isHypertensionFieldState = mutableStateOf(FieldState())
    val isHypertensionFieldState: State<FieldState> = _isHypertensionFieldState

    private val _physicalActivityFieldState = mutableStateOf(FieldState())
    val physicalActivityFieldState: State<FieldState> = _physicalActivityFieldState

    private val _isCholesterolFieldState = mutableStateOf(FieldState())
    val isCholesterolFieldState: State<FieldState> = _isCholesterolFieldState

    // Initialization
    init {
        collectLatestPredictionData()
        collectProfileData()
    }

    // Setters for Field States
    fun setSmokingStatus(value: String) {
        _smokingStatusFieldState.value = smokingStatusFieldState.value.copy(error = null)
        _smokingStatusFieldState.value = smokingStatusFieldState.value.copy(text = value)
    }

    fun setAverageCigarettes(value: String) {
        _averageCigarettesFieldState.value = averageCigarettesFieldState.value.copy(error = null)
        _averageCigarettesFieldState.value = averageCigarettesFieldState.value.copy(text = value)
    }

    fun setWeight(value: String) {
        _weightFieldState.value = weightFieldState.value.copy(error = null)
        _weightFieldState.value = weightFieldState.value.copy(text = value)
    }

    fun setIsHypertension(value: String) {
        _isHypertensionFieldState.value = isHypertensionFieldState.value.copy(error = null)
        _isHypertensionFieldState.value = isHypertensionFieldState.value.copy(text = value)
    }

    fun setPhysicalActivity(value: String) {
        _physicalActivityFieldState.value = physicalActivityFieldState.value.copy(error = null)
        _physicalActivityFieldState.value = physicalActivityFieldState.value.copy(text = value)
    }

    fun setIsCholesterol(value: String) {
        _isCholesterolFieldState.value = isCholesterolFieldState.value.copy(error = null)
        _isCholesterolFieldState.value = isCholesterolFieldState.value.copy(text = value)
    }

    // Validation Functions
    fun validateFields(): Boolean {
        var isValid = true

        if (smokingStatusFieldState.value.text.isBlank()) {
            _smokingStatusFieldState.value = smokingStatusFieldState.value.copy(error = "Status merokok tidak boleh kosong")
            isValid = false
        }

        if (averageCigarettesFieldState.value.text.toInt() < 0 || averageCigarettesFieldState.value.text.toInt() > 60) {
            _averageCigarettesFieldState.value = averageCigarettesFieldState.value.copy(error = "Jumlah rokok harus antara 0-60 batang")
            isValid = false
        }

        if (weightFieldState.value.text.toInt() < 30 || weightFieldState.value.text.toInt() > 300) {
            _weightFieldState.value = weightFieldState.value.copy(error = "Berat badan harus antara 30-300 kg")
            isValid = false
        }

        if (isHypertensionFieldState.value.text.isBlank()) {
            _isHypertensionFieldState.value = isHypertensionFieldState.value.copy(error = "Hipertensi tidak boleh kosong")
            isValid = false
        }

        if (physicalActivityFieldState.value.text.toInt() < 0 || physicalActivityFieldState.value.text.toInt() > 7) {
            _physicalActivityFieldState.value = physicalActivityFieldState.value.copy(error = "Aktivitas fisik harus antara 0-7 hari")
            isValid = false
        }

        if (isCholesterolFieldState.value.text.isBlank()) {
            _isCholesterolFieldState.value = isCholesterolFieldState.value.copy(error = "Kolesterol tidak boleh kosong")
            isValid = false
        }

        return isValid
    }

    // Use Case Calls
    private fun collectLatestPredictionData() {
        viewModelScope.launch {
            _latestPredictionState.value = latestPredictionState.value.copy(isLoading = true)

            predictionUseCases.getLatestPredictionRepository().onEach { prediction ->
                _latestPredictionState.value = latestPredictionState.value.copy(isLoading = false)

                prediction?.let {
                    _age.intValue = it.age

                    _averageCigarettesFieldState.value = FieldState(
                        text = it.avgSmokeCount.toString(),
                        error = null
                    )
                    _physicalActivityFieldState.value = FieldState(
                        text = it.physicalActivityFrequency.toString(),
                        error = null
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun collectProfileData() {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)

            profileUseCases.getProfileRepository().onEach { profile ->
                _profileState.value = profileState.value.copy(isLoading = false)

                profile?.let {
                    _macrosomicBaby.intValue = it.macrosomicBaby
                    _yearsSmoking.intValue = it.yearOfSmoking
                    _isBloodline.value = it.bloodline

                    _smokingStatusFieldState.value = FieldState(
                        text = it.smoking.toString(),
                        error = null
                    )
                    _weightFieldState.value = FieldState(
                        text = it.weight.toString(),
                        error = null
                    )
                    _isHypertensionFieldState.value = FieldState(
                        text = it.hypertension.toString(),
                        error = null
                    )
                    _isCholesterolFieldState.value = FieldState(
                        text = it.cholesterol.toString(),
                        error = null
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    @SuppressLint("DefaultLocale")
    fun calculateWhatIfPrediction() {
        viewModelScope.launch {
            _whatIfPredictionState.value = whatIfPredictionState.value.copy(isLoading = true)

            val smokingStatus = smokingStatusFieldState.value.text.toInt()
            val averageCigarettes = averageCigarettesFieldState.value.text.toInt()
            val weight = weightFieldState.value.text.toInt()
            val isHypertension = isHypertensionFieldState.value.text.toBoolean()
            val physicalActivity = physicalActivityFieldState.value.text.toInt()
            val isCholesterol = isCholesterolFieldState.value.text.toBoolean()

            val whatIfPredictionResult = predictionUseCases.whatIfPrediction(
                smokingStatus = smokingStatus,
                avgSmokeCount = averageCigarettes,
                weight = weight,
                isHypertension = isHypertension,
                physicalActivityFrequency = physicalActivity,
                isCholesterol = isCholesterol
            )

            _whatIfPredictionState.value = whatIfPredictionState.value.copy(isLoading = false)

            if (whatIfPredictionResult.smokingStatusError != null) {
                _smokingStatusFieldState.value = smokingStatusFieldState.value.copy(error = whatIfPredictionResult.smokingStatusError)
            }

            if (whatIfPredictionResult.avgSmokeCountError != null) {
                _averageCigarettesFieldState.value = averageCigarettesFieldState.value.copy(error = whatIfPredictionResult.avgSmokeCountError)
            }

            if (whatIfPredictionResult.weightError != null) {
                _weightFieldState.value = weightFieldState.value.copy(error = whatIfPredictionResult.weightError)
            }

            if (whatIfPredictionResult.physicalActivityFrequencyError != null) {
                _physicalActivityFieldState.value = physicalActivityFieldState.value.copy(error = whatIfPredictionResult.physicalActivityFrequencyError)
            }

            when (whatIfPredictionResult.result) {
                is Resource.Success -> {
                    val responseData = whatIfPredictionResult.result.data?.data

                    responseData?.let { data ->
                        _predictionScore.doubleValue = data.riskPercentage

                        val updatedRiskFactors = _riskFactors.value.map { riskFactor ->
                            val featureKey = when (riskFactor.abbreviation) {
                                "IMT" -> "BMI"
                                "H" -> "is_hypertension"
                                "RBM" -> "is_macrosomic_baby"
                                "AF" -> "moderate_physical_activity_frequency"
                                "U" -> "age"
                                "SM" -> "smoking_status"
                                "IB" -> "brinkman_index"
                                "RK" -> "is_bloodline"
                                "K" -> "is_cholesterol"
                                else -> null
                            }

                            featureKey?.let { key ->
                                data.featureExplanations[key]?.let { explanation ->
                                    val adjustedContribution = if (explanation.impact == 0) {
                                        -explanation.contribution
                                    } else {
                                        explanation.contribution
                                    }
                                    riskFactor.copy(
                                        name = riskFactor.name,
                                        abbreviation = riskFactor.abbreviation,
                                        percentage = adjustedContribution * 100
                                    )
                                }
                            } ?: riskFactor
                        }

                        _riskFactors.value = updatedRiskFactors
                    }

                    _navigationEvent.value = "WHAT_IF_RESULT_SCREEN"
                }
                is Resource.Error -> {
                    _errorMessage.value = whatIfPredictionResult.result.message ?: "Terjadi kesalahan saat melakukan prediksi"
                    whatIfPredictionResult.result.message?.let { Log.e("WhatIfViewModel", it) }
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat melakukan prediksi"
                    Log.e("WhatIfViewModel", "Unknown error occurred")
                }
            }
        }
    }

    // Helper Functions
    fun resetFields() {
        collectLatestPredictionData()
        collectProfileData()

        _successMessage.value = "Data berhasil di-reset"
    }

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }

    fun onSuccessShown() {
        _successMessage.value = null
    }
}
