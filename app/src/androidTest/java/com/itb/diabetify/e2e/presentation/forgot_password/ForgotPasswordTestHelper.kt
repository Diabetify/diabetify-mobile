package com.itb.diabetify.e2e.presentation.forgot_password

import android.annotation.SuppressLint
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.itb.diabetify.MainActivity

class ForgotPasswordTestHelper(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
) {

    @SuppressLint("CheckResult")
    fun startAppAndNavigateToForgotPassword() {
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
            composeTestRule.onNode(hasText("Sudah memiliki akun", substring = true))
                .assertIsDisplayed()
                .performClick()
            composeTestRule.waitForIdle()
        } catch (_: AssertionError) {
        }

        var attempts = 0
        val maxAttempts = 5

        while (attempts < maxAttempts) {
            try {
                composeTestRule.onNodeWithText("Halo,").assertIsDisplayed()
                composeTestRule.onNodeWithText("Selamat Datang Kembali").assertIsDisplayed()
                break
            } catch (e: AssertionError) {
                attempts++
                Thread.sleep(1000)
                composeTestRule.waitForIdle()

                if (attempts < maxAttempts) {
                    try {
                        composeTestRule.onNode(hasText("Masuk", substring = true))
                            .performClick()
                        composeTestRule.waitForIdle()
                    } catch (_: AssertionError) {
                    }
                }
            }
        }

        if (attempts >= maxAttempts) {
            throw AssertionError("Could not navigate to login screen after completing onboarding.")
        }

        composeTestRule.onNode(hasText("Lupa kata sandi", substring = true))
            .performClick()

        composeTestRule.waitForIdle()

        try {
            composeTestRule.onNode(hasText("Lupa Kata Sandi", substring = true))
                .assertIsDisplayed()
        } catch (e: AssertionError) {
            throw AssertionError("Could not navigate to forgot password screen")
        }
    }

    fun fillForgotPasswordEmail(email: String) {
        composeTestRule.onNodeWithTag("ForgotPasswordEmailTextField")
            .performClick()
            .performTextInput(email)

        composeTestRule.waitForIdle()
    }

    fun clickSendCodeButton() {
        composeTestRule.onNodeWithText("Kirim Kode")
            .performClick()
        composeTestRule.waitForIdle()
    }

    fun verifyForgotPasswordScreen() {
        composeTestRule.onNode(hasText("Lupa Kata Sandi", substring = true))
            .assertIsDisplayed()
        composeTestRule.onNode(hasText("Masukkan alamat email Anda untuk mendapatkan kode verifikasi", substring = true))
            .assertIsDisplayed()
    }

    fun verifyChangePasswordScreen() {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            try {
                composeTestRule.onNode(hasText("Masukkan kata sandi", substring = true))
                    .assertIsDisplayed()
                true
            } catch (e: AssertionError) {
                false
            }
        }
    }

    fun fillChangePasswordForm(newPassword: String, otpCode: String) {
        composeTestRule.onNodeWithTag("ChangePasswordNewPasswordTextField")
            .performClick()
            .performTextInput(newPassword)

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("ChangePasswordOtpTextField")
            .performClick()
            .performTextInput(otpCode)

        composeTestRule.waitForIdle()
    }

    fun clickChangePasswordButton() {
        composeTestRule.mainClock.autoAdvance = false

        composeTestRule.onNodeWithText("Ubah Sandi")
            .performClick()

        composeTestRule.mainClock.advanceTimeBy(100)
    }

    fun clickChangePasswordButtonV2() {
        composeTestRule.onNodeWithText("Ubah Sandi")
            .performClick()

        composeTestRule.waitForIdle()
    }

    fun verifySuccessScreen() {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            try {
                composeTestRule.onNode(hasText("Kata Sandi Berhasil Diubah", substring = true))
                    .assertIsDisplayed()
                true
            } catch (e: AssertionError) {
                false
            }
        }
    }

    fun clickGoToLoginButton() {
        composeTestRule.onNodeWithText("Masuk")
            .performClick()
        composeTestRule.waitForIdle()
    }

    fun verifyBackToLoginScreen() {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            try {
                composeTestRule.onNodeWithText("Halo,").assertIsDisplayed()
                composeTestRule.onNodeWithText("Selamat Datang Kembali").assertIsDisplayed()
                true
            } catch (e: AssertionError) {
                false
            }
        }
    }

    fun waitForErrorMessage(errorText: String, timeoutMs: Long = 5000) {
        composeTestRule.waitUntil(timeoutMillis = timeoutMs) {
            try {
                composeTestRule.onNode(hasText(errorText, substring = true))
                    .assertIsDisplayed()
                true
            } catch (e: AssertionError) {
                false
            }
        }
    }

    fun verifyStillOnForgotPasswordScreen() {
        composeTestRule.onNode(hasText("Lupa Kata Sandi", substring = true))
            .assertIsDisplayed()
    }

    fun verifyStillOnChangePasswordScreen() {
        composeTestRule.onNode(hasText("Masukkan kata sandi", substring = true))
            .assertIsDisplayed()
    }

    fun verifySendCodeButtonEnabled() {
        composeTestRule.onNodeWithText("Kirim Kode")
            .assertIsEnabled()
    }

    fun verifySendCodeButtonDisabled() {
        try {
            composeTestRule.onNodeWithText("Kirim Kode")
                .assertIsNotEnabled()
        } catch (e: AssertionError) {
            throw AssertionError("Send Code button should be disabled but it's enabled")
        }
    }

    fun verifyChangePasswordButtonEnabled() {
        composeTestRule.onNodeWithText("Ubah Sandi")
            .assertIsEnabled()
    }

    fun verifyChangePasswordButtonDisabled() {
        try {
            composeTestRule.onNodeWithText("Ubah Sandi")
                .assertIsNotEnabled()
        } catch (e: AssertionError) {
            throw AssertionError("Change Password button should be disabled but it's enabled")
        }
    }

    fun clickResendCode() {
        composeTestRule.onNode(hasText("Kirim ulang", substring = true))
            .performClick()
        composeTestRule.waitForIdle()
    }

    fun verifyResendCodeTimerActive() {
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            try {
                composeTestRule.onNode(hasText("(", substring = true))
                    .assertIsDisplayed()
                true
            } catch (e: AssertionError) {
                false
            }
        }
    }

    fun testPasswordVisibilityToggle() {
        composeTestRule.onNodeWithTag("ChangePasswordNewPasswordTextField")
            .performClick()
            .performTextInput("SecurePassword123")

        composeTestRule.waitForIdle()

        try {
            composeTestRule.onNode(hasText("Show password"))
                .assertIsDisplayed()
                .performClick()

            composeTestRule.waitForIdle()

            composeTestRule.onNode(hasText("Hide password"))
                .assertIsDisplayed()
        } catch (e: AssertionError) {
            try {
                composeTestRule.onNode(hasText("PasswordVisibilityIcon", substring = true))
                    .performClick()
                composeTestRule.waitForIdle()
            } catch (_: AssertionError) {
            }
        }
    }

    private fun clearForgotPasswordForm() {
        try {
            composeTestRule.onNodeWithTag("ForgotPasswordEmailTextField")
                .performClick()
                .performTextClearance()

            composeTestRule.waitForIdle()
        } catch (_: AssertionError) {
        }
    }

    fun testRealTimeValidation() {
        composeTestRule.onNodeWithTag("ForgotPasswordEmailTextField")
            .performClick()
            .performTextInput("invalid")

        composeTestRule.waitForIdle()
        Thread.sleep(500)

        verifySendCodeButtonEnabled()

        clickSendCodeButton()

        verifySendCodeButtonDisabled()

        clearForgotPasswordForm()

        composeTestRule.onNodeWithTag("ForgotPasswordEmailTextField")
            .performClick()
            .performTextInput("valid@example.com")

        composeTestRule.waitForIdle()

        verifySendCodeButtonEnabled()
    }
}