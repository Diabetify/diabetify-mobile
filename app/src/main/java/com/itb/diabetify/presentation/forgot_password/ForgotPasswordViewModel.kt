package com.itb.diabetify.presentation.forgot_password

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.auth.ChangePasswordUseCase
import com.itb.diabetify.domain.usecases.auth.SendVerificationUseCase
import com.itb.diabetify.presentation.common.FieldState
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val sendVerificationUseCase: SendVerificationUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
): ViewModel() {
    private var _sendVerificationState = mutableStateOf(DataState())
    val sendVerificationState: State<DataState> = _sendVerificationState

    private var _changePasswordState = mutableStateOf(DataState())
    val changePasswordState: State<DataState> = _changePasswordState

    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


    private val _emailState = mutableStateOf(FieldState())
    val emailState: State<FieldState> = _emailState

    fun setEmail(value: String) {
        _emailState.value = emailState.value.copy(error = null)
        _emailState.value = emailState.value.copy(text = value)
    }

    private val _passwordState = mutableStateOf(FieldState())
    val passwordState: State<FieldState> = _passwordState

    fun setPassword(value: String) {
        _passwordState.value = passwordState.value.copy(error = null)
        _passwordState.value = passwordState.value.copy(text = value)
    }

    private val _otpState = mutableStateOf(FieldState())
    val otpState: State<FieldState> = _otpState

    fun setOtp(value: String) {
        if (value.length <= 6 && value.all { it.isDigit() }) {
            _otpState.value = otpState.value.copy(error = null)
            _otpState.value = otpState.value.copy(text = value)
        }
    }

    fun validateForgotPasswordFields(): Boolean {
        val email = emailState.value.text

        var isValid = true

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailState.value = emailState.value.copy(error = "Email tidak valid")
            isValid = false
        }

        return isValid
    }

    fun sendVerification() {
        viewModelScope.launch {
            _sendVerificationState.value = sendVerificationState.value.copy(isLoading = true)

            val sendVerificationResult = sendVerificationUseCase(
                email = emailState.value.text,
                type = "reset-password"
            )

            _sendVerificationState.value = sendVerificationState.value.copy(isLoading = false)

            if (sendVerificationResult.emailError != null) {
                _emailState.value = emailState.value.copy(error = sendVerificationResult.emailError)
            }

            when (sendVerificationResult.result) {
                is Resource.Success -> {
                    _navigationEvent.value = "CHANGE_PASSWORD_SCREEN"
                }
                is Resource.Error -> {
                    _errorMessage.value = sendVerificationResult.result.message ?: "Unknown error occurred"
                    sendVerificationResult.result.message?.let { Log.d("ForgotPasswordViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("ForgotPasswordViewModel", "Loading")
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("ForgotPasswordViewModel", "Unexpected error")
                }
            }
        }
    }

    fun validateChangePasswordFields(): Boolean {
        val password = passwordState.value.text
        val code = otpState.value.text

        var isValid = true

        if (password.length < 8) {
            _passwordState.value = passwordState.value.copy(error = "Kata sandi harus lebih dari 8 karakter")
            isValid = false
        }

        if (code.isEmpty()) {
            _otpState.value = otpState.value.copy(error = "Kode tidak boleh kosong")
            isValid = false
        }

        if (code.length != 6) {
            _otpState.value = otpState.value.copy(error = "Kode harus 6 digit")
            isValid = false
        }

        return isValid
    }

    fun changePassword() {
        viewModelScope.launch {
            _changePasswordState.value = changePasswordState.value.copy(isLoading = true)

            val changePasswordResult = changePasswordUseCase(
                email = emailState.value.text,
                newPassword = passwordState.value.text,
                code = otpState.value.text
            )

            _changePasswordState.value = changePasswordState.value.copy(isLoading = false)

            if (changePasswordResult.emailError != null) {
                _emailState.value = emailState.value.copy(error = changePasswordResult.emailError)
            }

            if (changePasswordResult.newPasswordError != null) {
                _passwordState.value = passwordState.value.copy(error = changePasswordResult.newPasswordError)
            }

            if (changePasswordResult.codeError != null) {
                _otpState.value = otpState.value.copy(error = changePasswordResult.codeError)
            }

            when (changePasswordResult.result) {
                is Resource.Success -> {
                    _navigationEvent.value = "LOGIN_SCREEN"
                }
                is Resource.Error -> {
                    _errorMessage.value = changePasswordResult.result.message ?: "Unknown error occurred"
                    changePasswordResult.result.message?.let { Log.d("ForgotPasswordViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("ForgotPasswordViewModel", "Loading")
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("ForgotPasswordViewModel", "Unexpected error")
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