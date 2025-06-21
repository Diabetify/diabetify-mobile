package com.itb.diabetify.presentation.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.itb.diabetify.BuildConfig
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.Divider
import com.itb.diabetify.presentation.common.ErrorNotification
import com.itb.diabetify.presentation.common.InputField
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.ui.theme.poppinsFontFamily
import com.itb.diabetify.util.AuthResultContract

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel
) {
    // States
    val emailState by viewModel.emailState
    val passwordState by viewModel.passwordState
    var passwordVisible by remember { mutableStateOf(false) }
    val errorMessage = viewModel.errorMessage.value
    val isLoading = viewModel.loginState.value.isLoading

    // Navigation event
    val navigationEvent = viewModel.navigationEvent.value
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

    // Google Sign-In client setup
    val context = LocalContext.current
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .build()

        GoogleSignIn.getClient(context, gso)
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
                text = "Selamat Datang Kembali",
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
                // Email field
                InputField(
                    value = emailState.text,
                    onValueChange = { viewModel.setEmail(it) },
                    placeholderText = "Email",
                    iconResId = R.drawable.ic_message,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Email,
                    isError = emailState.error != null,
                    errorMessage = emailState.error ?: "",
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                InputField(
                    value = passwordState.text,
                    onValueChange = { viewModel.setPassword(it) },
                    placeholderText = "Kata Sandi",
                    iconResId = R.drawable.ic_lock,
                    modifier = Modifier.fillMaxWidth(),
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
                    errorMessage = passwordState.error ?: "",
                )

                // Forgot password
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp, top = 20.dp)
                ) {
                    Text(
                        text = "Lupa kata sandi?",
                        fontSize = 12.sp,
                        fontFamily = poppinsFontFamily,
                        textDecoration = TextDecoration.Underline,
                        lineHeight = 15.sp,
                        color = colorResource(id = R.color.gray_2),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                navController.navigate(Route.ForgotPasswordScreen.route)
                            }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 30.dp, end = 30.dp)
        ) {
            PrimaryButton(
                text = "Masuk",
                onClick = {
                    val isValid = viewModel.validateLoginFields()
                    if (isValid) {
                        viewModel.login()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = emailState.error == null && passwordState.error == null && !isLoading,
                leftImageResId = R.drawable.ic_enter,
                isLoading = isLoading
            )

            Divider(
                text = "Atau",
                modifier = Modifier.padding(vertical = 10.dp)
            )

            // Login with google and facebook
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                val signInRequestCode = 1
                val authResultLauncher =
                    rememberLauncherForActivityResult(contract = AuthResultContract(googleSignInClient)) {
                        try {
                            val account = it?.getResult(ApiException::class.java)
                            if (account == null) {
                                Log.e("GoogleSignIn", "Account is null")
                            } else {
                                viewModel.googleLogin(account.idToken!!)
                            }
                        } catch (e: ApiException) {
                            Log.d("GoogleSignIn", "signInResult:failed code=" + e.statusCode)
                        }
                    }

                Surface(
                    onClick = { authResultLauncher.launch(signInRequestCode) },
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(15.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, colorResource(id = R.color.gray_3))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google icon",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(25.dp))

                Surface(
                    onClick = { /* TODO: Handle Facebook login */ },
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(15.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, colorResource(id = R.color.gray_3))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_facebook),
                            contentDescription = "Facebook icon",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            }

            Text(
                text = buildAnnotatedString {
                    append("Belum memiliki akun? ")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF648C9C),
                        )
                    ) {
                        append("Daftar")
                    }
                },
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                color = colorResource(id = R.color.black),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth().padding(vertical = 20.dp)
                    .clickable {
                        navController.navigate(Route.RegisterScreen.route)
                    }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.register),
            contentDescription = "Login Logo",
            modifier = Modifier
                .width(150.dp)
                .align(Alignment.TopCenter)
                .offset(y = 15.dp)
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
    }
}