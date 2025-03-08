package com.itb.diabetify.presentation.register

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.DropdownField
import com.itb.diabetify.presentation.common.InputField
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.presentation.register.components.DatePickerModal
import com.itb.diabetify.ui.theme.poppinsFontFamily
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun BiodataScreen(
    navController: NavController,
    viewModel: RegisterViewModel
) {
    val genderState = viewModel.genderState.value
    val birthDateState = viewModel.dobState.value
    val weightState = viewModel.weightState.value
    val heightState = viewModel.heightState.value
    val showDatePicker = remember { mutableStateOf(false) }

    val navigationEvent = viewModel.navigationEvent.value
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let {
            when (it) {
                "OTP_SCREEN" -> {
                    navController.navigate(Route.OtpScreen.route)
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

    val isLoading = viewModel.createAccountState.value.isLoading || viewModel.sendVerificationState.value.isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .offset(y = (10).dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.man),
                contentDescription = "Man",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(150.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.woman),
                contentDescription = "Woman",
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(150.dp)
            )
        }

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
                text = "Lengkapi Profil Anda",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.primary)
            )
            Text(
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 0.dp, bottom = 25.dp),
                text = "Bantu kami mengenal Anda lebih baik!",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.gray)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 30.dp, end = 30.dp)
            ) {
                val options = listOf("Laki-laki", "Perempuan")

                // Gender dropdown
                DropdownField(
                    selectedOption = genderState.text,
                    onOptionSelected = { viewModel.setGender(it) },
                    options = options,
                    placeHolderText = "Pilih Jenis Kelamin",
                    iconResId = R.drawable.ic_users,
                    modifier = Modifier.fillMaxWidth(),
                    isError = genderState.error != null,
                    errorMessage = genderState.error ?: ""
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Birth date field
                InputField(
                    value = birthDateState.text,
                    onValueChange = { },
                    placeholderText = "Tanggal Lahir",
                    iconResId = R.drawable.ic_calendar,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        showDatePicker.value = true
                    },
                    enabled = false,
                    trailingIcon = {
                        if (birthDateState.text.isNotEmpty()) {
                            IconButton(
                                onClick = { viewModel.setDob("") },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear date",
                                    tint = colorResource(id = R.color.gray),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    isError = birthDateState.error != null,
                    errorMessage = birthDateState.error ?: ""
                )

                // Date picker dialog
                if (showDatePicker.value) {
                    DatePickerModal(
                        onDateSelected = { dateMillis ->
                            dateMillis?.let {
                                val date = Date(it)
                                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                viewModel.setDob(formatter.format(date))
                            }
                        },
                        onDismiss = { showDatePicker.value = false }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Weight field
                    InputField(
                        value = weightState.text,
                        onValueChange = { viewModel.setWeight(it) },
                        placeholderText = "Berat Badan",
                        iconResId = R.drawable.ic_scale,
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number,
                        visualTransformation = VisualTransformation.None,
                        singleLine = true,
                        isError = weightState.error != null,
                        errorMessage = weightState.error ?: ""
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(15.dp),
                        color = colorResource(id = R.color.primary),
                        border = BorderStroke(1.dp, colorResource(id = R.color.gray_3))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "KG",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = colorResource(id = R.color.gray_3)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Height field
                    InputField(
                        value = heightState.text,
                        onValueChange = { viewModel.setHeight(it) },
                        placeholderText = "Tinggi Badan",
                        iconResId = R.drawable.ic_swap,
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number,
                        visualTransformation = VisualTransformation.None,
                        singleLine = true,
                        isError = heightState.error != null,
                        errorMessage = heightState.error ?: ""
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(15.dp),
                        color = colorResource(id = R.color.primary),
                        border = BorderStroke(1.dp, colorResource(id = R.color.gray_3))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "CM",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = colorResource(id = R.color.gray_3)
                            )
                        }
                    }
                }
            }
        }

        PrimaryButton(
            text = "Lanjut",
            onClick = {
                val isValid = viewModel.validateBiodataFields()
                if (isValid) {
                    viewModel.createAccount()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp),
            enabled = !isLoading,
            rightImageResId = R.drawable.ic_chevron_right,
            isLoading = isLoading
        )
    }
}