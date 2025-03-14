package com.itb.diabetify.presentation.navbar

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NavigationViewModel() : ViewModel() {
    private val _selectedItem = mutableStateOf("home")
    val selectedItem: State<String> = _selectedItem

    val navigationItems = NavigationItems.items

    fun onNavigationItemSelected(route: String) {
        _selectedItem.value = route
    }
}