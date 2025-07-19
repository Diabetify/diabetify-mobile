package com.itb.diabetify

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule

object OnboardingNavigationUtils {
    fun verifyOnboardingScreen(composeTestRule: ComposeTestRule) {
        composeTestRule.onNodeWithText("Kenali Risiko", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Mulai").assertIsDisplayed()
    }

    fun verifyOnboardingPage(composeTestRule: ComposeTestRule, title: String, description: String) {
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText(description).assertIsDisplayed()
    }
} 