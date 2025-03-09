package com.itb.diabetify.presentation.forgot_password

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.InputField
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.presentation.register.RegisterViewModel
import com.itb.diabetify.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay

@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel
) {
    val passwordState = viewModel.passwordState.value
    val otpState = viewModel.otpState.value
    var passwordVisible by remember { mutableStateOf(false) }
    val maxLength = 6

    var resendTimerActive by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(60) }

    LaunchedEffect(resendTimerActive) {
        if (resendTimerActive) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            resendTimerActive = false
            timeLeft = 60
        }
    }

    val navigationEvent = viewModel.navigationEvent.value
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let {
            when (it) {
                "SUCCESS_SCREEN" -> {
                    navController.navigate(Route.ResetPasswordSuccessScreen.route)
                    viewModel.onNavigationHandled()
                }
            }
        }
    }

    val context = LocalContext.current
    val errorMessage = viewModel.errorMessage.value
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.onErrorShown()
        }
    }

    val isLoading = viewModel.changePasswordState.value.isLoading
    val isResendLoading = viewModel.sendVerificationState.value.isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
    ) {
        Image(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopCenter)
                .offset(y = (10).dp),
            painter = painterResource(id = R.drawable.verification),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 157.dp)
                .clip(RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp))
                .background(colorResource(id = R.color.white)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 25.dp, bottom = 5.dp),
                text = "Ubah Kata Sandi",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.primary)
            )

            Text(
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 0.dp, bottom = 25.dp),
                text = "Masukkan kata sandi baru dan kode OTP yang telah dikirimkan ke email Anda",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.gray)
            )

            // Password field
            InputField(
                value = passwordState.text,
                onValueChange = {
                    viewModel.setPassword(it)
                },
                placeholderText = "Kata Sandi Baru",
                iconResId = R.drawable.ic_lock,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp),
                keyboardType = KeyboardType.Password,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
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
                isError = passwordState.error != null,
                errorMessage = passwordState.error ?: ""
            )

            Spacer(modifier = Modifier.height(30.dp))

            // OTP Input Fields
            BasicTextField(
                value = TextFieldValue(
                    text = otpState.text,
                    selection = TextRange(otpState.text.length)
                ),
                onValueChange = { newValue ->
                    viewModel.setOtp(newValue.text)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 1.sp,
                    color = Color.Transparent
                ),
                decorationBox = { innerTextField ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (i in 0 until maxLength) {
                            val char = when {
                                i < otpState.text.length -> otpState.text[i].toString()
                                else -> ""
                            }

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        color = colorResource(id = R.color.white_2),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (i == otpState.text.length && otpState.text.length < maxLength)
                                            colorResource(id = R.color.primary)
                                        else
                                            Color.Transparent,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            ) {
                                Text(
                                    text = char,
                                    fontSize = 24.sp,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.primary),
                                    textAlign = TextAlign.Center
                                )
                            }

                            if (i < maxLength - 1) {
                                Spacer(modifier = Modifier.width(5.dp))
                            }
                        }
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Resend code with timer
            Row(
                modifier = Modifier
                    .padding(horizontal = 30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .clickable(enabled = !resendTimerActive && !isResendLoading) {
                            if (!resendTimerActive) {
                                viewModel.sendVerification(isResend = true)
                                resendTimerActive = true
                                timeLeft = 60
                            }
                        },
                    text = buildAnnotatedString {
                        append("Belum menerima kode? ")
                        withStyle(
                            style = SpanStyle(
                                color = if (resendTimerActive || isResendLoading)
                                    Color.Gray
                                else
                                    Color(0xFF648C9C),
                            )
                        ) {
                            append("Kirim ulang")
                        }
                    },
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.primary)
                )

                if (resendTimerActive) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${timeLeft}d)",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Change password button
        PrimaryButton(
            text = "Ubah Kata Sandi",
            onClick = {
                val isValid = viewModel.validateChangePasswordFields()
                if (isValid) {
                    viewModel.changePassword()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp),
            enabled = otpState.error == null && otpState.text.length == maxLength && passwordState.error == null && !isLoading,
            isLoading = isLoading
        )
    }
}