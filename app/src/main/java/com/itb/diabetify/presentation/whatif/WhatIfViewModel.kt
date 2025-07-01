package com.itb.diabetify.presentation.whatif

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCases
import com.itb.diabetify.domain.usecases.profile.ProfileUseCases
import com.itb.diabetify.presentation.common.FieldState
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
    // Navigation States
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Operational States
    private val _latestPredictionState = mutableStateOf(DataState())
    val latestPredictionState: State<DataState> = _latestPredictionState

    private var _profileState = mutableStateOf(DataState())
    val profileState: State<DataState> = _profileState

    private val _whatIfPredictionState = mutableStateOf(DataState())
    val whatIfPredictionState: State<DataState> = _whatIfPredictionState

    // States
    private val _predictionResult = mutableStateOf<Float?>(null)
    val predictionResult: State<Float?> = _predictionResult

    private val _age = mutableIntStateOf(0)
    val age: State<Int> = _age

    private val _macrosomicBaby = mutableIntStateOf(0)
    val macrosomicBaby: State<Int> = _macrosomicBaby

    private val _yearsSmoking = mutableIntStateOf(0)
    val yearsSmoking: State<Int> = _yearsSmoking

    private val _isBloodline = mutableStateOf(false)
    val isBloodline: State<Boolean> = _isBloodline

    private val _smokingStatus = mutableIntStateOf(0) // 0=never, 1=quit, 2=active
    val smokingStatus: State<Int> = _smokingStatus

    private val _averageCigarettes = mutableIntStateOf(0)
    val averageCigarettes: State<Int> = _averageCigarettes

    private val _weight = mutableIntStateOf(0)
    val weight: State<Int> = _weight

    private val _isHypertension = mutableStateOf(false)
    val isHypertension: State<Boolean> = _isHypertension

    private val _physicalActivityFrequency = mutableIntStateOf(0)
    val physicalActivityFrequency: State<Int> = _physicalActivityFrequency

    private val _isCholesterol = mutableStateOf(false)
    val isCholesterol: State<Boolean> = _isCholesterol

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

    init {
        collectLatestPrediction()
        collectProfileData()
    }

    private fun collectLatestPrediction() {
        viewModelScope.launch {
            _latestPredictionState.value = latestPredictionState.value.copy(isLoading = true)

            predictionUseCases.getLatestPredictionRepository().onEach { prediction ->
                _latestPredictionState.value = latestPredictionState.value.copy(isLoading = false)

                prediction?.let {
                    _age.intValue = it.age?.toInt() ?: 0
                    _averageCigarettes.intValue = it.avgSmokeCount?.toInt() ?: 0
                    _physicalActivityFrequency.intValue = it.physicalActivityFrequency?.toInt() ?: 0
                }

                Log.d("WhatIfViewModel", "Latest Prediction: ${prediction?.isMacrosomicBaby}")
            }.launchIn(viewModelScope)
        }
    }

    private fun collectProfileData() {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)

            profileUseCases.getProfileRepository().onEach { profile ->
                _profileState.value = profileState.value.copy(isLoading = false)

                profile?.let {
                    _macrosomicBaby.intValue = it.macrosomicBaby?.toInt() ?: 0
                    _isBloodline.value = it.bloodline ?: false
                    _smokingStatus.intValue = it.smoking?.toInt() ?: 0
                    _yearsSmoking.intValue = it.yearOfSmoking?.toInt() ?: 0
                    _weight.intValue = it.weight?.toInt() ?: 0
                    _isHypertension.value = it.hypertension ?: false
                    _isCholesterol.value = it.cholesterol ?: false
                }
            }.launchIn(viewModelScope)
        }
    }

    fun calculateWhatIfPrediction() {
    }

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }
}
