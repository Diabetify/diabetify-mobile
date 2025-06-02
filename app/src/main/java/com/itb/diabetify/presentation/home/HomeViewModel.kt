package com.itb.diabetify.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.repository.ActivityRepository
import com.itb.diabetify.domain.repository.PredictionRepository
import com.itb.diabetify.domain.repository.ProfileRepository
import com.itb.diabetify.domain.usecases.activity.GetActivityTodayUseCase
import com.itb.diabetify.domain.usecases.prediction.GetLatestPredictionUseCase
import com.itb.diabetify.domain.usecases.profile.GetProfileUseCase
import com.itb.diabetify.domain.usecases.user.GetUserUseCase
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getActivityTodayUseCase: GetActivityTodayUseCase,
    private val getLatestPredictionUseCase: GetLatestPredictionUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val predictionRepository: PredictionRepository,
    private val profileRepository: ProfileRepository,
    private val activityRepository: ActivityRepository
): ViewModel() {
    private var _userState = mutableStateOf(DataState())
    val userState: State<DataState> = _userState

    private var _activityTodayState = mutableStateOf(DataState())
    val activityTodayState: State<DataState> = _activityTodayState

    private var _latestPredictionState = mutableStateOf(DataState())
    val latestPredictionState: State<DataState> = _latestPredictionState

    private var _profileState = mutableStateOf(DataState())
    val profileState: State<DataState> = _profileState

    private val _errorMessage = mutableStateOf<String?>(null)

    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _latestPredictionScoreState = mutableStateOf("0.0")
    val latestPredictionScoreState: State<String> = _latestPredictionScoreState

    private val _riskFactors = mutableStateOf(listOf(
        RiskFactor("Indeks Massa Tubuh", "IMT", 0f),
        RiskFactor("Hipertensi", "HTN", 0f),
        RiskFactor("Riwayat Kelahiran", "RK", 0f),
        RiskFactor("Aktivitas Fisik", "AF", 0f),
        RiskFactor("Usia", "U", 0f),
        RiskFactor("Indeks Merokok", "IM", 0f)
    ))
    val riskFactors: State<List<RiskFactor>> = _riskFactors

    private val _riskFactorDetails = mutableStateOf(listOf(
        RiskFactorDetails(
            name = "IMT",
            fullName = "Indeks Massa Tubuh",
            impactPercentage = 0f,
            description = "Indeks Massa Tubuh adalah pengukuran yang menggunakan berat dan tinggi badan untuk mengestimasikan jumlah lemak tubuh. IMT yang lebih tinggi dikaitkan dengan risiko yang lebih besar untuk berbagai penyakit.",
            idealValue = "18.5 - 24.9 kg/m²",
            currentValue = "0 kg/m²"
        ),
        RiskFactorDetails(
            name = "HTN",
            fullName = "Hipertensi",
            impactPercentage = 0f,
            description = "Hipertensi atau tekanan darah tinggi adalah kondisi medis kronis dengan tekanan darah di arteri meningkat. Tanpa pengobatan, hipertensi meningkatkan risiko penyakit jantung dan stroke.",
            idealValue = "< 120/80 mmHg",
            currentValue = "0/0 mmHg"
        ),
        RiskFactorDetails(
            name = "RK",
            fullName = "Riwayat Kelahiran",
            impactPercentage = 0f,
            description = "Faktor riwayat kelahiran termasuk berat badan lahir, kelahiran prematur, atau komplikasi kelahiran lainnya yang dapat memengaruhi risiko kesehatan di masa depan.",
            idealValue = "Berat lahir normal, kelahiran cukup bulan",
            currentValue = "-"
        ),
        RiskFactorDetails(
            name = "AF",
            fullName = "Aktivitas Fisik",
            impactPercentage = 0f,
            description = "Aktivitas fisik mengacu pada tingkat olahraga dan gerakan fisik yang dilakukan secara rutin. Aktivitas fisik yang cukup membantu mengurangi risiko berbagai penyakit kronis.",
            idealValue = "Min. 150 menit aktivitas sedang per minggu",
            currentValue = "0 menit"
        ),
        RiskFactorDetails(
            name = "U",
            fullName = "Usia",
            impactPercentage = 0f,
            description = "Usia adalah faktor risiko yang tidak dapat dimodifikasi namun memiliki pengaruh signifikan terhadap risiko kesehatan. Risiko berbagai penyakit meningkat seiring bertambahnya usia.",
            idealValue = "-",
            currentValue = "0 tahun",
            isModifiable = false
        ),
        RiskFactorDetails(
            name = "IM",
            fullName = "Indeks Merokok",
            impactPercentage = 0f,
            description = "Indeks Merokok mengukur kebiasaan merokok seseorang termasuk jumlah dan durasi merokok. Merokok meningkatkan risiko berbagai penyakit kardiovaskular dan kanker.",
            idealValue = "0 (tidak merokok)",
            currentValue = "0 batang per hari"
        )
    ))
    val riskFactorDetails: State<List<RiskFactorDetails>> = _riskFactorDetails

    private val _bmiValueState = mutableStateOf("0.0")
    val bmiValueState: State<String> = _bmiValueState

    private val _weightValueState = mutableStateOf("0")
    val weightValueState: State<String> = _weightValueState

    private val _heightValueState = mutableStateOf("0")
    val heightValueState: State<String> = _heightValueState

    private val _isHypertensionState = mutableStateOf("false")
    val isHypertensionState: State<String> = _isHypertensionState

    private val _isMacrosomicBabyState = mutableStateOf("false")
    val isMacrosomicBabyState: State<String> = _isMacrosomicBabyState

    private val _smokeValueState = mutableStateOf("0")
    val smokeValueState: State<String> = _smokeValueState

    private val _physicalActivityValueState = mutableStateOf("0")
    val physicalActivityValueState: State<String> = _physicalActivityValueState

    init {
        loadUserData()
        loadLatestPredictionData()
        loadActivityTodayData()
        loadProfileData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _userState.value = userState.value.copy(isLoading = true)

            val getUserResult = getUserUseCase()

            _userState.value = userState.value.copy(isLoading = false)

            when (getUserResult.result) {
                is Resource.Success -> {
                    Log.d("HomeViewModel", "User data loaded successfully")
                }
                is Resource.Error -> {
                    _errorMessage.value = getUserResult.result.message ?: "Unknown error occurred"
                    getUserResult.result.message?.let { Log.d("HomeViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("HomeViewModel", "Loading")
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("HomeViewModel", "Unexpected error")
                }
            }
        }
    }

    private fun loadLatestPredictionData() {
        viewModelScope.launch {
            _latestPredictionState.value = latestPredictionState.value.copy(isLoading = true)

            val getLatestPredictionResult = getLatestPredictionUseCase()

            _latestPredictionState.value = latestPredictionState.value.copy(isLoading = false)

            when (getLatestPredictionResult.result) {
                is Resource.Success -> {
                    Log.d("HomeViewModel", "Latest prediction data loaded successfully")
                    collectLatestPrediction()
                }
                is Resource.Error -> {
                    _errorMessage.value = getLatestPredictionResult.result.message ?: "Unknown error occurred"
                    getLatestPredictionResult.result.message?.let { Log.d("HomeViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("HomeViewModel", "Loading latest prediction data")
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("HomeViewModel", "Unexpected error loading latest prediction data")
                }
            }
        }
    }

    private fun loadActivityTodayData() {
        viewModelScope.launch {
            _activityTodayState.value = activityTodayState.value.copy(isLoading = true)

            val getActivityTodayResult = getActivityTodayUseCase()

            _activityTodayState.value = activityTodayState.value.copy(isLoading = false)

            when (getActivityTodayResult.result) {
                is Resource.Success -> {
                    Log.d("HomeViewModel", "Activity data loaded successfully")
                    collectActivityToday()
                }
                is Resource.Error -> {
                    _errorMessage.value = getActivityTodayResult.result.message ?: "Unknown error occurred"
                    getActivityTodayResult.result.message?.let { Log.d("HomeViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("HomeViewModel", "Loading")
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("HomeViewModel", "Unexpected error")
                }
            }
        }
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)

            val getProfileResult = getProfileUseCase()

            _profileState.value = profileState.value.copy(isLoading = false)

            when (getProfileResult.result) {
                is Resource.Success -> {
                    Log.d("HomeViewModel", "Profile data loaded successfully")
                    collectProfile()
                }
                is Resource.Error -> {
                    if (getProfileResult.result.message?.contains("404") == true) {
                        Log.d("HomeViewModel", "Profile not found, navigating to survey screen")
                        resetToDefaultValues()
                        _navigationEvent.value = "SURVEY_SCREEN"
                    } else {
                        _errorMessage.value = getProfileResult.result.message ?: "Unknown error occurred"
                        getProfileResult.result.message?.let { Log.d("HomeViewModel", it) }
                    }
                }
                is Resource.Loading -> {
                    Log.d("HomeViewModel", "Loading profile data")
                }
                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("HomeViewModel", "Unexpected error loading profile data")
                }
            }
        }
    }

    private fun resetToDefaultValues() {
        _latestPredictionScoreState.value = "0.0"
        _bmiValueState.value = "0.0"
        _weightValueState.value = "0"
        _heightValueState.value = "0"
        _isHypertensionState.value = "false"
        _isMacrosomicBabyState.value = "false"
        _smokeValueState.value = "0"
        _physicalActivityValueState.value = "0"
        
        _riskFactors.value = _riskFactors.value.map { it.copy(percentage = 0f) }
        
        _riskFactorDetails.value = _riskFactorDetails.value.map {
            it.copy(impactPercentage = 0f, currentValue = when(it.name) {
                "IMT" -> "0 kg/m²"
                "HTN" -> "0/0 mmHg"
                "RK" -> "-"
                "AF" -> "0 menit"
                "U" -> "0 tahun"
                "IM" -> "0 batang per hari"
                else -> "0"
            })
        }
    }

    private fun collectLatestPrediction() {
        viewModelScope.launch {
            _latestPredictionState.value = latestPredictionState.value.copy(isLoading = true)

            predictionRepository.getLatestPrediction().collect { prediction ->
                _latestPredictionState.value = latestPredictionState.value.copy(isLoading = false)

                prediction?.let { latestPrediction ->
                    _latestPredictionScoreState.value = latestPrediction.riskScore ?: "0.0"

                    _riskFactors.value = listOf(
                        RiskFactor("Indeks Massa Tubuh", "IMT", (latestPrediction.bmiContribution?.toFloatOrNull() ?: 0f) * 100f),
                        RiskFactor("Hipertensi", "HTN", (latestPrediction.isHypertensionContribution?.toFloatOrNull() ?: 0f) * 100f),
                        RiskFactor("Riwayat Kelahiran", "RK", (latestPrediction.isMacrosomicBabyContribution?.toFloatOrNull() ?: 0f) * 100f),
                        RiskFactor("Aktivitas Fisik", "AF", (latestPrediction.physicalActivityMinutesContribution?.toFloatOrNull() ?: 0f) * 100f),
                        RiskFactor("Usia", "U", (latestPrediction.ageContribution?.toFloatOrNull() ?: 0f) * 100f),
                        RiskFactor("Indeks Merokok", "IM", (latestPrediction.brinkmanScoreContribution?.toFloatOrNull() ?: 0f) * 100f)
                    )

                    _riskFactorDetails.value = listOf(
                        RiskFactorDetails(
                            name = "IMT",
                            fullName = "Indeks Massa Tubuh",
                            impactPercentage = (latestPrediction.bmiContribution?.toFloatOrNull() ?: 0f) * 100f,
                            description = "Indeks Massa Tubuh adalah pengukuran yang menggunakan berat dan tinggi badan untuk mengestimasikan jumlah lemak tubuh. IMT yang lebih tinggi dikaitkan dengan risiko yang lebih besar untuk berbagai penyakit.",
                            idealValue = "18.5 - 24.9 kg/m²",
                            currentValue = "${latestPrediction.bmi ?: "0"} kg/m²"
                        ),
                        RiskFactorDetails(
                            name = "HTN",
                            fullName = "Hipertensi",
                            impactPercentage = (latestPrediction.isHypertensionContribution?.toFloatOrNull() ?: 0f) * 100f,
                            description = "Hipertensi atau tekanan darah tinggi adalah kondisi medis kronis dengan tekanan darah di arteri meningkat. Tanpa pengobatan, hipertensi meningkatkan risiko penyakit jantung dan stroke.",
                            idealValue = "< 120/80 mmHg",
                            currentValue = if (latestPrediction.isHypertension == "1") "Ya" else "Tidak"
                        ),
                        RiskFactorDetails(
                            name = "RK",
                            fullName = "Riwayat Kelahiran",
                            impactPercentage = (latestPrediction.isMacrosomicBabyContribution?.toFloatOrNull() ?: 0f) * 100f,
                            description = "Faktor riwayat kelahiran termasuk berat badan lahir, kelahiran prematur, atau komplikasi kelahiran lainnya yang dapat memengaruhi risiko kesehatan di masa depan.",
                            idealValue = "Berat lahir normal, kelahiran cukup bulan",
                            currentValue = if (latestPrediction.isMacrosomicBaby == "1") "Ya" else "Tidak"
                        ),
                        RiskFactorDetails(
                            name = "AF",
                            fullName = "Aktivitas Fisik",
                            impactPercentage = (latestPrediction.physicalActivityMinutesContribution?.toFloatOrNull() ?: 0f) * 100f,
                            description = "Aktivitas fisik mengacu pada tingkat olahraga dan gerakan fisik yang dilakukan secara rutin. Aktivitas fisik yang cukup membantu mengurangi risiko berbagai penyakit kronis.",
                            idealValue = "Min. 150 menit aktivitas sedang per minggu",
                            currentValue = "${latestPrediction.physicalActivityMinutes ?: "0"} menit per minggu"
                        ),
                        RiskFactorDetails(
                            name = "U",
                            fullName = "Usia",
                            impactPercentage = (latestPrediction.ageContribution?.toFloatOrNull() ?: 0f) * 100f,
                            description = "Usia adalah faktor risiko yang tidak dapat dimodifikasi namun memiliki pengaruh signifikan terhadap risiko kesehatan. Risiko berbagai penyakit meningkat seiring bertambahnya usia.",
                            idealValue = "-",
                            currentValue = "${latestPrediction.age ?: "0"} tahun",
                            isModifiable = false
                        ),
                        RiskFactorDetails(
                            name = "IM",
                            fullName = "Indeks Merokok",
                            impactPercentage = (latestPrediction.brinkmanScoreContribution?.toFloatOrNull() ?: 0f) * 100f,
                            description = "Indeks Merokok mengukur kebiasaan merokok seseorang termasuk jumlah dan durasi merokok. Merokok meningkatkan risiko berbagai penyakit kardiovaskular dan kanker.",
                            idealValue = "0 (tidak merokok)",
                            currentValue = "${latestPrediction.brinkmanScore ?: "0"} batang per hari"
                        )
                    )
                }
            }
        }
    }

    private fun collectProfile() {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)

            profileRepository.getProfile().collect { profile ->
                _profileState.value = profileState.value.copy(isLoading = false)

                profile?.let { userProfile ->
                    _bmiValueState.value = userProfile.bmi ?: "0.0"
                    _weightValueState.value = userProfile.weight ?: "0"
                    _heightValueState.value = userProfile.height ?: "0"
                    _isHypertensionState.value = userProfile.hypertension.toString() ?: "false"
                    _isMacrosomicBabyState.value = userProfile.macrosomicBaby.toString() ?: "false"
                }
            }
        }
    }

    private fun collectActivityToday() {
        viewModelScope.launch {
            _activityTodayState.value = activityTodayState.value.copy(isLoading = true)

            activityRepository.getActivityToday().collect { activity ->
                _activityTodayState.value = activityTodayState.value.copy(isLoading = false)

                activity?.let { todayActivity ->
                    _smokeValueState.value = todayActivity.smokingValue ?: "0"
                    _physicalActivityValueState.value = todayActivity.workoutValue ?: "0"
                }
            }
        }
    }

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }

    val lowRiskColor = Color(0xFF8BC34A)    // Green
    val mediumRiskColor = Color(0xFFFFC107) // Yellow
    val highRiskColor = Color(0xFFFA821F)   // Orange
    val veryHighRiskColor = Color(0xFFF44336) // Red

    data class RiskFactor(
        val name: String,
        val abbreviation: String,
        val percentage: Float
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
}