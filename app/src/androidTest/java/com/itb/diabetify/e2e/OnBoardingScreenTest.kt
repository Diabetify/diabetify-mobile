package com.itb.diabetify.e2e

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.itb.diabetify.OnboardingNavigationUtils
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
    fun onboardingFlow_E2E_SwipeNavigation_Test() {
        composeTestRule.setContent {
            DiabetifyTheme {
                OnBoardingScreen(event = mockEvent)
            }
        }

        OnboardingNavigationUtils.verifyOnboardingScreen(composeTestRule)

        composeTestRule.onNodeWithText("Mulai").performClick()


        for (i in 0 until pages.size - 1) {
            OnboardingNavigationUtils.verifyOnboardingPage(
                composeTestRule,
                pages[i].title,
                pages[i].description
            )

            composeTestRule.onNodeWithText(pages[i].title).performTouchInput {
                swipeLeft()
            }
            composeTestRule.waitForIdle()
            composeTestRule.onNodeWithText(pages[i + 1].title).assertIsDisplayed()
        }

        composeTestRule.onNodeWithText(">").performClick()

        verify { mockEvent(OnBoardingEvent.SaveAppEntry) }
    }

    @Test 
    fun onboardingFlow_E2E_ButtonNavigation_Test() {
        composeTestRule.setContent {
            DiabetifyTheme {
                OnBoardingScreen(event = mockEvent)
            }
        }

        OnboardingNavigationUtils.verifyOnboardingScreen(composeTestRule)

        composeTestRule.onNodeWithText("Mulai").performClick()

        for (i in 0 until pages.size - 1) {
            OnboardingNavigationUtils.verifyOnboardingPage(
                composeTestRule,
                pages[i].title,
                pages[i].description
            )

            composeTestRule.onNodeWithText(">").performClick()
            composeTestRule.waitForIdle()
            OnboardingNavigationUtils.verifyOnboardingPage(
                composeTestRule,
                pages[i + 1].title,
                pages[i + 1].description
            )
        }
    }
}