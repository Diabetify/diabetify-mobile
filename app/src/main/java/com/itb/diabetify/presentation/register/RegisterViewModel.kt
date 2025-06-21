package com.itb.diabetify.presentation.register

import android.util.Log
import android.util.Patterns
import com.itb.diabetify.presentation.common.FieldState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.auth.CreateAccountUseCase
import com.itb.diabetify.domain.usecases.auth.GoogleLoginUseCase
import com.itb.diabetify.domain.usecases.auth.SendVerificationUseCase
import com.itb.diabetify.domain.usecases.auth.VerifyOtpUseCase
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val sendVerificationUseCase: SendVerificationUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase
): ViewModel() {
    // Navigation and Error States
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Operational States
    private var _createAccountState = mutableStateOf(DataState())
    val createAccountState: State<DataState> = _createAccountState

    private var _googleLoginState = mutableStateOf(DataState())
    val googleLoginState: State<DataState> = _googleLoginState

    private var _sendVerificationState = mutableStateOf(DataState())
    val sendVerificationState: State<DataState> = _sendVerificationState

    private var _verifyOtpState = mutableStateOf(DataState())
    val verifyOtpState: State<DataState> = _verifyOtpState

    // Field States
    private val _nameState = mutableStateOf(FieldState())
    val nameState: State<FieldState> = _nameState

    private val _nameStateCopy = mutableStateOf(FieldState())
    val nameStateCopy: State<FieldState> = _nameStateCopy

    fun setName(value: String) {
        _nameState.value = nameState.value.copy(error = null)
        _nameStateCopy.value = nameState.value.copy(text = value)
        _nameState.value = nameState.value.copy(text = value)
    }

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

    private val _privacyPolicyState = mutableStateOf(false)
    val privacyPolicyState: State<Boolean> = _privacyPolicyState

    fun setPrivacyPolicy(value: Boolean) {
        _privacyPolicyState.value = value
    }

    fun validateRegisterFields(): Boolean {
        val name = nameState.value.text
        val email = emailState.value.text
        val password = passwordState.value.text

        var isValid = true

        if (name.isEmpty()) {
            _nameState.value = nameState.value.copy(error = "Nama tidak boleh kosong")
            isValid = false
        }

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

    private val _genderState = mutableStateOf(FieldState())
    val genderState: State<FieldState> = _genderState

    fun setGender(value: String) {
        _genderState.value = genderState.value.copy(error = null)
        _genderState.value = genderState.value.copy(text = value)
    }

    private val _dobState = mutableStateOf(FieldState())
    val dobState: State<FieldState> = _dobState

    fun setDob(value: String) {
        _dobState.value = dobState.value.copy(error = null)
        _dobState.value = dobState.value.copy(text = value)
    }

    private val _otpState = mutableStateOf(FieldState())
    val otpState: State<FieldState> = _otpState

    fun setOtp(value: String) {
        if (value.length <= 6 && value.all { it.isDigit() }) {
            _otpState.value = otpState.value.copy(error = null)
            _otpState.value = otpState.value.copy(text = value)
        }
    }

    fun validateBiodataFields(): Boolean {
        val gender = genderState.value.text
        val birthDate = dobState.value.text

        var isValid = true

        if (gender.isEmpty()) {
            _genderState.value = genderState.value.copy(error = "Jenis kelamin tidak boleh kosong")
            isValid = false
        }

        if (birthDate.isEmpty()) {
            _dobState.value = dobState.value.copy(error = "Tanggal lahir tidak boleh kosong")
            isValid = false
        }

        return isValid
    }

    fun createAccount() {
        viewModelScope.launch {
            _createAccountState.value = createAccountState.value.copy(isLoading = true)

            val dob = dobState.value.text
            val dobParts = dob.split("/")
            val dobFormatted = "${dobParts[2]}-${dobParts[1]}-${dobParts[0]}"

            val gender = genderState.value.text
            val genderFormatted = if (gender == "Laki-laki") { "male" } else { "female" }

            val createAccountResult = createAccountUseCase(
                name = nameState.value.text,
                email = emailState.value.text,
                password = passwordState.value.text,
                dob = dobFormatted,
                gender = genderFormatted,
            )

            _createAccountState.value = createAccountState.value.copy(isLoading = false)

            if (createAccountResult.nameError != null) {
                _nameState.value = nameState.value.copy(error = createAccountResult.nameError)
            }

            if (createAccountResult.emailError != null) {
                _emailState.value = emailState.value.copy(error = createAccountResult.emailError)
            }

            if (createAccountResult.passwordError != null) {
                _passwordState.value = passwordState.value.copy(error = createAccountResult.passwordError)
            }

            if (createAccountResult.dobError != null) {
                _dobState.value = dobState.value.copy(error = createAccountResult.dobError)
            }

            if (createAccountResult.genderError != null) {
                _genderState.value = genderState.value.copy(error = createAccountResult.genderError)
            }

            when (createAccountResult.result) {
                is Resource.Success -> {
                    sendVerification()
                }
                is Resource.Error -> {
                    _errorMessage.value = createAccountResult.result.message ?: "Unknown error occurred"
                    createAccountResult.result.message?.let { Log.d("RegisterViewModel", it) }
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

    fun sendVerification(
        isResend: Boolean = false
    ) {
        viewModelScope.launch {
            _sendVerificationState.value = sendVerificationState.value.copy(isLoading = true)

            val sendVerificationResult = sendVerificationUseCase(
                email = emailState.value.text,
                type = "register"
            )

            _sendVerificationState.value = sendVerificationState.value.copy(isLoading = false)

            if (sendVerificationResult.emailError != null) {
                _emailState.value = emailState.value.copy(error = sendVerificationResult.emailError)
            }

            when (sendVerificationResult.result) {
                is Resource.Success -> {
                    if (isResend) {
                        _errorMessage.value = "Kode OTP berhasil dikirim ulang"
                    } else {
                        _navigationEvent.value = "OTP_SCREEN"
                    }
                }
                is Resource.Error -> {
                    _errorMessage.value = sendVerificationResult.result.message ?: "Unknown error occurred"
                    sendVerificationResult.result.message?.let { Log.d("RegisterViewModel", it) }
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

    fun validateOtpFields(): Boolean {
        val code = otpState.value.text

        var isValid = true

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

    fun verifyOtp() {
        viewModelScope.launch {
            _verifyOtpState.value = verifyOtpState.value.copy(isLoading = true)

            val verifyOtpResult = verifyOtpUseCase(
                email = emailState.value.text,
                code = otpState.value.text
            )

            _verifyOtpState.value = verifyOtpState.value.copy(isLoading = false)

            if (verifyOtpResult.emailError != null) {
                _emailState.value = emailState.value.copy(error = verifyOtpResult.emailError)
            }

            if (verifyOtpResult.codeError != null) {
                _otpState.value = otpState.value.copy(error = verifyOtpResult.codeError)
            }

            when (verifyOtpResult.result) {
                is Resource.Success -> {
                    resetValues()
                    _navigationEvent.value = "SUCCESS_SCREEN"
                }
                is Resource.Error -> {
                    _errorMessage.value = verifyOtpResult.result.message ?: "Unknown error occurred"
                    verifyOtpResult.result.message?.let { Log.d("RegisterViewModel", it) }
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

    private fun resetValues() {
        _nameState.value = FieldState()
        _emailState.value = FieldState()
        _passwordState.value = FieldState()
        _privacyPolicyState.value = false
        _genderState.value = FieldState()
        _dobState.value = FieldState()
        _otpState.value = FieldState()
    }

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }
}