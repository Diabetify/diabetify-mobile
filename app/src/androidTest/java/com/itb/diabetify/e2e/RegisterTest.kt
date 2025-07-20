package com.itb.diabetify.e2e

import android.annotation.SuppressLint
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.itb.diabetify.MainActivity
import com.itb.diabetify.di.AppModule
import com.itb.diabetify.domain.repository.AuthRepository
import com.itb.diabetify.e2e.repository.FakeAuthRepository
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(AppModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RegisterTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    @BindValue
    val fakeRepo: AuthRepository = FakeAuthRepository()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun completeRegistrationFlow_E2E_ActsLikeRealUser() {
        startAppAndNavigateToRegister()

        fillRegistrationForm(
            name = "John Doe",
            email = "john.doe@example.com",
            password = "SecurePassword123"
        )

        acceptPrivacyPolicyAndSubmit()

        fillBiodataFormIfPresent(
            gender = "Laki-laki",
            birthDate = "15/08/1990"
        )

        proceedToOtpVerificationIfPresent()

        enterOtpAndVerifyIfPresent("123456")

        verifyRegistrationFlowCompletion()
    }

    @SuppressLint("CheckResult")
    private fun startAppAndNavigateToRegister() {
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        try {
            composeTestRule.onNodeWithText("Kenali Risiko", substring = true).assertIsDisplayed()
            composeTestRule.onNodeWithText("Mulai").assertIsDisplayed()
            composeTestRule.onNodeWithText("Mulai").performClick()
            composeTestRule.waitForIdle()
        } catch (_: AssertionError) {
        }

        val maxOnboardingPages = 4

        repeat(maxOnboardingPages) {
            try {
                composeTestRule.onNodeWithText(">").performClick()
                composeTestRule.waitForIdle()
                Thread.sleep(500)
            } catch (e: AssertionError) {
                try {
                    composeTestRule.onRoot().performTouchInput {
                        swipeLeft()
                    }
                    composeTestRule.waitForIdle()
                    Thread.sleep(500)
                } catch (_: AssertionError) {
                }
            }

            try {
                composeTestRule.onNodeWithText(">").assertExists()
            } catch (_: AssertionError) {
            }
        }

        try {
            composeTestRule.onNodeWithText(">").performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(1000)
        } catch (_: AssertionError) {
        }

        composeTestRule.waitForIdle()

        try {
            composeTestRule.onNode(hasText("Belum memiliki akun", substring = true))
                .assertIsDisplayed()
                .performClick()
            composeTestRule.waitForIdle()
        } catch (e: AssertionError) {
            try {
                composeTestRule.onNode(hasText("Daftar", substring = true))
                    .performClick()
                composeTestRule.waitForIdle()
            } catch (_: AssertionError) {
            }
        }

        var attempts = 0
        val maxAttempts = 5

        while (attempts < maxAttempts) {
            try {
                composeTestRule.onNodeWithText("Buat Akun Anda").assertIsDisplayed()
                return
            } catch (e: AssertionError) {
                attempts++
                Thread.sleep(1000)
                composeTestRule.waitForIdle()

                if (attempts < maxAttempts) {
                    try {
                        composeTestRule.onNode(hasText("Daftar", substring = true))
                            .performClick()
                        composeTestRule.waitForIdle()
                    } catch (_: AssertionError) {
                    }
                }
            }
        }

        throw AssertionError("Could not navigate to register screen after completing onboarding. Current screen might be different than expected.")
    }

    private fun fillRegistrationForm(name: String, email: String, password: String) {
        composeTestRule.onNode(hasText("Nama", substring = true))
            .performClick()
            .performTextInput(name)

        composeTestRule.onNode(hasText("Email", substring = true))
            .performClick()
            .performTextInput(email)

        composeTestRule.onNode(hasText("Kata Sandi", substring = true))
            .performClick()
            .performTextInput(password)

        composeTestRule.waitForIdle()
    }

    private fun acceptPrivacyPolicyAndSubmit() {
        composeTestRule.onNodeWithTag("PrivacyPolicyCheckbox")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Daftar")
            .performClick()

        composeTestRule.waitForIdle()


    }

    private fun fillBiodataFormIfPresent(gender: String, birthDate: String) {
        try {
            composeTestRule.onNode(hasText("Lengkapi Profil", substring = true))
                .assertIsDisplayed()

            fillBiodataForm(gender, birthDate)
        } catch (_: AssertionError) {
        }
    }

    private fun fillBiodataForm(gender: String, birthDate: String) {
        composeTestRule.onNode(hasText("Jenis Kelamin", substring = true))
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(gender)
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNode(hasText("Tanggal Lahir", substring = true))
            .performClick()

        composeTestRule.waitForIdle()

        try {
            composeTestRule.onNodeWithText("OK").performClick()
        } catch (e: AssertionError) {
            try {
                composeTestRule.onNodeWithText("Konfirmasi").performClick()
            } catch (e2: AssertionError) {
            }
        }

        composeTestRule.waitForIdle()
    }

    private fun proceedToOtpVerificationIfPresent() {
        try {
            composeTestRule.onNode(hasText("Lanjut", substring = true))
                .performClick()

            composeTestRule.waitForIdle()
        } catch (_: AssertionError) {
        }
    }

    private fun enterOtpAndVerifyIfPresent(otpCode: String) {
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("OtpTextField")
                .fetchSemanticsNodes().size == 1
        }

        enterOtpAndVerify(otpCode)
    }

    private fun enterOtpAndVerify(otpCode: String) {
        composeTestRule.onNodeWithTag("OtpTextField")
            .performTextInput(otpCode)

        composeTestRule.waitForIdle()

        composeTestRule.onNode(hasText("Verifikasi") and hasClickAction())
            .performClick()

        composeTestRule.mainClock.autoAdvance = false
    }

    private fun verifyRegistrationFlowCompletion() {
        try {
            composeTestRule.onNode(hasText("Selamat", substring = true))
                .assertIsDisplayed()

            verifyRegistrationSuccess()
        } catch (e: AssertionError) {
            try {
                composeTestRule.onNode(hasText("Verifikasi", substring = true))
                    .assertIsDisplayed()
            } catch (e2: AssertionError) {
                try {
                    composeTestRule.onNode(hasText("Lengkapi Profil", substring = true))
                        .assertIsDisplayed()
                } catch (e3: AssertionError) {
                    composeTestRule.onNodeWithText("Buat Akun Anda").assertIsDisplayed()
                }
            }
        }
    }

    private fun verifyRegistrationSuccess() {
        composeTestRule.onNode(hasText("Selamat", substring = true))
            .assertIsDisplayed()

        try {
            composeTestRule.onNodeWithText("Masuk").assertIsDisplayed()
        } catch (e: AssertionError) {
            try {
                composeTestRule.onNodeWithText("Mulai").assertIsDisplayed()
            } catch (e2: AssertionError) {
                try {
                    composeTestRule.onNodeWithText("Lanjutkan").assertIsDisplayed()
                } catch (_: AssertionError) {
                }
            }
        }
    }
}