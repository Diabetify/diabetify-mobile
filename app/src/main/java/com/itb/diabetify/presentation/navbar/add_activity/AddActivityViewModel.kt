package com.itb.diabetify.presentation.navbar.add_activity

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.model.activity.AddActivityResult
import com.itb.diabetify.domain.model.activity.UpdateActivityResult
import com.itb.diabetify.domain.usecases.activity.ActivityUseCases
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCases
import com.itb.diabetify.domain.usecases.profile.ProfileUseCases
import com.itb.diabetify.domain.usecases.user.UserUseCases
import com.itb.diabetify.presentation.common.FieldState
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.ZoneOffset
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class AddActivityViewModel @Inject constructor(
    private val activityUseCases: ActivityUseCases,
    private val profileUseCases: ProfileUseCases,
    private val predictionUseCases: PredictionUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {
    // Error and Success States
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private var _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage

    // Operational States
    private var _activityTodayState = mutableStateOf(DataState())
    val activityTodayState: State<DataState> = _activityTodayState

    private var _profileState = mutableStateOf(DataState())
    val profileState: State<DataState> = _profileState

    private var _addActivityState = mutableStateOf(DataState())
    val addActivityState: State<DataState> = _addActivityState

    private var _updateActivityState = mutableStateOf(DataState())
    val updateActivityState: State<DataState> = _updateActivityState

    private var _updateProfileState = mutableStateOf(DataState())
    val updateProfileState: State<DataState> = _updateProfileState

    private val _userState = mutableStateOf(DataState())
    val userState: State<DataState> = _userState

    // Field States
    private val _smokeValueState = mutableStateOf(FieldState())
    val smokeValueState: State<FieldState> = _smokeValueState

    private val _workoutValueState = mutableStateOf(FieldState())
    val workoutValueState: State<FieldState> = _workoutValueState

    private val _weightValueState = mutableStateOf(FieldState())
    val weightValueState: State<FieldState> = _weightValueState

    private val _heightValueState = mutableStateOf(FieldState())
    val heightValueState: State<FieldState> = _heightValueState

    private val _birthValueState = mutableStateOf(FieldState())
    val birthValueState: State<FieldState> = _birthValueState

    private val _hypertensionValueState = mutableStateOf(FieldState())
    val hypertensionValueState: State<FieldState> = _hypertensionValueState

    private val _systolicValueState = mutableStateOf(FieldState())
    val systolicValueState: State<FieldState> = _systolicValueState

    private val _diastolicValueState = mutableStateOf(FieldState())
    val diastolicValueState: State<FieldState> = _diastolicValueState

    private val _bloodlineValueState = mutableStateOf(FieldState())
    val bloodlineValueState: State<FieldState> = _bloodlineValueState

    private val _cholesterolValueState = mutableStateOf(FieldState())
    val cholesterolValueState: State<FieldState> = _cholesterolValueState

    // Other States
    private val _userGender = mutableStateOf<String?>(null)
    val userGender: State<String?> = _userGender

    private val _smokingId = mutableStateOf<String?>(null)
    val smokingId: State<String?> = _smokingId

    private val _workoutId = mutableStateOf<String?>(null)
    val workoutId: State<String?> = _workoutId

    @SuppressLint("NewApi")
    val activityDate = ZonedDateTime.now(ZoneOffset.UTC).toString()

    // Initialization
    init {
        collectActivityTodayData()
        collectProfileData()
        collectUserData()
    }

    // Validation Function
    private fun validateField(fieldType: String, value: String): String? {
        if (value.isBlank()) {
            return "Mohon isi field ini"
        }

        when (fieldType) {
            "smoke" -> {
                val numericValue = value.toIntOrNull() ?: return "Harap masukkan angka yang valid"
                if (numericValue < 0 || numericValue > 60) {
                    return "Jumlah rokok harus antara 0-60 batang"
                }
            }
            "weight" -> {
                val numericValue = value.toIntOrNull() ?: return "Harap masukkan angka yang valid"
                if (numericValue < 30 || numericValue > 300) {
                    return "Berat badan harus antara 30-300 kg"
                }
            }
            "height" -> {
                val numericValue = value.toIntOrNull() ?: return "Harap masukkan angka yang valid"
                if (numericValue < 100 || numericValue > 250) {
                    return "Tinggi badan harus antara 100-250 cm"
                }
            }
            "birth" -> {
                val numericValue = value.toIntOrNull() ?: return "Harap masukkan angka yang valid"
                if (numericValue < 0 || numericValue > 10) {
                    return "Jumlah bayi makrosomik harus antara 0-10"
                }
            }
            "systolic" -> {
                val numericValue = value.toIntOrNull() ?: return "Harap masukkan angka yang valid"
                if (numericValue < 70 || numericValue > 250) {
                    return "Tekanan sistolik harus antara 70-250 mmHg"
                }
            }
            "diastolic" -> {
                val numericValue = value.toIntOrNull() ?: return "Harap masukkan angka yang valid"
                if (numericValue < 40 || numericValue > 150) {
                    return "Tekanan diastolik harus antara 40-150 mmHg"
                }
            }
            "workout" -> {
                if (value != "true" && value != "false") {
                    return "Pilihan workout tidak valid"
                }
            }
            "hypertension", "bloodline", "cholesterol" -> {
                if (value != "true" && value != "false") {
                    return "Pilihan tidak valid"
                }
            }
        }
        
        return null
    }

    // Setters for Field States
    fun setSmokeValue(value: String) {
        val validationError = validateField("smoke", value)
        _smokeValueState.value = smokeValueState.value.copy(
            text = value,
            error = validationError
        )
    }

    fun setWorkoutValue(value: String) {
        val validationError = validateField("workout", value)
        _workoutValueState.value = workoutValueState.value.copy(
            text = value,
            error = validationError
        )
    }

    fun setWeightValue(value: String) {
        val validationError = validateField("weight", value)
        _weightValueState.value = weightValueState.value.copy(
            text = value,
            error = validationError
        )
    }

    fun setHeightValue(value: String) {
        val validationError = validateField("height", value)
        _heightValueState.value = heightValueState.value.copy(
            text = value,
            error = validationError
        )
    }

    fun setBirthValue(value: String) {
        val validationError = validateField("birth", value)
        _birthValueState.value = birthValueState.value.copy(
            text = value,
            error = validationError
        )
    }

    fun setHypertensionValue(value: String) {
        val validationError = validateField("hypertension", value)
        _hypertensionValueState.value = hypertensionValueState.value.copy(
            text = value,
            error = validationError
        )
    }

    fun setSystolicValue(value: String) {
        val validationError = validateField("systolic", value)
        _systolicValueState.value = systolicValueState.value.copy(
            text = value,
            error = validationError
        )
    }

    fun setDiastolicValue(value: String) {
        val validationError = validateField("diastolic", value)
        _diastolicValueState.value = diastolicValueState.value.copy(
            text = value,
            error = validationError
        )
    }

    fun setBloodlineValue(value: String) {
        val validationError = validateField("bloodline", value)
        _bloodlineValueState.value = bloodlineValueState.value.copy(
            text = value,
            error = validationError
        )
    }

    fun setCholesterolValue(value: String) {
        val validationError = validateField("cholesterol", value)
        _cholesterolValueState.value = cholesterolValueState.value.copy(
            text = value,
            error = validationError
        )
    }

    // Use Case Calls
    private fun collectUserData() {
        viewModelScope.launch {
            _userState.value = userState.value.copy(isLoading = true)

            userUseCases.getUserRepository().onEach { user ->
                _userState.value = userState.value.copy(isLoading = false)
                _userGender.value = user?.gender
                
                user?.let {
                    Log.d("AddActivityViewModel", "User gender collected: ${it.gender}")
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun collectActivityTodayData() {
        viewModelScope.launch {
            _activityTodayState.value = DataState(isLoading = true)

            activityUseCases.getActivityRepository().onEach { activity ->
                _activityTodayState.value = activityTodayState.value.copy(isLoading = false)

                activity?.let {
                    _smokingId.value = it.smokingId
                    _workoutId.value = it.workoutId
                    _smokeValueState.value = FieldState(
                        text = it.smokingValue ?: "0",
                        error = null
                    )
                    _workoutValueState.value = FieldState(
                        text = if ((it.workoutValue ?: "0") == "0") "false" else "true",
                        error = null
                    )
                }

                Log.d("AddActivityViewModel", "Activity data collected: $activity")
            }.launchIn(viewModelScope)
        }
    }

    private fun collectProfileData() {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)

            profileUseCases.getProfileRepository().onEach { profile ->
                _profileState.value = profileState.value.copy(isLoading = false)

                profile?.let {
                    _weightValueState.value = FieldState(
                        text = it.weight.toString(),
                        error = null
                    )
                    _heightValueState.value = FieldState(
                        text = it.height.toString(),
                        error = null
                    )
                    val birthValue = (it.macrosomicBaby).toString()
                    _birthValueState.value = FieldState(
                        text = birthValue,
                        error = null
                    )
                    _hypertensionValueState.value = FieldState(
                        text = it.hypertension.toString(),
                        error = null
                    )
                    _bloodlineValueState.value = FieldState(
                        text = it.bloodline.toString(),
                        error = null
                    )
                    _cholesterolValueState.value = FieldState(
                        text = it.cholesterol.toString(),
                        error = null
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    @SuppressLint("NewApi")
    fun handleSmoking() {
        // Validate smoke value before processing
        val smokeValueText = smokeValueState.value.text
        val validationError = validateField("smoke", smokeValueText)
        
        if (validationError != null) {
            _smokeValueState.value = smokeValueState.value.copy(error = validationError)
            _errorMessage.value = validationError
            return
        }

        val smokingIdValue = smokingId.value
        val smokeValue = smokeValueText.toIntOrNull() ?: 0

        if (smokingIdValue != null) {
            updateSmokingActivity(smokingIdValue, smokeValue)
        } else {
            addSmokingActivity(smokeValue)
        }
    }

    @SuppressLint("NewApi")
    fun handleWorkout() {
        val workoutValueText = workoutValueState.value.text
        val validationError = validateField("workout", workoutValueText)
        
        if (validationError != null) {
            _workoutValueState.value = workoutValueState.value.copy(error = validationError)
            _errorMessage.value = validationError
            return
        }

        val workoutIdValue = workoutId.value
        val workoutValue = if (workoutValueText.lowercase() == "true" || workoutValueText == "1") 1 else 0

        if (workoutIdValue != null) {
            updateWorkoutActivity(workoutIdValue, workoutValue)
        } else {
            addWorkoutActivity(workoutValue)
        }
    }

    @SuppressLint("NewApi")
    private fun addSmokingActivity(value: Int) {
        viewModelScope.launch {
            _addActivityState.value = addActivityState.value.copy(isLoading = true)

            val addActivityResult = activityUseCases.addActivity(
                activityDate = activityDate,
                activityType = "smoke",
                value = value
            )

            _addActivityState.value = addActivityState.value.copy(isLoading = false)

            handleActivityResult(addActivityResult, "add smoking activity")
        }
    }

    @SuppressLint("NewApi")
    private fun addWorkoutActivity(value: Int) {
        viewModelScope.launch {
            _addActivityState.value = addActivityState.value.copy(isLoading = true)

            val addActivityResult = activityUseCases.addActivity(
                activityDate = activityDate,
                activityType = "workout",
                value = value
            )

            _addActivityState.value = addActivityState.value.copy(isLoading = false)

            handleActivityResult(addActivityResult, "add workout activity")
        }
    }

    private fun updateSmokingActivity(activityId: String, value: Int) {
        viewModelScope.launch {
            _updateActivityState.value = updateActivityState.value.copy(isLoading = true)

            val updateActivityResult = activityUseCases.updateActivity(
                activityId = activityId,
                activityDate = activityDate,
                activityType = "smoke",
                value = value
            )

            _updateActivityState.value = updateActivityState.value.copy(isLoading = false)

            handleActivityResult(updateActivityResult, "update smoking activity")
        }
    }

    private fun updateWorkoutActivity(activityId: String, value: Int) {
        viewModelScope.launch {
            _updateActivityState.value = updateActivityState.value.copy(isLoading = true)

            val updateActivityResult = activityUseCases.updateActivity(
                activityId = activityId,
                activityDate = activityDate,
                activityType = "workout",
                value = value
            )

            _updateActivityState.value = updateActivityState.value.copy(isLoading = false)

            handleActivityResult(updateActivityResult, "update workout activity")
        }
    }

    private fun handleActivityResult(result: Any?, operationType: String) {
        val resourceResult = when (result) {
            is AddActivityResult -> result.result
            is UpdateActivityResult -> result.result
            else -> null
        }

        when (resourceResult) {
            is Resource.Success -> {
                Log.d("AddActivityViewModel", "Successfully performed: $operationType")
                triggerPredictionUpdate()
            }
            is Resource.Error -> {
                _errorMessage.value = resourceResult.message ?: "Unknown error occurred"
                Log.d("AddActivityViewModel", "Failed to $operationType: ${resourceResult.message}")
            }
            is Resource.Loading -> {
                Log.d("AddActivityViewModel", "Loading: $operationType")
            }
            else -> {
                _errorMessage.value = "Unknown error occurred"
                Log.d("AddActivityViewModel", "Unexpected error during: $operationType")
            }
        }
    }

    private fun triggerPredictionUpdate() {
        viewModelScope.launch {
            val predictionResult = predictionUseCases.predict()
            when (predictionResult.result) {
                is Resource.Success -> {
                    _successMessage.value = "Data updated successfully"
                    Log.d("AddActivityViewModel", "Prediction updated successfully")
                }
                is Resource.Error -> {
                    _errorMessage.value = predictionResult.result.message ?: "Failed to update prediction"
                    Log.d("AddActivityViewModel", "Failed to update prediction: ${predictionResult.result.message}")
                }
                else -> {
                    _errorMessage.value = "Unknown error occurred during prediction"
                    Log.d("AddActivityViewModel", "Unknown error during prediction")
                }
            }
        }
    }

    fun updateProfile(type: String) {
        viewModelScope.launch {
            val validationErrors = mutableListOf<String>()
            
            val weightError = validateField("weight", weightValueState.value.text)
            if (weightError != null) {
                _weightValueState.value = weightValueState.value.copy(error = weightError)
                validationErrors.add("Berat badan: $weightError")
            }
            
            val heightError = validateField("height", heightValueState.value.text)
            if (heightError != null) {
                _heightValueState.value = heightValueState.value.copy(error = heightError)
                validationErrors.add("Tinggi badan: $heightError")
            }
            
            val hypertensionError = validateField("hypertension", hypertensionValueState.value.text)
            if (hypertensionError != null) {
                _hypertensionValueState.value = hypertensionValueState.value.copy(error = hypertensionError)
                validationErrors.add("Hipertensi: $hypertensionError")
            }
            
            val birthError = validateField("birth", birthValueState.value.text)
            if (birthError != null) {
                _birthValueState.value = birthValueState.value.copy(error = birthError)
                validationErrors.add("Bayi makrosomik: $birthError")
            }
            
            val bloodlineError = validateField("bloodline", bloodlineValueState.value.text)
            if (bloodlineError != null) {
                _bloodlineValueState.value = bloodlineValueState.value.copy(error = bloodlineError)
                validationErrors.add("Riwayat keluarga: $bloodlineError")
            }
            
            val cholesterolError = validateField("cholesterol", cholesterolValueState.value.text)
            if (cholesterolError != null) {
                _cholesterolValueState.value = cholesterolValueState.value.copy(error = cholesterolError)
                validationErrors.add("Kolesterol: $cholesterolError")
            }
            
            if (validationErrors.isNotEmpty()) {
                _errorMessage.value = "Harap perbaiki kesalahan berikut:\n${validationErrors.joinToString("\n")}"
                return@launch
            }

            _updateProfileState.value = updateProfileState.value.copy(isLoading = true)

            val weight = weightValueState.value.text
            val height = heightValueState.value.text
            val hypertension = hypertensionValueState.value.text.toBoolean()
            val macrosomicBaby = birthValueState.value.text.toInt()
            val bloodline = bloodlineValueState.value.text.toBoolean()
            val cholesterol = cholesterolValueState.value.text.toBoolean()

            val updateProfileResult = when (type) {
                "weight", "height", "hypertension", "birth", "bloodline", "cholesterol" -> {
                    profileUseCases.updateProfile(
                        weight = weight,
                        height = height,
                        hypertension = hypertension,
                        macrosomicBaby = macrosomicBaby,
                        bloodline = bloodline,
                        cholesterol = cholesterol
                    )
                }
                else -> {
                    _errorMessage.value = "Invalid profile update type"
                    Log.d("AddActivityViewModel", "Invalid profile update type")
                    null
                }
            }

            _updateProfileState.value = updateProfileState.value.copy(isLoading = false)

            when (updateProfileResult?.result) {
                is Resource.Success -> {
                    Log.d("AddActivityViewModel", "Profile updated successfully")
                    triggerPredictionUpdate()
                }
                is Resource.Error -> {
                    _errorMessage.value = updateProfileResult.result.message ?: "Unknown error occurred"
                    updateProfileResult.result.message?.let { Log.d("AddActivityViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("AddActivityViewModel", "Loading")
                }
                else -> {
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("AddActivityViewModel", "Unexpected error")
                }
            }
        }
    }

    // Validation Helper Functions
    fun isFieldValid(fieldType: String): Boolean {
        val fieldState = when (fieldType) {
            "smoke" -> smokeValueState.value
            "workout" -> workoutValueState.value
            "weight" -> weightValueState.value
            "height" -> heightValueState.value
            "birth" -> birthValueState.value
            "hypertension" -> hypertensionValueState.value
            "systolic" -> systolicValueState.value
            "diastolic" -> diastolicValueState.value
            "bloodline" -> bloodlineValueState.value
            "cholesterol" -> cholesterolValueState.value
            else -> return false
        }
        
        return fieldState.error == null && fieldState.text.isNotBlank()
    }

    fun canUpdateBloodPressure(): Boolean {
        return isFieldValid("systolic") && isFieldValid("diastolic")
    }

    // Helper Functions
    fun onErrorShown() {
        _errorMessage.value = null
    }

    fun onSuccessShown() {
        _successMessage.value = null
    }
}