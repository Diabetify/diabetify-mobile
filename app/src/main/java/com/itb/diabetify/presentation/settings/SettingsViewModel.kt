package com.itb.diabetify.presentation.settings

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.auth.AuthUseCases
import com.itb.diabetify.domain.usecases.user.UserUseCases
import com.itb.diabetify.domain.usecases.notification.NotificationUseCases
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
    private val userUseCases: UserUseCases,
    private val authUseCases: AuthUseCases,
    private val notificationUseCases: NotificationUseCases
): ViewModel() {
    // Navigation, Error, and Success States
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private var _successMessage = mutableStateOf<String?>(null)
    val successMessage: State<String?> = _successMessage

    // Operational States
    private var _userState = mutableStateOf(DataState())
    val userState: State<DataState> = _userState

    private var _editProfileState = mutableStateOf(DataState())
    val editProfileState: State<DataState> = _editProfileState

    private var _logoutState = mutableStateOf(DataState())
    val logoutState: State<DataState> = _logoutState

    // UI States
    private val _dailyReminderEnabled = mutableStateOf(true)
    val dailyReminderEnabled: State<Boolean> = _dailyReminderEnabled

    private val _showLogoutDialog = mutableStateOf(false)
    val showLogoutDialog: State<Boolean> = _showLogoutDialog

    private val _showDatePicker = mutableStateOf(false)
    val showDatePicker: State<Boolean> = _showDatePicker

    // Field States
    private val _nameFieldState = mutableStateOf(FieldState())
    val nameFieldState: State<FieldState> = _nameFieldState

    private val _emailFieldState = mutableStateOf(FieldState())
    val emailFieldState: State<FieldState> = _emailFieldState

    private val _genderFieldState = mutableStateOf(FieldState())
    val genderFieldState: State<FieldState> = _genderFieldState

    private val _dobFieldState = mutableStateOf(FieldState())
    val dobFieldState: State<FieldState> = _dobFieldState

    // Initialization
    init {
        collectUserData()
        loadNotificationPreferences()
    }

    // Setters for UI States
    fun setDailyReminderEnabled(enabled: Boolean) {
        _dailyReminderEnabled.value = enabled
        notificationUseCases.setNotificationPreferences(enabled)
    }

    fun setShowLogoutDialog(show: Boolean) {
        _showLogoutDialog.value = show
    }

    fun setShowDatePicker(show: Boolean) {
        _showDatePicker.value = show
    }

    // Setters for Field States
    fun setName(value: String) {
        _nameFieldState.value = nameFieldState.value.copy(error = null)
        _nameFieldState.value = nameFieldState.value.copy(text = value)
    }

    fun setEmail(value: String) {
        _emailFieldState.value = emailFieldState.value.copy(error = null)
        _emailFieldState.value = emailFieldState.value.copy(text = value)
    }

    fun setGender(value: String) {
        _genderFieldState.value = genderFieldState.value.copy(error = null)
        _genderFieldState.value = genderFieldState.value.copy(text = value)
    }

    fun setDob(value: String) {
        _dobFieldState.value = dobFieldState.value.copy(error = null)
        _dobFieldState.value = dobFieldState.value.copy(text = value)
    }

    // Validation Functions
    fun validateEditProfileFields(): Boolean {
        val name = nameFieldState.value.text
        val email = emailFieldState.value.text
        val gender = genderFieldState.value.text
        val dob = dobFieldState.value.text

        var isValid = true

        if (name.isEmpty()) {
            _nameFieldState.value = nameFieldState.value.copy(error = "Nama tidak boleh kosong")
            isValid = false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailFieldState.value = emailFieldState.value.copy(error = "Email tidak valid")
            isValid = false
        }

        if (gender.isEmpty()) {
            _genderFieldState.value = genderFieldState.value.copy(error = "Jenis kelamin tidak boleh kosong")
            isValid = false
        }

        if (dob.isEmpty()) {
            _dobFieldState.value = dobFieldState.value.copy(error = "Tanggal lahir tidak boleh kosong")
            isValid = false
        }

        return isValid
    }

    // Use Case Calls
    private fun loadNotificationPreferences() {
        _dailyReminderEnabled.value = notificationUseCases.getNotificationPreferences()
    }

    private fun collectUserData() {
        viewModelScope.launch {
            _userState.value = userState.value.copy(isLoading = true)

            userUseCases.getUserRepository().onEach { user ->
                user?.let {
                    _nameFieldState.value = nameFieldState.value.copy(
                        text = it.name,
                        error = null
                    )
                    _emailFieldState.value = emailFieldState.value.copy(
                        text = it.email,
                        error = null
                    )
                    _genderFieldState.value = genderFieldState.value.copy(
                        text = it.gender,
                        error = null
                    )
                    _dobFieldState.value = dobFieldState.value.copy(
                        text = it.dob,
                        error = null
                    )
                }
            }.launchIn(viewModelScope)

            _userState.value = userState.value.copy(isLoading = false)
        }
    }

    fun editProfile() {
        viewModelScope.launch {
            _editProfileState.value = editProfileState.value.copy(isLoading = true)

            val dob = dobFieldState.value.text
            val dobParts = dob.split("/")
            val dobFormatted = "${dobParts[2]}-${dobParts[1]}-${dobParts[0]}"

            val gender = genderFieldState.value.text
            val genderFormatted = if (gender == "Laki-laki") { "male" } else { "female" }

            val editUserResult = userUseCases.editUser(
                name = nameFieldState.value.text,
                email = emailFieldState.value.text,
                dob = dobFormatted,
                gender = genderFormatted
            )

            _editProfileState.value = editProfileState.value.copy(isLoading = false)

            if (editUserResult.nameError != null) {
                _nameFieldState.value = nameFieldState.value.copy(error = editUserResult.nameError)
            }

            if (editUserResult.emailError != null) {
                _emailFieldState.value = emailFieldState.value.copy(error = editUserResult.emailError)
            }

            if (editUserResult.genderError != null) {
                _genderFieldState.value = genderFieldState.value.copy(error = editUserResult.genderError)
            }

            if (editUserResult.dobError != null) {
                _dobFieldState.value = dobFieldState.value.copy(error = editUserResult.dobError)
            }

            when (editUserResult.result) {
                is Resource.Success -> {
                    _successMessage.value = "Profil berhasil diperbarui"
                    Log.d("SettingsViewModel", "Profile updated successfully")
                }
                is Resource.Error -> {
                    _errorMessage.value = editUserResult.result.message ?: "Terjadi kesalahan saat memperbarui profil"
                    editUserResult.result.message?.let { Log.e("SettingsViewModel", it) }
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat memperbarui profil"
                    Log.e("SettingsViewModel", "Unexpected error")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = logoutState.value.copy(isLoading = true)

            val logoutResult = authUseCases.logout()

            _logoutState.value = logoutState.value.copy(isLoading = false)

            when (logoutResult.result) {
                is Resource.Success -> {
                    _navigationEvent.value = "LOGIN_SCREEN"
                }
                is Resource.Error -> {
                    _errorMessage.value = logoutResult.result.message ?: "Terjadi kesalahan saat logout"
                    logoutResult.result.message?.let { Log.e("SettingsViewModel", it) }
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat logout"
                    Log.e("SettingsViewModel", "Unexpected error")
                }
            }
        }
    }

    // Helper Functions
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