package com.example.diabetify.presentation.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import com.example.diabetify.presentation.common.InputField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.diabetify.R
import com.example.diabetify.ui.theme.poppinsFontFamily
import com.example.diabetify.presentation.common.Divider
import com.example.diabetify.presentation.common.PrimaryButton
import com.example.diabetify.presentation.navgraph.Route

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }

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
                    value = name,
                    onValueChange = { name = it },
                    placeholderText = "Nama Anda",
                    iconResId = R.drawable.ic_profile,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Text,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email field
                InputField(
                    value = email,
                    onValueChange = { email = it },
                    placeholderText = "Email",
                    iconResId = R.drawable.ic_message,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Email,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                InputField(
                    value = password,
                    onValueChange = { password = it },
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
                    }
                )

                // Privacy Policy Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
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
                        modifier = Modifier.clickable { /* Open privacy policy link */ }
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
                text = "Daftar",
                onClick = {
                    navController.navigate(Route.BiodataScreen.route)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isChecked
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
                Surface(
                    onClick = { /* Handle Google login */ },
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
                    onClick = { /* Handle Google login */ },
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
                    .clickable { /* Open Login */ }
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
