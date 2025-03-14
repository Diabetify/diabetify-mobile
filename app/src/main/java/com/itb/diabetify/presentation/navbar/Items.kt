package com.itb.diabetify.presentation.navbar

data class NavigationItem(
    val route: String,
    val title: String,
    val icon: Int,
    val contentDescription: String
)

object NavigationItems {
    val items = listOf(
        NavigationItem(
            route = "home",
            title = "Home",
            icon = android.R.drawable.ic_menu_myplaces,
            contentDescription = "Navigate to home screen"
        ),
        NavigationItem(
            route = "history",
            title = "History",
            icon = android.R.drawable.ic_menu_search,
            contentDescription = "Navigate to history screen"
        ),
        NavigationItem(
            route = "recommendation",
            title = "Recommendation",
            icon = android.R.drawable.ic_menu_myplaces,
            contentDescription = "Navigate to recommendation screen"
        ),
        NavigationItem(
            route = "profile",
            title = "Profile",
            icon = android.R.drawable.ic_menu_preferences,
            contentDescription = "Navigate to profile screen"
        )
    )
}