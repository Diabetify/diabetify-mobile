package com.itb.diabetify.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.auth.LogoutUseCase
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
): ViewModel() {
    private var _logoutState = mutableStateOf(DataState())
    val logoutState: State<DataState> = _logoutState

    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = logoutState.value.copy(isLoading = true)

            val logoutResult = logoutUseCase()

            _logoutState.value = logoutState.value.copy(isLoading = false)

            when (logoutResult.result) {
                is Resource.Success -> {
//                    _navigationEvent.value = "LOGIN_SCREEN"
                }
                is Resource.Error -> {
                    _errorMessage.value = logoutResult.result.message ?: "Unknown error occurred"
                    logoutResult.result.message?.let { Log.d("HomeViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("HomeViewModel", "Loading")
                }

                else -> {
                    // Handle unexpected er ror
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("HomeViewModel", "Unexpected error")
                }
            }
        }
    }

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }
}