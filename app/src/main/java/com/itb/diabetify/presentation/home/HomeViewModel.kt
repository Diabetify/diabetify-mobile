package com.itb.diabetify.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.repository.PredictionRepository
import com.itb.diabetify.domain.usecases.activity.ActivityUseCases
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCases
import com.itb.diabetify.domain.usecases.profile.ProfileUseCases
import com.itb.diabetify.domain.usecases.user.UserUseCases
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val activityUseCases: ActivityUseCases,
    private val predictionUseCases: PredictionUseCases,
    private val profileUseCases: ProfileUseCases,
    private val predictionRepository: PredictionRepository,
): ViewModel() {
    // Navigation, Error, and Success States
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage

    // Operational States
    private var _userState = mutableStateOf(DataState())
    val userState: State<DataState> = _userState

    private var _activityTodayState = mutableStateOf(DataState())
    val activityTodayState: State<DataState> = _activityTodayState

    private var _latestPredictionState = mutableStateOf(DataState())
    val latestPredictionState: State<DataState> = _latestPredictionState

    private var _profileState = mutableStateOf(DataState())
    val profileState: State<DataState> = _profileState

    // UI States
    private val _userName = mutableStateOf("User")
    val userName: State<String> = _userName

    private val _lastPredictionAt = mutableStateOf("Belum ada prediksi")
    val lastPredictionAt: State<String> = _lastPredictionAt

    private val _latestPredictionScoreState = mutableDoubleStateOf(0.0)
    val latestPredictionScoreState: State<Double> = _latestPredictionScoreState

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

    private val _riskFactorDetails = mutableStateOf(listOf(
        RiskFactorDetails(
            name = "IMT",
            fullName = "Indeks Massa Tubuh",
            impactPercentage = 0.0,
            explanation = "",
            idealValue = "18.5 - 24.9 kg/m²",
            currentValue = "0 kg/m²"
        ),
        RiskFactorDetails(
            name = "H",
            fullName = "Hipertensi",
            impactPercentage = 0.0,
            explanation = "",
            idealValue = "< 120/80 mmHg",
            currentValue = "0/0 mmHg"
        ),
        RiskFactorDetails(
            name = "RBM",
            fullName = "Riwayat Bayi Makrosomia",
            impactPercentage = 0.0,
            explanation = "",
            idealValue = "Berat lahir normal, kelahiran cukup bulan",
            currentValue = "-"
        ),
        RiskFactorDetails(
            name = "AF",
            fullName = "Aktivitas Fisik",
            impactPercentage = 0.0,
            explanation = "",
            idealValue = "Min. 150 menit aktivitas sedang per minggu",
            currentValue = "0 menit"
        ),
        RiskFactorDetails(
            name = "U",
            fullName = "Usia",
            impactPercentage = 0.0,
            explanation = "",
            idealValue = "-",
            currentValue = "0 tahun",
            isModifiable = false
        ),
        RiskFactorDetails(
            name = "SM",
            fullName = "Status Merokok",
            impactPercentage = 0.0,
            explanation = "",
            idealValue = "0 (tidak merokok)",
            currentValue = "0 batang per hari"
        ),
        RiskFactorDetails(
            name = "IB",
            fullName = "Indeks Brinkman",
            impactPercentage = 0.0,
            explanation = "",
            idealValue = "0 (tidak merokok)",
            currentValue = "0 batang per hari"
        ),
        RiskFactorDetails(
            name = "RK",
            fullName = "Riwayat Keluarga",
            impactPercentage = 0.0,
            explanation = "",
            idealValue = "Tidak ada riwayat penyakit serius",
            currentValue = "-"
        ),
        RiskFactorDetails(
            name = "K",
            fullName = "Kolesterol",
            impactPercentage = 0.0,
            explanation = "",
            idealValue = "< 200 mg/dL",
            currentValue = "0 mg/dL"
        )
    ))
    val riskFactorDetails: State<List<RiskFactorDetails>> = _riskFactorDetails

    private val _bmi = mutableDoubleStateOf(0.0)
    val bmi: State<Double> = _bmi

    private val _weight = mutableIntStateOf(0)
    val weight: State<Int> = _weight

    private val _height = mutableIntStateOf(0)
    val height: State<Int> = _height

    private val _isHypertension = mutableStateOf(false)
    val isHypertension: State<Boolean> = _isHypertension

    private val _macrosomicBaby = mutableIntStateOf(0)
    val macrosomicBaby: State<Int> = _macrosomicBaby

    private val _smoke = mutableStateOf("0")
    val smoke: State<String> = _smoke

    private val _physicalActivity = mutableStateOf("0")
    val physicalActivity: State<String> = _physicalActivity

    private val _isBloodline = mutableStateOf(false)
    val isBloodline: State<Boolean> = _isBloodline

    private val _isCholesterol = mutableStateOf(false)
    val isCholesterol: State<Boolean> = _isCholesterol

    private val _brinkmanIndex = mutableStateOf("0.0")
    val brinkmanIndex: State<String> = _brinkmanIndex

    private val _smokingStatus = mutableStateOf("0")
    val smokingStatus: State<String> = _smokingStatus

    private val _physicalActivityAverage = mutableStateOf("0")
    val physicalActivityAverage: State<String> = _physicalActivityAverage

    // Initialization
    init {
        loadUserData()
        loadLatestPredictionData()
        loadActivityTodayData()
        loadProfileData()
    }

    // Use Case Calls
    private fun loadUserData() {
        viewModelScope.launch {
            _userState.value = userState.value.copy(isLoading = true)

            val getUserResult = userUseCases.getUser()

            _userState.value = userState.value.copy(isLoading = false)

            when (getUserResult.result) {
                is Resource.Success -> {
                    collectUserData()
                }
                is Resource.Error -> {
                    _errorMessage.value = getUserResult.result.message ?: "Terjadi kesalahan saat mengambil data pengguna"
                    getUserResult.result.message?.let { Log.e("HomeViewModel", it) }
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat mengambil data pengguna"
                    Log.e("HomeViewModel", "Unexpected error")
                }
            }
        }
    }

    private fun collectUserData() {
        viewModelScope.launch {
            _userState.value = userState.value.copy(isLoading = true)

            userUseCases.getUserRepository().onEach { user ->
                _userState.value = userState.value.copy(isLoading = false)

                user?.let {
                    _userName.value = it.name
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun loadLatestPredictionData() {
        viewModelScope.launch {
            _latestPredictionState.value = latestPredictionState.value.copy(isLoading = true)

            val getLatestPredictionResult = predictionUseCases.getLatestPrediction()

            _latestPredictionState.value = latestPredictionState.value.copy(isLoading = false)

            when (getLatestPredictionResult.result) {
                is Resource.Success -> {
                    collectLatestPredictionData()
                }
                is Resource.Error -> {
                    _errorMessage.value = getLatestPredictionResult.result.message ?: "Terjadi kesalahan saat mengambil data prediksi terbaru"
                    getLatestPredictionResult.result.message?.let { Log.e("HomeViewModel", it) }
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat mengambil data prediksi terbaru"
                    Log.e("HomeViewModel", "Unexpected error loading latest prediction data")
                }
            }
        }
    }

    private fun collectLatestPredictionData() {
        viewModelScope.launch {
            _latestPredictionState.value = latestPredictionState.value.copy(isLoading = true)

            predictionRepository.getLatestPrediction().onEach { prediction ->
                _latestPredictionState.value = latestPredictionState.value.copy(isLoading = false)

                if (prediction == null) {
                    resetToDefaultValues()
                    return@onEach
                }

                prediction.let { latestPrediction ->
                    _lastPredictionAt.value = latestPrediction.createdAt
                    _latestPredictionScoreState.doubleValue = latestPrediction.riskScore

                    _riskFactors.value = listOf(
                        RiskFactor("Indeks Massa Tubuh", "IMT", latestPrediction.bmiContribution),
                        RiskFactor("Hipertensi", "H", latestPrediction.isHypertensionContribution),
                        RiskFactor("Riwayat Bayi Makrosomia", "RBM", latestPrediction.isMacrosomicBabyContribution),
                        RiskFactor("Aktivitas Fisik", "AF", latestPrediction.physicalActivityFrequencyContribution),
                        RiskFactor("Usia", "U", latestPrediction.ageContribution),
                        RiskFactor("Status Merokok", "SM", latestPrediction.smokingStatusContribution),
                        RiskFactor("Indeks Brinkman", "IB", latestPrediction.brinkmanScoreContribution),
                        RiskFactor("Riwayat Keluarga", "RK", latestPrediction.isBloodlineContribution),
                        RiskFactor("Kolesterol", "K", latestPrediction.isCholesterolContribution)
                    )

                    _riskFactorDetails.value = listOf(
                        RiskFactorDetails(
                            name = "IMT",
                            fullName = "Indeks Massa Tubuh",
                            impactPercentage = latestPrediction.bmiContribution,
                            explanation = latestPrediction.bmiExplanation,
                            idealValue = "18.5 - 24.9 kg/m²",
                            currentValue = "${latestPrediction.bmi} kg/m²"
                        ),
                        RiskFactorDetails(
                            name = "H",
                            fullName = "Hipertensi",
                            impactPercentage = latestPrediction.isHypertensionContribution,
                            explanation = latestPrediction.isHypertensionExplanation,
                            idealValue = "< 120/80 mmHg",
                            currentValue = if (latestPrediction.isHypertension) "Ya" else "Tidak"
                        ),
                        RiskFactorDetails(
                            name = "RBM",
                            fullName = "Riwayat Bayi Makrosomia",
                            impactPercentage = latestPrediction.isMacrosomicBabyContribution,
                            explanation = latestPrediction.isMacrosomicBabyExplanation,
                            idealValue = "Berat lahir normal, kelahiran cukup bulan",
                            currentValue = when (latestPrediction.isMacrosomicBaby) {
                                1 -> "Ya"
                                else -> "Tidak"
                            }
                        ),
                        RiskFactorDetails(
                            name = "AF",
                            fullName = "Aktivitas Fisik",
                            impactPercentage = latestPrediction.physicalActivityFrequencyContribution,
                            explanation = latestPrediction.physicalActivityFrequencyExplanation,
                            idealValue = "Min. 150 menit aktivitas sedang per minggu",
                            currentValue = "${latestPrediction.physicalActivityFrequency} menit per minggu"
                        ),
                        RiskFactorDetails(
                            name = "U",
                            fullName = "Usia",
                            impactPercentage = latestPrediction.ageContribution,
                            explanation = latestPrediction.ageExplanation,
                            idealValue = "-",
                            currentValue = "${latestPrediction.age} tahun",
                            isModifiable = false
                        ),
                        RiskFactorDetails(
                            name = "SM",
                            fullName = "Status Merokok",
                            impactPercentage = latestPrediction.smokingStatusContribution,
                            explanation = latestPrediction.smokingStatusExplanation,
                            idealValue = "0 (tidak merokok)",
                            currentValue = "${latestPrediction.smokingStatus} batang per hari"
                        ),
                        RiskFactorDetails(
                            name = "IB",
                            fullName = "Indeks Brinkman",
                            impactPercentage = latestPrediction.brinkmanScoreContribution,
                            explanation = latestPrediction.brinkmanScoreExplanation,
                            idealValue = "0 (tidak merokok)",
                            currentValue = "${latestPrediction.brinkmanScore}"
                        ),
                        RiskFactorDetails(
                            name = "RK",
                            fullName = "Riwayat Keluarga",
                            impactPercentage = latestPrediction.isBloodlineContribution,
                            explanation = latestPrediction.isBloodlineExplanation,
                            idealValue = "Tidak ada riwayat penyakit serius",
                            currentValue = if (latestPrediction.isBloodline) "Ya" else "Tidak"
                        ),
                        RiskFactorDetails(
                            name = "K",
                            fullName = "Kolesterol",
                            impactPercentage = latestPrediction.isCholesterolContribution,
                            explanation = latestPrediction.isCholesterolExplanation,
                            idealValue = "< 200 mg/dL",
                            currentValue = if (latestPrediction.isCholesterol) "Ya" else "Tidak"
                        )
                    )

                    _brinkmanIndex.value = latestPrediction.brinkmanScore.toString()
                    _smokingStatus.value = latestPrediction.smokingStatus
                    _physicalActivityAverage.value = latestPrediction.physicalActivityFrequency.toString()
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)

            val getProfileResult = profileUseCases.getProfile()

            _profileState.value = profileState.value.copy(isLoading = false)

            when (getProfileResult.result) {
                is Resource.Success -> {
                    collectProfile()
                }
                is Resource.Error -> {
                    if (getProfileResult.result.message?.contains("404") == true) {
                        Log.d("HomeViewModel", "Profile not found, navigating to survey screen")
                        resetToDefaultValues()
                        _navigationEvent.value = "SURVEY_SCREEN"
                    } else {
                        _errorMessage.value = getProfileResult.result.message ?: "Terjadi kesalahan saat mengambil data profil"
                        getProfileResult.result.message?.let { Log.e("HomeViewModel", it) }
                    }
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat mengambil data profil"
                    Log.e("HomeViewModel", "Unexpected error loading profile data")
                }
            }
        }
    }


    private fun collectProfile() {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)

            profileUseCases.getProfileRepository().onEach { profile ->
                _profileState.value = profileState.value.copy(isLoading = false)

                profile?.let { userProfile ->
                    _bmi.value = userProfile.bmi
                    _weight.value = userProfile.weight
                    _height.value = userProfile.height
                    _isHypertension.value = userProfile.hypertension
                    _macrosomicBaby.value = userProfile.macrosomicBaby
                    _isBloodline.value = userProfile.bloodline
                    _isCholesterol.value = userProfile.cholesterol
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun loadActivityTodayData() {
        viewModelScope.launch {
            _activityTodayState.value = activityTodayState.value.copy(isLoading = true)

            val getActivityTodayResult = activityUseCases.getActivityToday()

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

    private fun resetToDefaultValues() {
        _latestPredictionScoreState.value = 0.0
        _bmi.value = 0.0
        _weight.value = 0
        _height.value = 0
        _isHypertension.value = false
        _macrosomicBaby.value = 0
        _smoke.value = "0"
        _physicalActivity.value = "0"
        _isBloodline.value = false
        _isCholesterol.value = false
        
        _riskFactors.value = _riskFactors.value.map { it.copy(percentage = 0.0) }
        
        _riskFactorDetails.value = _riskFactorDetails.value.map {
            it.copy(impactPercentage = 0.0, currentValue = when(it.name) {
                "IMT" -> "0 kg/m²"
                "H" -> "0/0 mmHg"
                "RBM" -> "-"
                "AF" -> "0 menit"
                "U" -> "0 tahun"
                "SM" -> "0 batang per hari"
                "IB" -> "0 batang per hari"
                "RK" -> "-"
                "K" -> "0 mg/dL"
                else -> "0"
            })
        }
    }

    private fun collectActivityToday() {
        viewModelScope.launch {
            _activityTodayState.value = activityTodayState.value.copy(isLoading = true)

            activityUseCases.getActivityRepository().collect { activity ->
                _activityTodayState.value = activityTodayState.value.copy(isLoading = false)

                activity?.let { todayActivity ->
                    _smoke.value = todayActivity.smokingValue ?: "0"
                    _physicalActivity.value = todayActivity.workoutValue ?: "0"
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
        val percentage: Double
    )

    data class RiskFactorDetails(
        val name: String,
        val fullName: String,
        val impactPercentage: Double,
        val explanation: String,
        val idealValue: String,
        val currentValue: String,
        val isModifiable: Boolean = true
    )
}