package com.itb.diabetify.presentation.forgot_password

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
class ForgotPasswordViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
): ViewModel() {
    // Navigation, Error, and Success States
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage

    // Operational States
    private var _sendVerificationState = mutableStateOf(DataState())
    val sendVerificationState: State<DataState> = _sendVerificationState

    private var _changePasswordState = mutableStateOf(DataState())
    val changePasswordState: State<DataState> = _changePasswordState

    // UI States
    private val _passwordVisible = mutableStateOf(false)
    val passwordVisible: State<Boolean> = _passwordVisible

    // Field States
    private val _emailFieldState = mutableStateOf(FieldState())
    val emailFieldState: State<FieldState> = _emailFieldState

    private val _passwordFieldState = mutableStateOf(FieldState())
    val passwordFieldState: State<FieldState> = _passwordFieldState

    private val _otpFieldState = mutableStateOf(FieldState())
    val otpFieldState: State<FieldState> = _otpFieldState

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

    fun setOtp(value: String) {
        if (value.length <= 6 && value.all { it.isDigit() }) {
            _otpFieldState.value = otpFieldState.value.copy(error = null)
            _otpFieldState.value = otpFieldState.value.copy(text = value)
        }
    }

    // Validation Functions
    fun validateForgotPasswordFields(): Boolean {
        val email = emailFieldState.value.text

        var isValid = true

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailFieldState.value = emailFieldState.value.copy(error = "Email tidak valid")
            isValid = false
        }

        return isValid
    }

    fun validateChangePasswordFields(): Boolean {
        val password = passwordFieldState.value.text
        val code = otpFieldState.value.text

        var isValid = true

        if (password.length < 8) {
            _passwordFieldState.value = passwordFieldState.value.copy(error = "Kata sandi harus lebih dari 8 karakter")
            isValid = false
        }

        if (code.isEmpty()) {
            _otpFieldState.value = otpFieldState.value.copy(error = "Kode tidak boleh kosong")
            isValid = false
        }

        if (code.length != 6) {
            _otpFieldState.value = otpFieldState.value.copy(error = "Kode harus 6 digit")
            isValid = false
        }

        return isValid
    }

    // Use Case Calls
    fun sendVerification(
        isResend: Boolean = false
    ) {
        viewModelScope.launch {
            _sendVerificationState.value = sendVerificationState.value.copy(isLoading = true)

            val sendVerificationResult = authUseCases.sendVerification(
                email = emailFieldState.value.text,
                type = "reset-password"
            )

            _sendVerificationState.value = sendVerificationState.value.copy(isLoading = false)

            if (sendVerificationResult.emailError != null) {
                _emailFieldState.value = emailFieldState.value.copy(error = sendVerificationResult.emailError)
            }

            when (sendVerificationResult.result) {
                is Resource.Success -> {
                    if (isResend) {
                        _successMessage.value = "Kode OTP telah dikirim ulang"
                    } else {
                        _navigationEvent.value = "CHANGE_PASSWORD_SCREEN"
                    }
                }
                is Resource.Error -> {
                    _errorMessage.value = sendVerificationResult.result.message ?: "Terjadi kesalahan saat mengirim kode verifikasi"
                    sendVerificationResult.result.message?.let { Log.e("ForgotPasswordViewModel", it) }
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat mengirim kode verifikasi"
                    Log.e("ForgotPasswordViewModel", "Unexpected error")
                }
            }
        }
    }

    fun changePassword() {
        viewModelScope.launch {
            _changePasswordState.value = changePasswordState.value.copy(isLoading = true)

            val changePasswordResult = authUseCases.changePassword(
                email = emailFieldState.value.text,
                newPassword = passwordFieldState.value.text,
                code = otpFieldState.value.text
            )

            _changePasswordState.value = changePasswordState.value.copy(isLoading = false)

            if (changePasswordResult.emailError != null) {
                _emailFieldState.value = emailFieldState.value.copy(error = changePasswordResult.emailError)
            }

            if (changePasswordResult.newPasswordError != null) {
                _passwordFieldState.value = passwordFieldState.value.copy(error = changePasswordResult.newPasswordError)
            }

            if (changePasswordResult.codeError != null) {
                _otpFieldState.value = otpFieldState.value.copy(error = changePasswordResult.codeError)
            }

            when (changePasswordResult.result) {
                is Resource.Success -> {
                    resetValues()
                    _navigationEvent.value = "SUCCESS_SCREEN"
                }
                is Resource.Error -> {
                    _errorMessage.value = changePasswordResult.result.message ?: "Terjadi kesalahan saat mengubah kata sandi"
                    changePasswordResult.result.message?.let { Log.e("ForgotPasswordViewModel", it) }
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat mengubah kata sandi"
                    Log.e("ForgotPasswordViewModel", "Unexpected error")
                }
            }
        }
    }

    // Helper Functions
    private fun resetValues() {
        _emailFieldState.value = FieldState()
        _passwordFieldState.value = FieldState()
        _otpFieldState.value = FieldState()
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
}