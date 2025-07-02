package com.itb.diabetify.presentation.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.auth.AuthUseCases
import com.itb.diabetify.presentation.common.FieldState
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
): ViewModel() {
    // Navigation and Error States
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Operational States
    private var _loginState = mutableStateOf(DataState())
    val loginState: State<DataState> = _loginState

    // UI States
    private val _passwordVisible = mutableStateOf(false)
    val passwordVisible: State<Boolean> = _passwordVisible

    // Field States
    private val _emailFieldState = mutableStateOf(FieldState())
    val emailFieldState: State<FieldState> = _emailFieldState

    private val _passwordFieldState = mutableStateOf(FieldState())
    val passwordFieldState: State<FieldState> = _passwordFieldState

    // Setters for UI States
    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    // Setters for Field States
    fun setEmail(value: String) {
        _emailFieldState.value = emailFieldState.value.copy(error = null)
        _emailFieldState.value = emailFieldState.value.copy(text = value)
    }

    fun setPassword(value: String) {
        _passwordFieldState.value = passwordFieldState.value.copy(error = null)
        _passwordFieldState.value = passwordFieldState.value.copy(text = value)
    }

    // Validation Functions
    fun validateLoginFields(): Boolean {
        val email = emailFieldState.value.text
        val password = passwordFieldState.value.text

        var isValid = true

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailFieldState.value = emailFieldState.value.copy(error = "Email tidak valid")
            isValid = false
        }

        if (password.length < 8) {
            _passwordFieldState.value = passwordFieldState.value.copy(error = "Kata sandi harus lebih dari 8 karakter")
            isValid = false
        }

        return isValid
    }

    // Use Case Calls
    fun login() {
        viewModelScope.launch {
            _loginState.value = loginState.value.copy(isLoading = true)

            val loginResult = authUseCases.login(
                email = emailFieldState.value.text,
                password = passwordFieldState.value.text
            )

            _loginState.value = loginState.value.copy(isLoading = false)

            if (loginResult.emailError != null) {
                _emailFieldState.value = emailFieldState.value.copy(error = loginResult.emailError)
            }

            if (loginResult.passwordError != null) {
                _passwordFieldState.value = passwordFieldState.value.copy(error = loginResult.passwordError)
            }

            when (loginResult.result) {
                is Resource.Success -> {
                    resetValues()
                    _navigationEvent.value = "HOME_SCREEN"
                }
                is Resource.Error -> {
                    _errorMessage.value = loginResult.result.message ?: "Terjadi kesalahan saat masuk"
                    loginResult.result.message?.let { Log.e("LoginViewModel", it) }
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat masuk"
                    Log.e("LoginViewModel", "Unexpected error")
                }
            }
        }
    }

    // Helper Functions
    private fun resetValues() {
        _emailFieldState.value = FieldState()
        _passwordFieldState.value = FieldState()
    }

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }
}