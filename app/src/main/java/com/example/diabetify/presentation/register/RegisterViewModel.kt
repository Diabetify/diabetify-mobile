package com.example.diabetify.presentation.register

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diabetify.domain.usecases.auth.CreateAccountUseCase
import com.example.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase
): ViewModel() {
    private var _createAccountState = mutableStateOf(RegisterState())
    val createAccountState: State<RegisterState> = _createAccountState

    fun createAccount() {
        viewModelScope.launch {
            _createAccountState.value = createAccountState.value.copy(isLoading = true)

            val createAccountResult = createAccountUseCase(
                name = "name",
                email = "email",
                password = "password",
                age = 100,
                hipertension = false,
                cholesterol = false,
                disturbedVision = false,
                weight = 50,
                height = 150,
                verified = false
            )

            _createAccountState.value = createAccountState.value.copy(isLoading = false)

            if (createAccountResult.nameError != null) {
                // Handle name error
            }

            if (createAccountResult.emailError != null) {
                // Handle email error
            }

            if (createAccountResult.passwordError != null) {
                // Handle password error
            }

            if (createAccountResult.ageError != null) {
                // Handle age error
            }

            if (createAccountResult.weightError != null) {
                // Handle weight error
            }

            if (createAccountResult.heightError != null) {
                // Handle height error
            }

            when (createAccountResult.result) {
                is Resource.Success -> {
                    // Handle success
                    Log.d("RegisterViewModel", "Account created")
                }
                is Resource.Error -> {
                    // Handle error
                    createAccountResult.result.message?.let { Log.d("RegisterViewModel", it) }
                }
                is Resource.Loading -> {
                    // Handle loading
                    Log.d("RegisterViewModel", "Loading")
                }

                else -> {
                    // Handle unexpected error
                    Log.d("RegisterViewModel", "Unexpected error")
                }
            }
        }

    }
}