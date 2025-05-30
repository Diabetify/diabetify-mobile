package com.itb.diabetify.presentation.navbar.add_activity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.repository.ActivityRepository
import com.itb.diabetify.presentation.common.FieldState
import com.itb.diabetify.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddActivityViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {
    private var _activityTodayState = mutableStateOf(DataState())
    val activityTodayState = _activityTodayState

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
}