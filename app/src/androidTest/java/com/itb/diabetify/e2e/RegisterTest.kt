package com.itb.diabetify.e2e

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.itb.diabetify.presentation.common.FieldState
import com.itb.diabetify.presentation.register.RegisterScreen
import com.itb.diabetify.presentation.register.RegisterViewModel
import com.itb.diabetify.ui.theme.DiabetifyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import io.mockk.every
import io.mockk.mockk
import androidx.navigation.NavController

@RunWith(AndroidJUnit4::class)
class RegisterTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun registerFlow_E2E_Test() {
        val mockNavController: NavController = mockk(relaxed = true)
        
        val mockViewModel: RegisterViewModel = mockk(relaxed = true)
        
        every { mockViewModel.nameFieldState } returns mutableStateOf(FieldState())
        every { mockViewModel.emailFieldState } returns mutableStateOf(FieldState())
        every { mockViewModel.passwordFieldState } returns mutableStateOf(FieldState())
        every { mockViewModel.privacyPolicyChecked } returns mutableStateOf(false)
        every { mockViewModel.passwordVisible } returns mutableStateOf(false)
        every { mockViewModel.navigationEvent } returns mutableStateOf(null)
        
        every { mockViewModel.setName(any()) } returns Unit
        every { mockViewModel.setEmail(any()) } returns Unit
        every { mockViewModel.setPassword(any()) } returns Unit
        every { mockViewModel.setPrivacyPolicy(any()) } returns Unit
        every { mockViewModel.togglePasswordVisibility() } returns Unit
        every { mockViewModel.validateRegisterFields() } returns true
        every { mockViewModel.onNavigationHandled() } returns Unit

        composeTestRule.setContent {
            DiabetifyTheme {
                RegisterScreen(
                    navController = mockNavController,
                    viewModel = mockViewModel
                )
            }
        }

        composeTestRule.waitForIdle()

        verifyRegisterScreenDisplayed()
    }

    private fun verifyRegisterScreenDisplayed() {
        composeTestRule.onNodeWithText("Halo,").assertIsDisplayed()
        composeTestRule.onNodeWithText("Buat Akun Anda").assertIsDisplayed()

        composeTestRule.onNodeWithText("Nama Anda").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Kata Sandi").assertIsDisplayed()

        composeTestRule.onNode(hasText("Dengan melanjutkan, Anda menyetujui", substring = true)).assertIsDisplayed()
        composeTestRule.onNode(hasText("Kebijakan Privasi", substring = true)).assertIsDisplayed()
        composeTestRule.onNode(hasText("Ketentuan Penggunaan", substring = true)).assertIsDisplayed()

        composeTestRule.onNodeWithText("Daftar").assertIsDisplayed()

        composeTestRule.onNode(hasText("Sudah memiliki akun?", substring = true)).assertIsDisplayed()
        composeTestRule.onNode(hasText("Masuk", substring = true)).assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Register Logo").assertIsDisplayed()
    }
}