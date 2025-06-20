package com.itb.diabetify.presentation.whatif

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.CustomizableButton
import com.itb.diabetify.presentation.common.DropdownField
import com.itb.diabetify.presentation.common.InputField
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun WhatIfScreen(
    navController: NavController,
    viewModel: WhatIfViewModel
) {
    val scrollState = rememberScrollState()
    val state = viewModel.state.value
    val navigationEvent = viewModel.navigationEvent.value

    LaunchedEffect(navigationEvent) {
        navigationEvent?.let {
            when (it) {
                "WHAT_IF_RESULT_SCREEN" -> {
                    navController.navigate(Route.WhatIfResultScreen.route)
                    viewModel.onNavigationHandled()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = colorResource(id = R.color.primary)
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Simulasi What If",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.primary)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            // Description Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.primary).copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = colorResource(id = R.color.primary),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Ubah faktor risiko di bawah ini untuk melihat bagaimana perubahan tersebut mempengaruhi risiko diabetes Anda.",
                        fontFamily = poppinsFontFamily,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.primary),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Data Tidak Dapat Diubah",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(id = R.color.primary)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Age field
            Text(
                text = "Usia",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary),
            )
            InputField(
                value = state.age.toString(),
                onValueChange = { },
                placeholderText = "Usia",
                iconResId = R.drawable.ic_baby,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Macrosomic baby field
            Text(
                text = "Pernah melahirkan bayi > 4kg",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary),
            )
            InputField(
                value = viewModel.getMacrosomicBabyText(state.isMacrosomicBaby),
                onValueChange = { },
                placeholderText = "Pernah melahirkan bayi > 4kg",
                iconResId = R.drawable.ic_baby,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Years smoking
            Text(
                text = "Berapa tahun sudah merokok",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary),
            )
            InputField(
                value = state.yearsSmokingOriginal.toString(),
                onValueChange = { },
                placeholderText = "Berapa tahun sudah merokok",
                iconResId = R.drawable.ic_calendar,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bloodline
            Text(
                text = "Riwayat keluarga diabetes",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary),
            )
            InputField(
                value = if (state.isBloodline) "Ya" else "Tidak",
                onValueChange = { },
                placeholderText = "Riwayat keluarga diabetes",
                iconResId = R.drawable.ic_family,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                color = colorResource(id = R.color.gray_3),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Faktor Yang Dapat Diubah",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(id = R.color.primary)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Smoking Status
            Text(
                text = "Status merokok",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary),
            )
            DropdownField(
                selectedOption = viewModel.getSmokingStatusText(state.smokingStatus),
                onOptionSelected = { selectedText ->
                    val status = when (selectedText) {
                        "Tidak Pernah Merokok" -> 0
                        "Berhenti Merokok" -> 1
                        "Aktif Merokok" -> 2
                        else -> state.smokingStatus
                    }
                    viewModel.updateSmokingStatus(status)
                },
                options = viewModel.getAllowedSmokingTransitionsPublic(state.originalSmokingStatus).map { 
                    viewModel.getSmokingStatusText(it) 
                },
                placeHolderText = "Status merokok",
                iconResId = R.drawable.ic_smoking,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Average cigarettes
            if (state.smokingStatus > 0) {
                Text(
                    text = "Rata-rata rokok per hari",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.primary),
                )
                InputField(
                    value = state.averageCigarettes,
                    onValueChange = { viewModel.updateAverageCigarettes(it) },
                    placeholderText = "Rata-rata rokok per hari",
                    iconResId = R.drawable.ic_smoking,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Weight
            Text(
                text = "Berat badan (kg)",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary),
            )
            InputField(
                value = state.weight,
                onValueChange = { viewModel.updateWeight(it) },
                placeholderText = "Berat badan (kg)",
                iconResId = R.drawable.ic_weight,
                keyboardType = KeyboardType.Decimal,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Hypertension
            Text(
                text = "Hipertensi",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary),
            )
            DropdownField(
                selectedOption = if (state.isHypertension) "Ya" else "Tidak",
                onOptionSelected = { selectedText ->
                    viewModel.updateHypertension(selectedText == "Ya")
                },
                options = listOf("Tidak", "Ya"),
                placeHolderText = "Hipertensi",
                iconResId = R.drawable.ic_hypertension,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Physical Activity
            Text(
                text = "Aktivitas fisik per minggu (hari)",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary),
            )
            InputField(
                value = state.physicalActivityFrequency,
                onValueChange = { viewModel.updatePhysicalActivity(it) },
                placeholderText = "Aktivitas fisik per minggu (hari)",
                iconResId = R.drawable.ic_walk,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Cholesterol
            Text(
                text = "Kolesterol",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary),
            )
            DropdownField(
                selectedOption = if (state.isCholesterol) "Ya" else "Tidak",
                onOptionSelected = { selectedText ->
                    viewModel.updateCholesterol(selectedText == "Ya")
                },
                options = listOf("Tidak", "Ya"),
                placeHolderText = "Kolesterol",
                iconResId = R.drawable.ic_cholesterol,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Error message
            if (state.errorMessage.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = state.errorMessage,
                        fontFamily = poppinsFontFamily,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomizableButton(
                    text = "Reset",
                    onClick = { viewModel.resetToOriginal() },
                    backgroundColor = Color.Gray,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                )

                PrimaryButton(
                    text = "Hitung",
                    onClick = { viewModel.calculateWhatIfPrediction() },
                    enabled = !state.isLoading,
                    isLoading = state.isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}