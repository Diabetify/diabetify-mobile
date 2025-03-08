package com.itb.diabetify.presentation.register

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.itb.diabetify.presentation.common.FieldState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.auth.CreateAccountUseCase
import com.itb.diabetify.domain.usecases.auth.SendVerificationUseCase
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase,
    private val sendVerificationUseCase: SendVerificationUseCase
): ViewModel() {
    private var _createAccountState = mutableStateOf(RegisterState())
    val createAccountState: State<RegisterState> = _createAccountState

    private var _sendVerificationState = mutableStateOf(RegisterState())
    val sendVerificationState: State<RegisterState> = _sendVerificationState

    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


    private val _nameState = mutableStateOf(FieldState())
    val nameState: State<FieldState> = _nameState

    fun setName(value: String) {
        _nameState.value = nameState.value.copy(error = null)
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

    private val _weightState = mutableStateOf(FieldState())
    val weightState: State<FieldState> = _weightState

    fun setWeight(value: String) {
        _weightState.value = weightState.value.copy(error = null)
        _weightState.value = weightState.value.copy(text = value)
    }

    private val _heightState = mutableStateOf(FieldState())
    val heightState: State<FieldState> = _heightState

    fun setHeight(value: String) {
        _heightState.value = heightState.value.copy(error = null)
        _heightState.value = heightState.value.copy(text = value)
    }

    fun validateBiodataFields(): Boolean {
        val gender = genderState.value.text
        val birthDate = dobState.value.text
        val weight = weightState.value.text
        val height = heightState.value.text

        var isValid = true

        if (gender.isEmpty()) {
            _genderState.value = genderState.value.copy(error = "Jenis kelamin tidak boleh kosong")
            isValid = false
        }

        if (birthDate.isEmpty()) {
            _dobState.value = dobState.value.copy(error = "Tanggal lahir tidak boleh kosong")
            isValid = false
        }

        if (weight.isEmpty()) {
            _weightState.value = weightState.value.copy(error = "Berat badan tidak boleh kosong")
            isValid = false
        }

        if (height.isEmpty()) {
            _heightState.value = heightState.value.copy(error = "Tinggi badan tidak boleh kosong")
            isValid = false
        }

        if (weight != "" && weight.toInt() < 0) {
            _weightState.value = weightState.value.copy(error = "Berat badan tidak boleh negatif")
            isValid = false
        }

        if (height != "" && height.toInt() < 0) {
            _heightState.value = heightState.value.copy(error = "Tinggi badan tidak boleh negatif")
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
                hypertension = false,
                cholesterol = false,
                gender = genderFormatted,
                disturbedVision = false,
                weight = weightState.value.text.toInt(),
                height = heightState.value.text.toInt(),
                verified = false
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

            if (createAccountResult.weightError != null) {
                _weightState.value = weightState.value.copy(error = createAccountResult.weightError)
            }

            if (createAccountResult.heightError != null) {
                _heightState.value = heightState.value.copy(error = createAccountResult.heightError)
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

    private fun sendVerification() {
        viewModelScope.launch {
            _sendVerificationState.value = sendVerificationState.value.copy(isLoading = true)

            val sendVerificationResult = sendVerificationUseCase(
                email = emailState.value.text
            )

            _sendVerificationState.value = sendVerificationState.value.copy(isLoading = false)

            if (sendVerificationResult.emailError != null) {
                _emailState.value = emailState.value.copy(error = sendVerificationResult.emailError)
            }

            when (sendVerificationResult.result) {
                is Resource.Success -> {
                    _navigationEvent.value = "OTP_SCREEN"
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

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }
}