package com.itb.diabetify.presentation.settings

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.repository.UserRepository
import com.itb.diabetify.domain.usecases.auth.LogoutUseCase
import com.itb.diabetify.domain.usecases.user.EditUserUseCase
import com.itb.diabetify.presentation.common.FieldState
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val editUserUseCase: EditUserUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {
    private var _userState = mutableStateOf(DataState())
    val state: State<DataState> = _userState

    private var _editProfileState = mutableStateOf(DataState())
    val editProfileState: State<DataState> = _editProfileState

    private var _logoutState = mutableStateOf(DataState())
    val logoutState: State<DataState> = _logoutState

    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _userState.value = state.value.copy(isLoading = true)

            userRepository.getUser().onEach { user ->
                user?.let {
                    _nameState.value = nameState.value.copy(
                        text = it.name ?: "",
                        error = null
                    )

                    _emailState.value = emailState.value.copy(
                        text = it.email ?: "",
                        error = null
                    )
                }
            }.launchIn(viewModelScope)

            _userState.value = state.value.copy(isLoading = false)
        }
    }

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

    fun validateEditProfileFields(): Boolean {
        val name = nameState.value.text
        val email = emailState.value.text

        var isValid = true

        if (name.isEmpty()) {
            _nameState.value = nameState.value.copy(error = "Nama tidak boleh kosong")
            isValid = false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailState.value = emailState.value.copy(error = "Email tidak valid")
            isValid = false
        }

        return isValid
    }

    fun editProfile() {
        viewModelScope.launch {
            _editProfileState.value = editProfileState.value.copy(isLoading = true)

            val editUserResult = editUserUseCase(
                name = nameState.value.text,
                email = emailState.value.text
            )

            _editProfileState.value = editProfileState.value.copy(isLoading = false)

            when (editUserResult.result) {
                is Resource.Success -> {
                    Log.d("SettingsViewModel", "Profile updated successfully")
                }
                is Resource.Error -> {
                    _errorMessage.value = editUserResult.result.message ?: "Unknown error occurred"
                    editUserResult.result.message?.let { Log.d("SettingsViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("SettingsViewModel", "Loading")
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("SettingsViewModel", "Unexpected error")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = logoutState.value.copy(isLoading = true)

            val logoutResult = logoutUseCase()

            _logoutState.value = logoutState.value.copy(isLoading = false)

            when (logoutResult.result) {
                is Resource.Success -> {
                    _navigationEvent.value = "LOGIN_SCREEN"
                }
                is Resource.Error -> {
                    _errorMessage.value = logoutResult.result.message ?: "Unknown error occurred"
                    logoutResult.result.message?.let { Log.d("HomeViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("HomeViewModel", "Loading")
                }

                else -> {
                    // Handle unexpected error
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