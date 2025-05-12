package com.itb.diabetify.presentation.navbar

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.itb.diabetify.presentation.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {
    private val _selectedItem = mutableStateOf(Route.HomeScreen.route)
    val selectedItem: State<String> = _selectedItem

    val navigationItems = items

    fun onNavigationItemSelected(route: String) {
        _selectedItem.value = route
    }
}