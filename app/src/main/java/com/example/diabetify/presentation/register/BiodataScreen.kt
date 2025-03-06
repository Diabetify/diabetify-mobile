package com.example.diabetify.presentation.register

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diabetify.R
import com.example.diabetify.presentation.common.DropdownField
import com.example.diabetify.presentation.common.InputField
import com.example.diabetify.presentation.common.PrimaryButton
import com.example.diabetify.presentation.register.components.DatePickerModal
import com.example.diabetify.ui.theme.poppinsFontFamily
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun BiodataScreen() {
    var gender by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    val showDatePicker = remember { mutableStateOf(false) }

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
                color = colorResource(id = R.color.primary)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 30.dp, end = 30.dp)
            ) {
                val options = listOf("Laki-laki", "Perempuan")

                // Gender dropdown
                DropdownField(
                    selectedOption = gender,
                    onOptionSelected = { gender = it },
                    options = options,
                    placeHolderText = "Pilih Jenis Kelamin",
                    iconResId = R.drawable.ic_users,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Birth date field
                InputField(
                    value = birthDate,
                    onValueChange = { /* No direct text editing */ },
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
                        if (birthDate.isNotEmpty()) {
                            IconButton(
                                onClick = { birthDate = "" },
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
                    }
                )

                // Date picker dialog
                if (showDatePicker.value) {
                    DatePickerModal(
                        onDateSelected = { dateMillis ->
                            dateMillis?.let {
                                val date = Date(it)
                                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                birthDate = formatter.format(date)
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
                        value = weight,
                        onValueChange = { weight = it },
                        placeholderText = "Berat Badan",
                        iconResId = R.drawable.ic_scale,
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number,
                        visualTransformation = VisualTransformation.None,
                        singleLine = true
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
                        value = height,
                        onValueChange = { height = it },
                        placeholderText = "Tinggi Badan",
                        iconResId = R.drawable.ic_swap,
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number,
                        visualTransformation = VisualTransformation.None,
                        singleLine = true
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
//                        navController.navigate(Route.BiodataScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp),
            enabled = true,
            rightImageResId = R.drawable.ic_chevron_right
        )
    }
}