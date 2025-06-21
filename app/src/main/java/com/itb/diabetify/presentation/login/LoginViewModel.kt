package com.itb.diabetify.presentation.login

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.auth.GoogleLoginUseCase
import com.itb.diabetify.domain.usecases.auth.LoginUseCase
import com.itb.diabetify.presentation.common.FieldState
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
): ViewModel() {
    // Navigation and Error States
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Operational States
    private var _loginState = mutableStateOf(DataState())
    val loginState: State<DataState> = _loginState

    private var _googleLoginState = mutableStateOf(DataState())
    val googleLoginState: State<DataState> = _googleLoginState

    // Field States
    private val _emailState = mutableStateOf(FieldState())
    val emailState: State<FieldState> = _emailState

    private val _passwordState = mutableStateOf(FieldState())
    val passwordState: State<FieldState> = _passwordState

    // Setters for Field States
    fun setEmail(value: String) {
        _emailState.value = emailState.value.copy(error = null)
        _emailState.value = emailState.value.copy(text = value)
    }

    fun setPassword(value: String) {
        _passwordState.value = passwordState.value.copy(error = null)
        _passwordState.value = passwordState.value.copy(text = value)
    }

    // Validation Functions
    fun validateLoginFields(): Boolean {
        val email = emailState.value.text
        val password = passwordState.value.text

        var isValid = true

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailState.value = emailState.value.copy(error = "Email tidak valid")
            isValid = false
        }

        if (password.length < 8) {
            _passwordState.value = passwordState.value.copy(error = "Kata sandi harus lebih dari 8 karakter")
            isValid = false
        }

        return isValid
    }

    // API Call Functions
    fun login() {
        viewModelScope.launch {
            _loginState.value = loginState.value.copy(isLoading = true)

            val loginResult = loginUseCase(
                email = emailState.value.text,
                password = passwordState.value.text
            )

            _loginState.value = loginState.value.copy(isLoading = false)

            if (loginResult.emailError != null) {
                _emailState.value = emailState.value.copy(error = loginResult.emailError)
            }

            if (loginResult.passwordError != null) {
                _passwordState.value = passwordState.value.copy(error = loginResult.passwordError)
            }

            when (loginResult.result) {
                is Resource.Success -> {
                    resetValues()
                    _navigationEvent.value = "HOME_SCREEN"
                }
                is Resource.Error -> {
                    _errorMessage.value = loginResult.result.message ?: "Unknown error occurred"
                    loginResult.result.message?.let { Log.d("LoginViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("LoginViewModel", "Loading")
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("LoginViewModel", "Unexpected error")
                }
            }
        }
    }

    fun googleLogin(token: String) {
        viewModelScope.launch {
            _googleLoginState.value = googleLoginState.value.copy(isLoading = true)

            val googleLoginResult = googleLoginUseCase(
                token = token
            )

            _googleLoginState.value = googleLoginState.value.copy(isLoading = false)

            when (googleLoginResult.result) {
                is Resource.Success -> {
                    _navigationEvent.value = "HOME_SCREEN"
                }
                is Resource.Error -> {
                    _errorMessage.value = googleLoginResult.result.message ?: "Unknown error occurred"
                    googleLoginResult.result.message?.let { Log.d("RegisterViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("RegisterViewModel", "Loading")
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("RegisterViewModel", "Unexpected error")
                }
            }
        }
    }

    // Helper Functions
    private fun resetValues() {
        _emailState.value = FieldState()
        _passwordState.value = FieldState()
    }

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }
}