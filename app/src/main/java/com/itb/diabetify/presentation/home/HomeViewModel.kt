package com.itb.diabetify.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.repository.PredictionRepository
import com.itb.diabetify.domain.usecases.activity.ActivityUseCases
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCases
import com.itb.diabetify.domain.usecases.profile.ProfileUseCases
import com.itb.diabetify.domain.usecases.user.UserUseCases
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.PredictionUpdateNotifier
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

    private val _loadingMessage = mutableStateOf<String?>(null)
    val loadingMessage: State<String?> = _loadingMessage

    // Operational States
    private var _userState = mutableStateOf(DataState())
    val userState: State<DataState> = _userState

    private var _activityTodayState = mutableStateOf(DataState())
    val activityTodayState: State<DataState> = _activityTodayState

    private var _latestPredictionState = mutableStateOf(DataState())
    val latestPredictionState: State<DataState> = _latestPredictionState

    private var _explainPredictionState = mutableStateOf(DataState())
    val explainPredictionState: State<DataState> = _explainPredictionState

    private var _profileState = mutableStateOf(DataState())
    val profileState: State<DataState> = _profileState

    // UI States
    private val _userName = mutableStateOf("Pengguna")
    val userName: State<String> = _userName

    private val _lastPredictionAt = mutableStateOf("Belum ada prediksi")
    val lastPredictionAt: State<String> = _lastPredictionAt

    private val _latestPredictionScore = mutableDoubleStateOf(0.0)
    val latestPredictionScore: State<Double> = _latestPredictionScore

    data class RiskFactor(
        val name: String,
        val abbreviation: String,
        val percentage: Double
    )

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

    data class RiskFactorDetails(
        val name: String,
        val fullName: String,
        val impactPercentage: Double,
        val explanation: String,
        val idealValue: String,
        val currentValue: String,
        val isModifiable: Boolean = true
    )

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

    private val _isHypertension = mutableStateOf(false)
    val isHypertension: State<Boolean> = _isHypertension

    private val _weight = mutableIntStateOf(0)
    val weight: State<Int> = _weight

    private val _height = mutableIntStateOf(0)
    val height: State<Int> = _height

    private val _bmi = mutableDoubleStateOf(0.0)
    val bmi: State<Double> = _bmi

    private val _smokingStatus = mutableStateOf("0")
    val smokingStatus: State<String> = _smokingStatus

    private val _macrosomicBaby = mutableIntStateOf(0)
    val macrosomicBaby: State<Int> = _macrosomicBaby

    private val _isBloodline = mutableStateOf(false)
    val isBloodline: State<Boolean> = _isBloodline

    private val _isCholesterol = mutableStateOf(false)
    val isCholesterol: State<Boolean> = _isCholesterol

    private val _smokeAverage = mutableIntStateOf(0)
    val smokeAverage: State<Int> = _smokeAverage

    private val _physicalActivityAverage = mutableIntStateOf(0)
    val physicalActivityAverage: State<Int> = _physicalActivityAverage

    private val _smokeToday = mutableIntStateOf(0)
    val smoke: State<Int> = _smokeToday

    private val _physicalActivityToday = mutableIntStateOf(0)
    val physicalActivityToday: State<Int> = _physicalActivityToday

    private var isOnRiskFactorDetailScreen = false
    private var hasUnprocessedPredictionUpdate = false
    private var hasLoadedExplanationsOnce = false
    
    // Loading state tracking
    private var isUserDataLoaded = false
    private var isPredictionDataLoaded = false
    private var isActivityDataLoaded = false
    private var isProfileDataLoaded = false

    // Initialization
    init {
        _loadingMessage.value = "Memuat data..."
        loadUserData()
        loadLatestPredictionData()
        loadActivityTodayData()
        loadProfileData()
        
        PredictionUpdateNotifier.addListener {
            hasUnprocessedPredictionUpdate = true
            if (isOnRiskFactorDetailScreen) {
                loadExplanationData()
                hasUnprocessedPredictionUpdate = false
            }
        }
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
                    isUserDataLoaded = true
                    checkAllDataLoaded()
                }
                is Resource.Error -> {
                    _errorMessage.value = getUserResult.result.message ?: "Terjadi kesalahan saat mengambil data pengguna"
                    getUserResult.result.message?.let { Log.e("HomeViewModel", it) }
                    _loadingMessage.value = null
                    isUserDataLoaded = true
                    checkAllDataLoaded()
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat mengambil data pengguna"
                    Log.e("HomeViewModel", "Unexpected error")
                    _loadingMessage.value = null
                    isUserDataLoaded = true
                    checkAllDataLoaded()
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
                    isPredictionDataLoaded = true
                    checkAllDataLoaded()
                }
                is Resource.Error -> {
                    _errorMessage.value = getLatestPredictionResult.result.message ?: "Terjadi kesalahan saat mengambil data prediksi terbaru"
                    getLatestPredictionResult.result.message?.let { Log.e("HomeViewModel", it) }
                    _loadingMessage.value = null
                    isPredictionDataLoaded = true
                    checkAllDataLoaded()
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat mengambil data prediksi terbaru"
                    Log.e("HomeViewModel", "Unexpected error loading latest prediction data")
                    _loadingMessage.value = null
                    isPredictionDataLoaded = true
                    checkAllDataLoaded()
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
                    _latestPredictionScore.doubleValue = latestPrediction.riskScore

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

                    _smokingStatus.value = latestPrediction.smokingStatus
                    _smokeAverage.intValue = latestPrediction.avgSmokeCount
                    _physicalActivityAverage.intValue = latestPrediction.physicalActivityFrequency
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
                    collectProfileData()
                    isProfileDataLoaded = true
                    checkAllDataLoaded()
                }
                is Resource.Error -> {
                    if (getProfileResult.result.message?.contains("404") == true) {
                        Log.d("HomeViewModel", "Profile not found, navigating to survey screen")
                        resetToDefaultValues()
                        _navigationEvent.value = "SURVEY_SCREEN"
                        _loadingMessage.value = null
                    } else {
                        _errorMessage.value = getProfileResult.result.message ?: "Terjadi kesalahan saat mengambil data profil"
                        getProfileResult.result.message?.let { Log.e("HomeViewModel", it) }
                        _loadingMessage.value = null
                    }
                    isProfileDataLoaded = true
                    checkAllDataLoaded()
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat mengambil data profil"
                    Log.e("HomeViewModel", "Unexpected error loading profile data")
                    _loadingMessage.value = null
                    isProfileDataLoaded = true
                    checkAllDataLoaded()
                }
            }
        }
    }

    private fun collectProfileData() {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)

            profileUseCases.getProfileRepository().onEach { profile ->
                _profileState.value = profileState.value.copy(isLoading = false)

                profile?.let { userProfile ->
                    _weight.intValue = userProfile.weight
                    _height.intValue = userProfile.height
                    _bmi.doubleValue = userProfile.bmi
                    _isHypertension.value = userProfile.hypertension
                    _macrosomicBaby.intValue = userProfile.macrosomicBaby
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
                    collectActivityTodayData()
                    isActivityDataLoaded = true
                    checkAllDataLoaded()
                }
                is Resource.Error -> {
                    _errorMessage.value = getActivityTodayResult.result.message ?: "Terjadi kesalahan saat mengambil data aktivitas hari ini"
                    getActivityTodayResult.result.message?.let { Log.e("HomeViewModel", it) }
                    _loadingMessage.value = null
                    isActivityDataLoaded = true
                    checkAllDataLoaded()
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat mengambil data aktivitas hari ini"
                    Log.e("HomeViewModel", "Unexpected error")
                    _loadingMessage.value = null
                    isActivityDataLoaded = true
                    checkAllDataLoaded()
                }
            }
        }
    }

    private fun collectActivityTodayData() {
        viewModelScope.launch {
            _activityTodayState.value = activityTodayState.value.copy(isLoading = true)

            activityUseCases.getActivityRepository().onEach { activity ->
                _activityTodayState.value = activityTodayState.value.copy(isLoading = false)

                activity?.let { todayActivity ->
                    _smokeToday.intValue = todayActivity.smokingValue
                    _physicalActivityToday.intValue = todayActivity.workoutValue
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun loadExplanationData() {
        viewModelScope.launch {
            _explainPredictionState.value = explainPredictionState.value.copy(isLoading = true)

            val explainPredictionResult = predictionUseCases.explainPrediction()

            _explainPredictionState.value = explainPredictionState.value.copy(isLoading = false)

            when (explainPredictionResult.result) {
                is Resource.Success -> {
                    // Do nothing
                }
                is Resource.Error -> {
                    _errorMessage.value = explainPredictionResult.result.message ?: "Terjadi kesalahan saat memuat penjelasan"
                    explainPredictionResult.result.message?.let { Log.e("HomeViewModel", it) }
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat memuat penjelasan"
                    Log.e("HomeViewModel", "Unexpected error loading explanation data")
                }
            }
        }
    }

    fun loadRiskFactorExplanations() {
        isOnRiskFactorDetailScreen = true

        if (!hasLoadedExplanationsOnce || hasUnprocessedPredictionUpdate) {
            loadExplanationData()
            hasUnprocessedPredictionUpdate = false
            hasLoadedExplanationsOnce = true
        }
    }

    // Helper Functions
    private fun resetToDefaultValues() {
        _latestPredictionScore.doubleValue = 0.0
        _bmi.doubleValue = 0.0
        _weight.intValue = 0
        _height.intValue = 0
        _isHypertension.value = false
        _macrosomicBaby.intValue = 0
        _isBloodline.value = false
        _isCholesterol.value = false
        _smokingStatus.value = "0"
        _smokeToday.intValue = 0
        _physicalActivityToday.intValue = 0

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
        
        isUserDataLoaded = false
        isPredictionDataLoaded = false
        isActivityDataLoaded = false
        isProfileDataLoaded = false
    }

    private fun checkAllDataLoaded() {
        if (isUserDataLoaded && isPredictionDataLoaded && isActivityDataLoaded && isProfileDataLoaded) {
            _loadingMessage.value = null
            _successMessage.value = "Data berhasil dimuat"
        }
    }

    fun onRiskFactorDetailScreenExit() {
        isOnRiskFactorDetailScreen = false
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

    override fun onCleared() {
        super.onCleared()
        PredictionUpdateNotifier.removeListener {
            hasUnprocessedPredictionUpdate = true
            if (isOnRiskFactorDetailScreen) {
                loadExplanationData()
                hasUnprocessedPredictionUpdate = false
            }
        }
    }
}