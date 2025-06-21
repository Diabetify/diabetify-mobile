package com.itb.diabetify.presentation.register

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.ErrorNotification
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.common.SuccessNotification
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    // States
    val otpState by viewModel.otpState
    val errorMessage = viewModel.errorMessage.value
    val successMessage = viewModel.successMessage.value
    val isLoading = viewModel.verifyOtpState.value.isLoading
    val isResendLoading = viewModel.sendVerificationState.value.isLoading
    val maxLength = 6

    // Timer for resend code
    var resendTimerActive by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(60) }
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

    // Request focus on OTP input field
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Navigation event
    val navigationEvent = viewModel.navigationEvent.value
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let {
            when (it) {
                "SUCCESS_SCREEN" -> {
                    navController.navigate(Route.RegisterSuccessScreen.route)
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
                text = "Masukkan Kode OTP",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.primary)
            )

            Text(
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 0.dp, bottom = 25.dp),
                text = "Kode verifikasi telah dikirim ke Alamat Email Anda",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.gray)
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
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

            Spacer(modifier = Modifier.height(20.dp))

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

        // Verify button
        PrimaryButton(
            text = "Verifikasi",
            onClick = {
                val isValid = viewModel.validateOtpFields()
                if (isValid) {
                    viewModel.verifyOtp()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp),
            enabled = otpState.error == null && otpState.text.length == maxLength && !isLoading,
            isLoading = isLoading
        )

        // Error notification
        ErrorNotification(
            showError = errorMessage != null,
            errorMessage = errorMessage,
            onDismiss = { viewModel.onErrorShown() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1000f)
        )

        // Success notification
        SuccessNotification(
            showSuccess = successMessage != null,
            successMessage = successMessage,
            onDismiss = { viewModel.onErrorShown() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1000f)
        )
    }
}