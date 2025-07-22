package com.itb.diabetify.e2e.presentation.forgot_password

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.itb.diabetify.MainActivity
import com.itb.diabetify.di.AppModule
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
class ForgotPasswordTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    @BindValue
    val fakeRepo: FakeAuthRepository = FakeAuthRepository()

    private lateinit var testHelper: ForgotPasswordTestHelper

    @Before
    fun setUp() {
        hiltRule.inject()
        testHelper = ForgotPasswordTestHelper(composeTestRule)
        fakeRepo.reset()
    }

    @Test
    fun forgotPasswordFlow_Complete() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.fillChangePasswordForm(
            newPassword = "NewSecurePassword123",
            otpCode = "123456"
        )

        testHelper.clickChangePasswordButton()
    }

    @Test
    fun forgotPasswordWithEmptyEmail_ShowsValidationError() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("")

        testHelper.clickSendCodeButton()

        testHelper.waitForErrorMessage("Email tidak valid")
        testHelper.verifyStillOnForgotPasswordScreen()
    }

    @Test
    fun forgotPasswordWithInvalidEmail_ShowsValidationError() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("invalid-email")

        testHelper.clickSendCodeButton()

        testHelper.waitForErrorMessage("Email tidak valid")
        testHelper.verifyStillOnForgotPasswordScreen()
    }

    @Test
    fun forgotPasswordWithUnregisteredEmail_ShowsErrorMessage() {
        fakeRepo.shouldFailSendVerification = true
        fakeRepo.sendVerificationErrorType = "email_not_found"

        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("nonexistent@example.com")

        testHelper.clickSendCodeButton()

        testHelper.waitForErrorMessage("Email tidak terdaftar")
        testHelper.verifyStillOnForgotPasswordScreen()
    }

    @Test
    fun forgotPasswordWithNetworkError_ShowsErrorMessage() {
        fakeRepo.shouldFailSendVerification = true
        fakeRepo.sendVerificationErrorType = "network_error"

        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.waitForErrorMessage("Network error occurred")
        testHelper.verifyStillOnForgotPasswordScreen()
    }

    @Test
    fun changePasswordWithShortPassword_ShowsValidationError() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.fillChangePasswordForm(
            newPassword = "123",
            otpCode = "123456"
        )

        testHelper.clickChangePasswordButton()

        testHelper.waitForErrorMessage("Kata sandi harus lebih dari 8 karakter")
        testHelper.verifyStillOnChangePasswordScreen()
    }

    @Test
    fun changePasswordWithInvalidOtp_ShowsErrorMessage() {
        fakeRepo.shouldFailChangePassword = true
        fakeRepo.changePasswordErrorType = "invalid_otp"

        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.fillChangePasswordForm(
            newPassword = "NewSecurePassword123",
            otpCode = "000000"
        )

        testHelper.clickChangePasswordButtonV2()

        testHelper.waitForErrorMessage("Kode OTP salah atau sudah kadaluarsa")
        testHelper.verifyStillOnChangePasswordScreen()
    }

    @Test
    fun changePasswordWithNetworkError_ShowsErrorMessage() {
        fakeRepo.shouldFailChangePassword = true
        fakeRepo.changePasswordErrorType = "network_error"

        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.fillChangePasswordForm(
            newPassword = "NewSecurePassword123",
            otpCode = "123456"
        )

        testHelper.clickChangePasswordButtonV2()

        testHelper.waitForErrorMessage("Network error occurred")
        testHelper.verifyStillOnChangePasswordScreen()
    }

    @Test
    fun sendCodeButtonEnabled_WhenEmailValid() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("valid@example.com")

        testHelper.verifySendCodeButtonEnabled()
    }

    @Test
    fun sendCodeButtonDisabled_AfterInvalidEmailValidation() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("invalid-email")

        testHelper.verifySendCodeButtonEnabled()

        testHelper.clickSendCodeButton()

        testHelper.verifySendCodeButtonDisabled()
        testHelper.verifyStillOnForgotPasswordScreen()
    }

    @Test
    fun changePasswordButtonDisabled_WhenFieldsIncomplete() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.fillChangePasswordForm(
            newPassword = "NewSecurePassword123",
            otpCode = "12345"
        )

        testHelper.verifyChangePasswordButtonDisabled()
    }

    @Test
    fun changePasswordButtonEnabled_WhenAllFieldsValid() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.fillChangePasswordForm(
            newPassword = "NewSecurePassword123",
            otpCode = "123456"
        )

        testHelper.verifyChangePasswordButtonEnabled()
    }

    @Test
    fun resendCodeFunction_WorksCorrectly() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.clickResendCode()

        testHelper.verifyResendCodeTimerActive()
    }

    @Test
    fun passwordVisibilityToggle_WorksCorrectly() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.testPasswordVisibilityToggle()
    }

    @Test
    fun forgotPasswordFormFieldValidation_RealTime() {
        testHelper.startAppAndNavigateToForgotPassword()
        testHelper.testRealTimeValidation()
    }

    @Test
    fun forgotPasswordWithLongPassword_WorksCorrectly() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.fillChangePasswordForm(
            newPassword = "ThisIsAVeryLongPasswordWithMoreThan8CharactersAndSpecialSymbols!@#123",
            otpCode = "123456"
        )

        testHelper.clickChangePasswordButton()

        testHelper.verifySuccessScreen()
    }

    @Test
    fun forgotPasswordWithEmailContainingNumbers_WorksCorrectly() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("user123@domain456.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.fillChangePasswordForm(
            newPassword = "NewSecurePassword123",
            otpCode = "123456"
        )

        testHelper.clickChangePasswordButton()

        testHelper.verifySuccessScreen()
    }

    @Test
    fun verifyNavigationFromForgotPasswordScreen() {
        testHelper.startAppAndNavigateToForgotPassword()
        testHelper.verifyForgotPasswordScreen()
    }

    @Test
    fun forgotPasswordWithIncompleteOtp_ShowsValidationError() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.fillChangePasswordForm(
            newPassword = "NewSecurePassword123",
            otpCode = "123"
        )

        testHelper.verifyChangePasswordButtonDisabled()
    }

    @Test
    fun changePasswordButtonDisabled_AfterPasswordValidationError() {
        testHelper.startAppAndNavigateToForgotPassword()

        testHelper.fillForgotPasswordEmail("john.doe@example.com")

        testHelper.clickSendCodeButton()

        testHelper.verifyChangePasswordScreen()

        testHelper.fillChangePasswordForm(
            newPassword = "123",
            otpCode = "123456"
        )

        testHelper.verifyChangePasswordButtonEnabled()

        testHelper.clickChangePasswordButton()

        testHelper.verifyChangePasswordButtonDisabled()
        testHelper.verifyStillOnChangePasswordScreen()
    }
}