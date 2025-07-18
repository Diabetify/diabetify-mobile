package com.itb.diabetify.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.itb.diabetify.presentation.onboarding.OnBoardingEvent
import com.itb.diabetify.presentation.onboarding.OnBoardingScreen
import com.itb.diabetify.presentation.onboarding.pages
import com.itb.diabetify.ui.theme.DiabetifyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import io.mockk.mockk
import io.mockk.verify

@RunWith(AndroidJUnit4::class)
class OnBoardingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockEvent: (OnBoardingEvent) -> Unit = mockk(relaxed = true)

    @Test
    fun onboardingFlow_E2E_Test() {
        composeTestRule.setContent {
            DiabetifyTheme {
                OnBoardingScreen(event = mockEvent)
            }
        }

        composeTestRule.onNodeWithText("Kenali Risiko,\nKendalikan Diabetes").assertIsDisplayed()
        composeTestRule.onNodeWithText("Mulai").assertIsDisplayed()

        composeTestRule.onNodeWithText("Mulai").performClick()

        composeTestRule.onNodeWithText(pages[0].title).assertIsDisplayed()
        composeTestRule.onNodeWithText(pages[0].description).assertIsDisplayed()

        for (i in 0 until pages.size - 1) {
            composeTestRule.onNodeWithText(pages[i].title).performTouchInput {
                swipeLeft()
            }
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithText(pages[i + 1].title).assertIsDisplayed()
        }

        composeTestRule.onNodeWithText(">").performClick()

        verify { mockEvent(OnBoardingEvent.SaveAppEntry) }
    }
}