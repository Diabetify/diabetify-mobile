package com.itb.diabetify.presentation.navbar.add_activity

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.repository.ActivityRepository
import com.itb.diabetify.domain.usecases.activity.AddActivityUseCase
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
    private val addActivityUseCase: AddActivityUseCase
) : ViewModel() {
    private var _activityTodayState = mutableStateOf(DataState())
    val activityTodayState = _activityTodayState

    private var _addActivityState = mutableStateOf(DataState())
    val addActivityState: State<DataState> = _addActivityState

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        loadActivityTodayData()
    }

    private fun loadActivityTodayData() {
        viewModelScope.launch {
            _activityTodayState.value = DataState(isLoading = true)

            activityRepository.getActivityToday().onEach { activity ->
                _activityTodayState.value = activityTodayState.value.copy(isLoading = false)

                activity?.let {
                    _smokeValueState.value = FieldState(
                        text = it.smokingValue ?: "0",
                        error = null
                    )
                    _workoutValueState.value = FieldState(
                        text = it.workoutValue ?: "0",
                        error = null
                    )
                }
            }.launchIn(viewModelScope)
        }
    }

    private val _smokeValueState = mutableStateOf(FieldState())
    val smokeValueState: State<FieldState> = _smokeValueState

    fun setSmokeValue(value: String) {
        _smokeValueState.value = smokeValueState.value.copy(error = null)
        _smokeValueState.value = smokeValueState.value.copy(text = value)
    }

    private val _workoutValueState = mutableStateOf(FieldState())
    val workoutValueState: State<FieldState> = _workoutValueState

    fun setWorkoutValue(value: String) {
        _workoutValueState.value = workoutValueState.value.copy(error = null)
        _workoutValueState.value = workoutValueState.value.copy(text = value)
    }

    @SuppressLint("NewApi")
    fun addActivity(type: String) {
        viewModelScope.launch {
            _addActivityState.value = addActivityState.value.copy(isLoading = true)

            val activityDate = ZonedDateTime.now(ZoneOffset.UTC).toString()

            val addActivityResult = when (type) {
                "smoke" -> {
                    val value = smokeValueState.value.text.toIntOrNull() ?: 0
                    addActivityUseCase(activityDate, "smoke", value)
                }
                "workout" -> {
                    val value = workoutValueState.value.text.toIntOrNull() ?: 0
                    addActivityUseCase(activityDate, "workout", value)
                }
                else -> {
                    _errorMessage.value = "Invalid activity type"
                    Log.d("AddActivityViewModel", "Invalid activity type")
                    null
                }
            }

            _addActivityState.value = addActivityState.value.copy(isLoading = false)

            when (addActivityResult?.result) {
                is Resource.Success -> {
                    Log.d("AddActivityViewModel", "Activity added successfully")
                }
                is Resource.Error -> {
                    _errorMessage.value = addActivityResult.result.message ?: "Unknown error occurred"
                    addActivityResult.result.message?.let { Log.d("AddActivityViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("AddActivityViewModel", "Loading")
                }
                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("AddActivityViewModel", "Unexpected error")
                }
            }
        }
    }
}