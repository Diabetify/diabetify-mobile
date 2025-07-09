package com.itb.diabetify.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.ErrorNotification
import com.itb.diabetify.presentation.common.LoadingNotification
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.common.SuccessNotification
import com.itb.diabetify.presentation.home.components.BarChartEntry
import com.itb.diabetify.presentation.home.components.BarChart
import com.itb.diabetify.presentation.home.components.MeasurementCard
import com.itb.diabetify.presentation.home.components.HomeCard
import com.itb.diabetify.presentation.home.components.PieChart
import com.itb.diabetify.presentation.home.components.RiskCategory
import com.itb.diabetify.presentation.home.components.RiskIndicator
import com.itb.diabetify.presentation.home.components.StatItem
import com.itb.diabetify.presentation.home.components.formatDisplayTime
import com.itb.diabetify.presentation.home.components.formatRelativeTime
import com.itb.diabetify.presentation.home.components.getActivityAverageColor
import com.itb.diabetify.presentation.home.components.getBmiCategory
import com.itb.diabetify.presentation.home.components.getBmiCategoryColor
import com.itb.diabetify.presentation.home.components.getRiskCategoryColor
import com.itb.diabetify.presentation.home.components.getRiskCategoryDescription
import com.itb.diabetify.presentation.home.components.getSmokingBackgroundColor
import com.itb.diabetify.presentation.home.components.getSmokingTextColor
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.ui.theme.poppinsFontFamily
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
) {
    // States
    val userName by viewModel.userName
    val lastPredictionAt by viewModel.lastPredictionAt
    val latestPredictionScore by viewModel.latestPredictionScore
    val riskFactors by viewModel.riskFactors
    val successMessage = viewModel.successMessage.value
    val errorMessage = viewModel.errorMessage.value
    val loadingMessage = viewModel.loadingMessage.value

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

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                            text = userName.split(" ").first(),
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
                            value = formatRelativeTime(lastPredictionAt),
                            icon = Icons.Outlined.Info
                        )
                    }
                }

                // Last update
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
                        text = "Terakhir diperbarui: ${formatDisplayTime(lastPredictionAt)}",
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
                        title = "Persentase Risiko",
                        hasWarning = true,
                        riskPercentage = latestPredictionScore
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            RiskIndicator(
                                percentage = latestPredictionScore,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            RiskCategory(
                                modifier = Modifier.padding(vertical = 5.dp),
                                color = getRiskCategoryColor(latestPredictionScore),
                                description = getRiskCategoryDescription(latestPredictionScore),
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
                                var selectedTabIndex by remember { mutableIntStateOf(0) }
                                val tabTitles = listOf("Grafik Batang", "Grafik Lingkaran")

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    tabTitles.forEachIndexed { index, title ->
                                        Card(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    selectedTabIndex = index
                                                },
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (selectedTabIndex == index) {
                                                    colorResource(id = R.color.primary)
                                                } else {
                                                    Color(0xFFF3F4F6)
                                                }
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = title,
                                                fontFamily = poppinsFontFamily,
                                                fontWeight = if (selectedTabIndex == index) FontWeight.SemiBold else FontWeight.Medium,
                                                fontSize = 12.sp,
                                                color = if (selectedTabIndex == index) {
                                                    Color.White
                                                } else {
                                                    Color(0xFF6B7280)
                                                },
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                                            )
                                        }
                                    }
                                }

                                // Chart
                                when (selectedTabIndex) {
                                    0 -> {
                                        BarChart(
                                            entries = riskFactors.mapIndexed { _, riskFactor ->
                                                BarChartEntry(
                                                    label = riskFactor.name,
                                                    abbreviation = riskFactor.abbreviation,
                                                    value = riskFactor.percentage,
                                                    isNegative = riskFactor.percentage < 0
                                                )
                                            }
                                        )
                                    }
                                    1 -> {
                                        PieChart(
                                            riskFactors = riskFactors,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
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
                    val bmi by viewModel.bmi
                    val bmiCategory = getBmiCategory(bmi)
                    val bmiColor = getBmiCategoryColor(bmi)

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
                                text = String.format("%.1f", bmi),
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = bmiColor,
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // BMI progress indicator
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            val totalWidth = maxWidth
                            val labelWidth = 60.dp

                            Text(
                                text = "Normal",
                                fontSize = 10.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(labelWidth)
                                    .wrapContentHeight()
                                    .offset(
                                        x = (0.32f * totalWidth) - (labelWidth / 2),
                                        y = 0.dp
                                    )
                            )

                            Text(
                                text = "Obesitas I",
                                fontSize = 10.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF17C0B),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(labelWidth)
                                    .wrapContentHeight()
                                    .offset(
                                        x = (0.55f * totalWidth) - (labelWidth / 2),
                                        y = 0.dp
                                    )
                            )

                            // Progress bar
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .offset(y = 20.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFFE5E7EB))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                0f to Color(0xFFF59E0B),
                                                0.185f to Color(0xFFF59E0B),
                                                0.185f to Color(0xFF10B981),
                                                0.46f to Color(0xFF10B981),
                                                0.46f to Color(0xFFF7B13D),
                                                0.5f to Color(0xFFF7B13D),
                                                0.5f to Color(0xFFF17C0B),
                                                0.6f to Color(0xFFF17C0B),
                                                0.6f to Color(0xFFEF4444),
                                                1f to Color(0xFFEF4444)
                                            )
                                        )
                                )

                                val indicatorPosition = when {
                                    bmi < 18.5f -> (bmi / 50f) * 0.185f
                                    bmi < 23f -> 0.185f + ((bmi - 18.5f) / (23f - 18.5f)) * (0.46f - 0.185f)
                                    bmi < 25f -> 0.46f + ((bmi - 23f) / (25f - 23f)) * (0.5f - 0.46f)
                                    bmi < 30f -> 0.5f + ((bmi - 25f) / (30f - 25f)) * (0.6f - 0.5f)
                                    else -> 0.6f + ((bmi - 30f) / 20f) * (1f - 0.6f)
                                }

                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .offset(
                                            x = (indicatorPosition * totalWidth).coerceAtMost(totalWidth - 8.dp)
                                        )
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }

                            Text(
                                text = "Kurus",
                                fontSize = 10.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF59E0B),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(labelWidth)
                                    .wrapContentHeight()
                                    .offset(
                                        x = (0.092f * totalWidth) - (labelWidth / 2),
                                        y = 24.dp
                                    )
                            )

                            Text(
                                text = "Beresiko",
                                fontSize = 10.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF7B13D),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(labelWidth)
                                    .wrapContentHeight()
                                    .offset(
                                        x = (0.48f * totalWidth) - (labelWidth / 2),
                                        y = 24.dp
                                    )
                            )

                            Text(
                                text = "Obesitas II",
                                fontSize = 10.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEF4444),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(labelWidth)
                                    .wrapContentHeight()
                                    .offset(
                                        x = (0.8f * totalWidth) - (labelWidth / 2),
                                        y = 24.dp
                                    )
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
                    val weight by viewModel.weight
                    val height by viewModel.height

                    MeasurementCard(
                        modifier = Modifier.weight(1f),
                        label = "Berat",
                        value = weight,
                        unit = "kg",
                        changeIndicator = "+0.5",
                        trend = "up"
                    )

                    MeasurementCard(
                        modifier = Modifier.weight(1f),
                        label = "Tinggi",
                        value = height,
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
                            val isHypertension by viewModel.isHypertension
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isHypertension) Icons.Outlined.Warning else Icons.Outlined.CheckCircle,
                                    contentDescription = if (isHypertension) "Warning" else "Check",
                                    tint = if (isHypertension) Color(0xFFD97706) else colorResource(id = R.color.primary),
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
                                    containerColor = if (!isHypertension) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = if (isHypertension) "Ya" else "Tidak",
                                    color = if (!isHypertension) Color(0xFF16A34A) else Color(0xFFDC2626),
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
                            val isCholesterol by viewModel.isCholesterol
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isCholesterol) Icons.Outlined.Warning else Icons.Outlined.CheckCircle,
                                    contentDescription = if (isCholesterol) "Warning" else "Check",
                                    tint = if (isCholesterol) Color(0xFFD97706) else colorResource(id = R.color.primary),
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
                                    containerColor = if (!isCholesterol) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = if (isCholesterol) "Ya" else "Tidak",
                                    color = if (!isCholesterol) Color(0xFF16A34A) else Color(0xFFDC2626),
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
                            val isBloodline by viewModel.isBloodline
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isBloodline) Icons.Outlined.Warning else Icons.Outlined.CheckCircle,
                                    contentDescription = if (isBloodline) "Warning" else "Check",
                                    tint = if (isBloodline) Color(0xFFD97706) else colorResource(id = R.color.primary),
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
                                    containerColor = if (!isBloodline) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = if (isBloodline) "Ya" else "Tidak",
                                    color = if (!isBloodline) Color(0xFF16A34A) else Color(0xFFDC2626),
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
                            val isMacrosomicBaby by viewModel.macrosomicBaby
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isMacrosomicBaby == 1) Icons.Outlined.Warning else Icons.Outlined.CheckCircle,
                                    contentDescription = if (isMacrosomicBaby == 1) "Warning" else "Check",
                                    tint = if (isMacrosomicBaby == 1) Color(0xFFD97706) else colorResource(id = R.color.primary),
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
                                    containerColor = if (isMacrosomicBaby != 1) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = if (isMacrosomicBaby == 1) "Ya" else "Tidak",
                                    color = if (isMacrosomicBaby != 1) Color(0xFF16A34A) else Color(0xFFDC2626),
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

                            val smokingValue by viewModel.smoke
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = getSmokingBackgroundColor(smokingValue)
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(
                                    text = "$smokingValue batang",
                                    color = getSmokingTextColor(smokingValue),
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
                            val smokingStatus by viewModel.smokingStatus
                            val smokeAverage by viewModel.smokeAverage

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
                                    text = "Rata-rata Rokok per Hari",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = Color(0xFF6B7280)
                                )
                                Text(
                                    text = smokeAverage.toString(),
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = getSmokingTextColor(smokeAverage)
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
                        val workoutValue = viewModel.physicalActivityToday.value
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
                                    containerColor = if (workoutValue == 1) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = if (workoutValue == 1) "Ya" else "Tidak",
                                    color = if (workoutValue == 1) Color(0xFF16A34A) else Color(0xFFDC2626),
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val physicalActivityAverage = viewModel.physicalActivityAverage.value

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
                                    text = "$physicalActivityAverage/7 hari",
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = getActivityAverageColor(physicalActivityAverage),
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
                                        .fillMaxWidth(physicalActivityAverage / 7f)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(getActivityAverageColor(physicalActivityAverage))
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
                                    physicalActivityAverage >= 5 -> "Sangat Aktif"
                                    physicalActivityAverage >= 3 -> "Cukup Aktif"
                                    physicalActivityAverage >= 1 -> "Kurang Aktif"
                                    else -> "Tidak Aktif"
                                },
                                fontSize = 12.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                color = getActivityAverageColor(physicalActivityAverage),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }


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
            onDismiss = { viewModel.onSuccessShown() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1000f)
        )

        // Loading notification
        LoadingNotification(
            showLoading = loadingMessage != null,
            loadingMessage = loadingMessage,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1000f)
        )
    }
}