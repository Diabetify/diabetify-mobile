package com.itb.diabetify.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.itb.diabetify.presentation.common.InputField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.ui.theme.poppinsFontFamily
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.navgraph.Route

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    // States
    val nameFieldState by viewModel.nameFieldState
    val emailFieldState by viewModel.emailFieldState
    val passwordFieldState by viewModel.passwordFieldState
    val privacyPolicyChecked by viewModel.privacyPolicyChecked
    val passwordVisible by viewModel.passwordVisible

    // Navigation event
    val navigationEvent by viewModel.navigationEvent
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let {
            when (it) {
                "HOME_SCREEN" -> {
                    navController.navigate(Route.MainNavigation.route)
                    viewModel.onNavigationHandled()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 157.dp)
                .clip(RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp))
                .background(colorResource(id = R.color.white)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 25.dp, bottom = 5.dp),
                text = "Halo,",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.primary)
            )
            Text(
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 25.dp),
                text = "Buat Akun Anda",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.primary)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 30.dp, end = 30.dp)
            ) {
                // Name field
                InputField(
                    value = nameFieldState.text,
                    onValueChange = { viewModel.setName(it) },
                    placeholderText = "Nama Anda",
                    iconResId = R.drawable.ic_profile,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Text,
                    isError = nameFieldState.error != null,
                    errorMessage = nameFieldState.error ?: ""
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email field
                InputField(
                    value = emailFieldState.text,
                    onValueChange = { viewModel.setEmail(it) },
                    placeholderText = "Email",
                    iconResId = R.drawable.ic_message,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Email,
                    isError = emailFieldState.error != null,
                    errorMessage = emailFieldState.error ?: ""
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                InputField(
                    value = passwordFieldState.text,
                    onValueChange = { viewModel.setPassword(it) },
                    placeholderText = "Kata Sandi",
                    iconResId = R.drawable.ic_lock,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.togglePasswordVisibility() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible) R.drawable.ic_show else R.drawable.ic_hide
                                ),
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = Color.Gray
                            )
                        }
                    },
                    isError = passwordFieldState.error != null,
                    errorMessage = passwordFieldState.error ?: ""
                )

                // Privacy Policy Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 5.dp)
                ) {
                    Checkbox(
                        checked = privacyPolicyChecked,
                        onCheckedChange = { viewModel.setPrivacyPolicy(it) },
                        modifier = Modifier.testTag("PrivacyPolicyCheckbox")
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Dengan melanjutkan, Anda menyetujui ")
                            withStyle(
                                style = SpanStyle(
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("Kebijakan Privasi")
                            }
                            append(" dan ")
                            withStyle(
                                style = SpanStyle(
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("Ketentuan Penggunaan")
                            }
                            append(" kami.")
                        },
                        fontSize = 10.sp,
                        fontFamily = poppinsFontFamily,
                        lineHeight = 15.sp,
                        color = colorResource(id = R.color.gray_2),
                        modifier = Modifier.clickable { /* TODO: Open privacy policy link */ }
                    )
                }

                // Register Button
                PrimaryButton(
                    text = "Daftar",
                    onClick = {
                        val isValid = viewModel.validateRegisterFields()
                        if (isValid && privacyPolicyChecked) {
                            navController.navigate(Route.BiodataScreen.route)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    enabled = nameFieldState.error == null && emailFieldState.error == null && passwordFieldState.error == null && privacyPolicyChecked
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 30.dp, end = 30.dp)
        ) {
            // Login Button
            Text(
                text = buildAnnotatedString {
                    append("Sudah memiliki akun? ")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF648C9C),
                        )
                    ) {
                        append("Masuk")
                    }
                },
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                color = colorResource(id = R.color.black),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth().padding(vertical = 20.dp)
                        .clickable {
                        navController.navigate(Route.LoginScreen.route)
                    }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.register),
            contentDescription = "Register Logo",
            modifier = Modifier
                .width(150.dp)
                .align(Alignment.TopCenter)
                .offset(y = 15.dp)
        )
    }
}
