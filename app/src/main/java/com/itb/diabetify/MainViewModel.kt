package com.itb.diabetify

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.manager.TokenManager
import com.itb.diabetify.domain.usecases.app_entry.AppEntryUseCase
import com.itb.diabetify.presentation.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appEntryUseCase: AppEntryUseCase,
    private val tokenManager: TokenManager
): ViewModel() {
    var splashCondition by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Route.AppStartNavigation.route)
        private set

    init {
        appEntryUseCase.readAppEntry().onEach { shouldStartFromRegisterScreen ->
            val isLoggedIn = tokenManager.isLoggedIn()

            startDestination = when {
                isLoggedIn -> Route.MainNavigation.route
                shouldStartFromRegisterScreen -> Route.AuthNavigation.route
                else -> Route.AppStartNavigation.route
            }

            delay(300)
            splashCondition = false
        }.launchIn(viewModelScope)
    }
}