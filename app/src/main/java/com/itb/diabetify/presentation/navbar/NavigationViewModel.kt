package com.itb.diabetify.presentation.navbar

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.itb.diabetify.presentation.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {
    // UI States
    private val _selectedItem = mutableStateOf(Route.HomeScreen.route)
    val selectedItem: State<String> = _selectedItem

    private val _showPopUp = mutableStateOf(false)
    val showPopUp: State<Boolean> = _showPopUp

    val navigationItems = items

    // Setters for UI States
    fun onNavigationItemSelected(route: String) {
        _selectedItem.value = route
    }

    fun setShowPopUp(show: Boolean) {
        _showPopUp.value = show
    }
}