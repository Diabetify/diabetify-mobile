package com.itb.diabetify.e2e.presentation.login

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
class LoginTest {

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

    private lateinit var testHelper: LoginTestHelper

    @Before
    fun setUp() {
        hiltRule.inject()
        testHelper = LoginTestHelper(composeTestRule)
    }

    @Test
    fun loginFlow_Complete() {
        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginForm(
            email = "john.doe@example.com",
            password = "SecurePassword123"
        )

        testHelper.clickLoginButton()

        testHelper.verifyLoginFlowCompletion()
    }

    @Test
    fun loginWithEmptyEmail_ShowsValidationError() {
        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginFormWithValidation(
            email = "",
            password = "SecurePassword123",
            expectValidation = true
        )

        testHelper.clickLoginButton()

        testHelper.waitForErrorMessage("Email tidak valid")
        testHelper.verifyStillOnLoginScreen()
    }

    @Test
    fun loginWithInvalidEmail_ShowsValidationError() {
        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginFormWithValidation(
            email = "invalid-email",
            password = "SecurePassword123",
            expectValidation = true
        )

        testHelper.clickLoginButton()

        testHelper.waitForErrorMessage("Email tidak valid")
        testHelper.verifyStillOnLoginScreen()
    }

    @Test
    fun loginWithShortPassword_ShowsValidationError() {
        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginFormWithValidation(
            email = "john.doe@example.com",
            password = "123",
            expectValidation = true
        )

        testHelper.clickLoginButton()

        testHelper.waitForErrorMessage("Kata sandi harus lebih dari 8 karakter")
        testHelper.verifyStillOnLoginScreen()
    }

    @Test
    fun loginWithWrongPassword_ShowsErrorMessage() {
        fakeRepo.shouldFailLogin = true
        fakeRepo.loginErrorType = "wrong_password"

        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginForm(
            email = "john.doe@example.com",
            password = "WrongPassword123"
        )

        testHelper.clickLoginButton()

        testHelper.waitForErrorMessage("Password salah")
        testHelper.verifyStillOnLoginScreen()
    }

    @Test
    fun loginWithUnregisteredAccount_ShowsErrorMessage() {
        fakeRepo.shouldFailLogin = true
        fakeRepo.loginErrorType = "account_not_found"

        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginForm(
            email = "nonexistent@example.com",
            password = "SecurePassword123"
        )

        testHelper.clickLoginButton()

        testHelper.waitForErrorMessage("Akun tidak ditemukan")
        testHelper.verifyStillOnLoginScreen()
    }

    @Test
    fun loginWithNetworkError_ShowsErrorMessage() {
        fakeRepo.shouldFailLogin = true
        fakeRepo.loginErrorType = "network_error"

        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginForm(
            email = "john.doe@example.com",
            password = "SecurePassword123"
        )

        testHelper.clickLoginButton()

        testHelper.waitForErrorMessage("Network error occurred")
        testHelper.verifyStillOnLoginScreen()
    }

    @Test
    fun passwordVisibilityToggle_WorksCorrectly() {
        testHelper.startAppAndNavigateToLogin()
        testHelper.testPasswordVisibilityToggle()
    }

    @Test
    fun navigateToRegister_WorksCorrectly() {
        testHelper.startAppAndNavigateToLogin()
        testHelper.navigateToRegister()
    }

    @Test
    fun navigateToForgotPassword_WorksCorrectly() {
        testHelper.startAppAndNavigateToLogin()
        testHelper.navigateToForgotPassword()
    }

    @Test
    fun loginButtonEnabled_WhenFieldsEmpty() {
        testHelper.startAppAndNavigateToLogin()
        testHelper.verifyLoginButtonEnabled()
    }

    @Test
    fun loginButtonDisabled_AfterInvalidEmailValidation() {
        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginForm(
            email = "invalid-email",
            password = "SecurePassword123"
        )

        testHelper.verifyLoginButtonEnabled()

        testHelper.clickLoginButton()

        testHelper.verifyLoginButtonDisabled()
        testHelper.verifyStillOnLoginScreen()
    }

    @Test
    fun loginButtonDisabled_AfterShortPasswordValidation() {
        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginForm(
            email = "john.doe@example.com",
            password = "123"
        )

        testHelper.verifyLoginButtonEnabled()

        testHelper.clickLoginButton()

        testHelper.verifyLoginButtonDisabled()
        testHelper.verifyStillOnLoginScreen()
    }

    @Test
    fun loginWithLongPassword_WorksCorrectly() {
        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginForm(
            email = "john.doe@example.com",
            password = "ThisIsAVeryLongPasswordWithMoreThan8CharactersAndSpecialSymbols!@#123"
        )

        testHelper.clickLoginButton()

        testHelper.verifyLoginFlowCompletion()
    }

    @Test
    fun loginFormFieldValidation_RealTime() {
        testHelper.startAppAndNavigateToLogin()
        testHelper.testRealTimeValidation()
    }

    @Test
    fun loginButtonDisabled_AfterBothFieldsInvalid() {
        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginForm(
            email = "invalid-email",
            password = "123"
        )

        testHelper.verifyLoginButtonEnabled()

        testHelper.clickLoginButton()

        testHelper.verifyLoginButtonDisabled()
        testHelper.verifyStillOnLoginScreen()
    }

    @Test
    fun loginWithEmailContainingNumbers_WorksCorrectly() {
        testHelper.startAppAndNavigateToLogin()

        testHelper.fillLoginForm(
            email = "user123@domain456.com",
            password = "SecurePassword123"
        )

        testHelper.clickLoginButton()

        testHelper.verifyLoginFlowCompletion()
    }
}