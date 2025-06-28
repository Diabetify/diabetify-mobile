package com.itb.diabetify.presentation.navbar.add_activity

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.model.activity.AddActivityResult
import com.itb.diabetify.domain.model.activity.UpdateActivityResult
import com.itb.diabetify.domain.repository.ActivityRepository
import com.itb.diabetify.domain.repository.ProfileRepository
import com.itb.diabetify.domain.usecases.activity.AddActivityUseCase
import com.itb.diabetify.domain.usecases.activity.UpdateActivityUseCase
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCases
import com.itb.diabetify.domain.usecases.profile.ProfileUseCases
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
    private val activityRepository: ActivityRepository,
    private val profileRepository: ProfileRepository,
    private val addActivityUseCase: AddActivityUseCase,
    private val updateActivityUseCase: UpdateActivityUseCase,
    private val profileUseCases: ProfileUseCases,
    private val predictionUseCases: PredictionUseCases
) : ViewModel() {
    @SuppressLint("NewApi")
    val activityDate = ZonedDateTime.now(ZoneOffset.UTC).toString()

    private var _activityTodayState = mutableStateOf(DataState())
    val activityTodayState = _activityTodayState

    private var _profileState = mutableStateOf(DataState())
    val profileState: State<DataState> = _profileState

    private var _addActivityState = mutableStateOf(DataState())
    val addActivityState: State<DataState> = _addActivityState

    private var _updateActivityState = mutableStateOf(DataState())
    val updateActivityState: State<DataState> = _updateActivityState

    private var _updateProfileState = mutableStateOf(DataState())
    val updateProfileState: State<DataState> = _updateProfileState

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _smokingId = mutableStateOf<String?>(null)
    val smokingId: State<String?> = _smokingId

    private val _workoutId = mutableStateOf<String?>(null)
    val workoutId: State<String?> = _workoutId

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

    init {
        collectActivityTodayData()
        collectProfileData()
    }

    private fun collectActivityTodayData() {
        viewModelScope.launch {
            _activityTodayState.value = DataState(isLoading = true)

            activityRepository.getActivityToday().onEach { activity ->
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

            profileRepository.getProfile().onEach { profile ->
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
                    val frontendBirthValue = (it.macrosomicBaby).toString()
                    _birthValueState.value = FieldState(
                        text = frontendBirthValue,
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

    fun setSmokeValue(value: String) {
        _smokeValueState.value = smokeValueState.value.copy(error = null)
        _smokeValueState.value = smokeValueState.value.copy(text = value)
    }

    fun setWorkoutValue(value: String) {
        _workoutValueState.value = workoutValueState.value.copy(error = null)
        _workoutValueState.value = workoutValueState.value.copy(text = value)
    }

    fun setWeightValue(value: String) {
        _weightValueState.value = weightValueState.value.copy(error = null)
        _weightValueState.value = weightValueState.value.copy(text = value)
    }

    fun setHeightValue(value: String) {
        _heightValueState.value = heightValueState.value.copy(error = null)
        _heightValueState.value = heightValueState.value.copy(text = value)
    }

    fun setBirthValue(value: String) {
        _birthValueState.value = birthValueState.value.copy(error = null)
        _birthValueState.value = birthValueState.value.copy(text = value)
    }

    fun setHypertensionValue(value: String) {
        _hypertensionValueState.value = hypertensionValueState.value.copy(error = null)
        _hypertensionValueState.value = hypertensionValueState.value.copy(text = value)
    }

    fun setSystolicValue(value: String) {
        _systolicValueState.value = systolicValueState.value.copy(error = null)
        _systolicValueState.value = systolicValueState.value.copy(text = value)
    }

    fun setDiastolicValue(value: String) {
        _diastolicValueState.value = diastolicValueState.value.copy(error = null)
        _diastolicValueState.value = diastolicValueState.value.copy(text = value)
    }

    fun setBloodlineValue(value: String) {
        _bloodlineValueState.value = bloodlineValueState.value.copy(error = null)
        _bloodlineValueState.value = bloodlineValueState.value.copy(text = value)
    }

    fun setCholesterolValue(value: String) {
        _cholesterolValueState.value = cholesterolValueState.value.copy(error = null)
        _cholesterolValueState.value = cholesterolValueState.value.copy(text = value)
    }

    @SuppressLint("NewApi")
    fun handleSmoking() {
        val smokingIdValue = smokingId.value
        val smokeValue = smokeValueState.value.text.toIntOrNull() ?: 0

        if (smokingIdValue != null) {
            updateSmokingActivity(smokingIdValue, smokeValue)
        } else {
            addSmokingActivity(smokeValue)
        }
    }

    @SuppressLint("NewApi")
    fun handleWorkout() {
        val workoutIdValue = workoutId.value
        val workoutValue = workoutValueState.value.text.toIntOrNull() ?: 0

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

            val addActivityResult = addActivityUseCase(
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

            val addActivityResult = addActivityUseCase(
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

            val updateActivityResult = updateActivityUseCase(
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

            val updateActivityResult = updateActivityUseCase(
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
}