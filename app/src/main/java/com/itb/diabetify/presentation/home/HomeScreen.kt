package com.itb.diabetify.presentation.home

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.home.components.BarChartEntry
import com.itb.diabetify.presentation.home.components.BarChartV2
import com.itb.diabetify.presentation.home.components.MeasurementCard
import com.itb.diabetify.presentation.home.components.HomeCard
import com.itb.diabetify.presentation.home.components.RiskIndicator
import com.itb.diabetify.presentation.home.components.StatItem
import com.itb.diabetify.presentation.home.components.formatDisplayTime
import com.itb.diabetify.presentation.home.components.formatRelativeTime
import com.itb.diabetify.presentation.home.components.getActivityAverageColor
import com.itb.diabetify.presentation.home.components.getBmiCategory
import com.itb.diabetify.presentation.home.components.getBmiCategoryColor
import com.itb.diabetify.presentation.home.components.getBrinkmanIndexColor
import com.itb.diabetify.presentation.home.components.getSmokingBackgroundColor
import com.itb.diabetify.presentation.home.components.getSmokingTextColor
import com.itb.diabetify.presentation.home.risk_detail.components.RiskCategory
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.ui.theme.poppinsFontFamily
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
) {
    // Navigation Event
    val navigationEvent = viewModel.navigationEvent.value
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let {
            when (it) {
                "SURVEY_SCREEN" -> {
                    navController.navigate(Route.SurveyScreen.route)
                    viewModel.onNavigationHandled()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Selamat Datang Kembali,",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.primary)
                    )
                    Text(
                        text = viewModel.userNameState.value.split(" ").firstOrNull() ?: "Pengguna",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = colorResource(id = R.color.primary)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_calendar),
                            contentDescription = "Date",
                            tint = colorResource(id = R.color.gray),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
                        val formattedDate = dateFormat.format(Date())
                        Text(
                            text = formattedDate,
                            fontSize = 14.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(id = R.color.gray)
                        )
                    }
                }
            }

            // Quick Stats Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.primary).copy(alpha = 0.05f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(
                        label = "Pemeriksaan Terakhir",
                        value = formatRelativeTime(viewModel.lastPredictionAtState.value),
                        icon = Icons.Outlined.Info
                    )
                }
            }

            // Last update text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .clickable {

                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Terakhir diperbarui: ${formatDisplayTime(viewModel.lastPredictionAtState.value)}",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = Color(0xFF6B7280)
                )
            }

            // Risk Percentage Card
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                HomeCard(
                    title = "Persentase Resiko",
                    hasWarning = true,
                    riskPercentage = viewModel.latestPredictionScoreState.value.toFloatOrNull()?.times(100) ?: 0f
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        RiskIndicator(
                            percentage = viewModel.latestPredictionScoreState.value.toFloatOrNull()?.times(100)?.toInt() ?: 0,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        RiskCategory(
                            modifier = Modifier.padding(vertical = 5.dp),
                            color = when {
                                (viewModel.latestPredictionScoreState.value.toFloatOrNull()
                                    ?.times(100)?.toInt() ?: 0) <= 30 -> viewModel.lowRiskColor
                                (viewModel.latestPredictionScoreState.value.toFloatOrNull()
                                    ?.times(100)?.toInt() ?: 0) <= 50 -> viewModel.mediumRiskColor
                                (viewModel.latestPredictionScoreState.value.toFloatOrNull()
                                    ?.times(100)?.toInt() ?: 0) <= 65 -> viewModel.highRiskColor
                                else -> viewModel.veryHighRiskColor
                            },
                            description = when {
                                (viewModel.latestPredictionScoreState.value.toFloatOrNull()
                                    ?.times(100)?.toInt() ?: 0) <= 30 -> "Diperkirakan 14 dari 100 orang dengan skor ini akan mengidap Diabetes"
                                (viewModel.latestPredictionScoreState.value.toFloatOrNull()
                                    ?.times(100)?.toInt() ?: 0) <= 50 -> "Diperkirakan 26 dari 100 orang dengan skor ini akan mengidap Diabetes"
                                (viewModel.latestPredictionScoreState.value.toFloatOrNull()
                                    ?.times(100)?.toInt() ?: 0) <= 65 -> "Diperkirakan 43 dari 100 orang dengan skor ini akan mengidap Diabetes"
                                else -> "Diperkirakan 63 dari 100 orang dengan skor ini akan mengidap Diabetes"
                            },
                            isHighlighted = true
                        )

                        PrimaryButton(
                            text = "Lihat Detail",
                            onClick = {
                                navController.navigate(Route.RiskDetailScreen.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                        )
                    }
                }
            }

            // Risk Factor Contribution Card
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            ) {
                HomeCard(
                    title = "Kontribusi Faktor Risiko"
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (viewModel.latestPredictionState.value.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(50.dp),
                                color = colorResource(id = R.color.primary)
                            )
                        } else {
//                            PieChart(
//                                riskFactors = viewModel.riskFactors.value,
//                                centerText = "Faktor\nRisiko",
//                                modifier = Modifier.fillMaxWidth()
//                            )

                            BarChartV2(
                                entries = viewModel.riskFactors.value.mapIndexed { index, riskFactor ->
                                    BarChartEntry(
                                        label = riskFactor.name,
                                        abbreviation = riskFactor.abbreviation,
                                        value = riskFactor.percentage.toFloat(),
                                        isNegative = riskFactor.percentage < 0
                                    )
                                }
                            )
                        }

                        PrimaryButton(
                            text = "Lihat Detail",
                            onClick = {
                                navController.navigate(Route.RiskFactorDetailScreen.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .height(50.dp)
                        )
                    }
                }
            }

            // What-If Simulation Card
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            ) {
                HomeCard(
                    title = "Simulasi What-If"
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFECFDF5)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Info,
                                        contentDescription = "Info",
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Potensi penurunan risiko",
                                        fontSize = 14.sp,
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF059669)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Jika Anda mengurangi konsumsi rokok dan meningkatkan aktivitas fisik, risiko diabetes dapat turun hingga",
                                    fontSize = 12.sp,
                                    fontFamily = poppinsFontFamily,
                                    color = Color(0xFF065F46)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "-17%",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = poppinsFontFamily,
                                    color = Color(0xFF059669),
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        Text(
                            text = "Simulasikan bagaimana perubahan gaya hidup dapat mempengaruhi risiko diabetes Anda",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        PrimaryButton(
                            text = "Mulai Simulasi",
                            onClick = {
                                 navController.navigate(Route.WhatIfScreen.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                                .height(50.dp)
                        )
                    }
                }
            }

            // Today's Data Section
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            ) {
                Text(
                    text = "Data Hari Ini",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier.padding(top = 20.dp)
                )
            }

            // BMI Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color.Gray.copy(alpha = 0.2f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.white)
                )
            ) {
                val bmi = viewModel.bmiValueState.value
                val bmiValue = bmi.toDoubleOrNull() ?: 0.0
                val bmiCategory = getBmiCategory(bmiValue)
                val bmiColor = getBmiCategoryColor(bmiValue)

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "BMI",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.primary),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = bmiCategory,
                                color = bmiColor,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                            )
                        }

                        Text(
                            text = String.format("%.1f", bmiValue),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = bmiColor,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // BMI progress indicator
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE5E7EB))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFF59E0B), // Underweight
                                            Color(0xFF10B981), // Normal
                                            Color(0xFFF59E0B), // Overweight
                                            Color(0xFFEF4444)  // Obese
                                        )
                                    )
                                )
                        )

                        // BMI indicator
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .offset(
                                    x = ((bmiValue - 10) / 30f * (1f - 12f/360f)) * 360.dp,
                                )
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    // BMI scale labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Kurus",
                            fontSize = 10.sp,
                            fontFamily = poppinsFontFamily,
                            color = Color(0xFFF59E0B),
                        )
                        Text(
                            text = "Normal",
                            fontSize = 10.sp,
                            fontFamily = poppinsFontFamily,
                            color = Color(0xFF10B981)
                        )
                        Text(
                            text = "Gemuk",
                            fontSize = 10.sp,
                            fontFamily = poppinsFontFamily,
                            color = Color(0xFFF59E0B)
                        )
                        Text(
                            text = "Obesitas",
                            fontSize = 10.sp,
                            fontFamily = poppinsFontFamily,
                            color = Color(0xFFEF4444)
                        )
                    }
                }
            }

            // Height and Weight Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MeasurementCard(
                    modifier = Modifier.weight(1f),
                    label = "Berat",
                    value = viewModel.weightValueState.value,
                    unit = "kg",
                    changeIndicator = "+0.5",
                    trend = "up"
                )

                MeasurementCard(
                    modifier = Modifier.weight(1f),
                    label = "Tinggi",
                    value = viewModel.heightValueState.value,
                    unit = "cm",
                    changeIndicator = "0",
                    trend = "stable"
                )
            }

            // Health Status Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color.Gray.copy(alpha = 0.2f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.white)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Status Kesehatan",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.primary),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isHypertension = viewModel.isHypertensionState.value
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isHypertension != "false") Icons.Outlined.Warning else Icons.Outlined.CheckCircle,
                                contentDescription = if (isHypertension != "false") "Warning" else "Check",
                                tint = if (isHypertension != "false") Color(0xFFD97706) else colorResource(id = R.color.primary),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Hipertensi",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.primary),
                            )
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (!isHypertension.toBoolean()) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = if (isHypertension.toBoolean()) "Ya" else "Tidak",
                                color = if (!isHypertension.toBoolean()) Color(0xFF16A34A) else Color(0xFFDC2626),
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        color = Color(0xFFE5E7EB)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isCholesterol = viewModel.isCholesterolValueState.value
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isCholesterol != "false") Icons.Outlined.Warning else Icons.Outlined.CheckCircle,
                                contentDescription = if (isCholesterol != "false") "Warning" else "Check",
                                tint = if (isCholesterol != "false") Color(0xFFD97706) else colorResource(id = R.color.primary),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Kolesterol",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.primary),
                            )
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (!isCholesterol.toBoolean()) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = if (isCholesterol.toBoolean()) "Ya" else "Tidak",
                                color = if (!isCholesterol.toBoolean()) Color(0xFF16A34A) else Color(0xFFDC2626),
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // History Status Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color.Gray.copy(alpha = 0.2f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.white)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Riwayat Kesehatan",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.primary),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isBloodline = viewModel.isBloodlineValueState.value
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isBloodline != "false") Icons.Outlined.Warning else Icons.Outlined.CheckCircle,
                                contentDescription = if (isBloodline != "false") "Warning" else "Check",
                                tint = if (isBloodline != "false") Color(0xFFD97706) else colorResource(id = R.color.primary),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Riwayat Keluarga",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.primary),
                            )
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (!isBloodline.toBoolean()) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = if (isBloodline.toBoolean()) "Ya" else "Tidak",
                                color = if (!isBloodline.toBoolean()) Color(0xFF16A34A) else Color(0xFFDC2626),
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        color = Color(0xFFE5E7EB)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isMacrosomicBaby = viewModel.isMacrosomicBabyState.value
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isMacrosomicBaby != "false") Icons.Outlined.Warning else Icons.Outlined.CheckCircle,
                                contentDescription = if (isMacrosomicBaby != "false") "Warning" else "Check",
                                tint = if (isMacrosomicBaby != "false") Color(0xFFD97706) else colorResource(id = R.color.primary),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Riwayat Kehamilan",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.primary),
                            )
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (!isMacrosomicBaby.toBoolean()) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = if (isMacrosomicBaby.toBoolean()) "Ya" else "Tidak",
                                color = if (!isMacrosomicBaby.toBoolean()) Color(0xFF16A34A) else Color(0xFFDC2626),
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Lifestyle Factors Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color.Gray.copy(alpha = 0.2f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.white)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Faktor Gaya Hidup",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.primary),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Smoking Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Jumlah Rokok Hari Ini",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.primary),
                            )
                        }

                        val smokingValue = viewModel.smokeValueState.value
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = getSmokingBackgroundColor(smokingValue.toIntOrNull() ?: 0)
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = "$smokingValue batang",
                                color = getSmokingTextColor(smokingValue.toIntOrNull() ?: 0),
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val smokingStatus = viewModel.smokingStatusValueState.value
                        val brinkmanIndex = viewModel.brinkmanIndexValueState.value

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Status Merokok",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                text = when (smokingStatus) {
                                    "0" -> "Tidak Pernah"
                                    "1" -> "Berhenti Merokok"
                                    "2" -> "Aktif Merokok"
                                    else -> "Tidak Diketahui"
                                },
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = when (smokingStatus) {
                                    "0" -> Color(0xFF10B981)
                                    "1" -> Color(0xFFF59E0B)
                                    "2" -> Color(0xFFEF4444)
                                    else -> Color(0xFF6B7280)
                                }
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Indeks Brinkman",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                text = String.format("%.1f", brinkmanIndex.toDoubleOrNull() ?: 0.0),
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = getBrinkmanIndexColor(brinkmanIndex.toDoubleOrNull() ?: 0.0)
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        color = Color(0xFFE5E7EB)
                    )

                    // Physical Activity Section
                    val workoutValue = viewModel.physicalActivityValueState.value
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Aktivitas Fisik Hari Ini",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.primary),
                        )

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (workoutValue.toBoolean()) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = if (workoutValue.toBoolean()) "Ya" else "Tidak",
                                color = if (workoutValue.toBoolean()) Color(0xFF16A34A) else Color(0xFFDC2626),
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val physicalActivityAverage = viewModel.physicalActivityAverageValueState.value
                    val averageValue = physicalActivityAverage.toIntOrNull() ?: 0

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Rata-rata Aktivitas Fisik (7 Hari)",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280),
                            )

                            Text(
                                text = "$averageValue/7 hari",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = getActivityAverageColor(averageValue),
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFE5E7EB))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(averageValue / 7f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(getActivityAverageColor(averageValue))
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "0 hari",
                                fontSize = 10.sp,
                                fontFamily = poppinsFontFamily,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                text = "Target: 7 hari",
                                fontSize = 10.sp,
                                fontFamily = poppinsFontFamily,
                                color = Color(0xFF6B7280)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = when {
                                averageValue >= 5 -> "Sangat Aktif"
                                averageValue >= 3 -> "Cukup Aktif"
                                averageValue >= 1 -> "Kurang Aktif"
                                else -> "Tidak Aktif"
                            },
                            fontSize = 12.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            color = getActivityAverageColor(averageValue),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            // Tips Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color.Gray.copy(alpha = 0.2f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEFF6FF)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Tips",
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tips Hari Ini",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF3B82F6)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Untuk menurunkan risiko hipertensi, kurangi asupan garam hingga kurang dari 5 gram per hari dan perbanyak konsumsi buah dan sayuran.",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color(0xFF1E3A8A),
                        lineHeight = 20.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}