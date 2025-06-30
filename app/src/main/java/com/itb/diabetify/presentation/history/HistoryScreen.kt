package com.itb.diabetify.presentation.history

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.ErrorNotification
import com.itb.diabetify.presentation.history.components.HorizontalCalendar
import com.itb.diabetify.presentation.history.components.LineGraph
import com.itb.diabetify.presentation.history.components.DailySummary
import com.itb.diabetify.presentation.history.components.DailySummaryData
import com.itb.diabetify.presentation.history.components.RiskFactorContribution
import com.itb.diabetify.presentation.history.components.DailyInput
import com.itb.diabetify.presentation.history.components.PredictionScoreEntry
import com.itb.diabetify.ui.theme.poppinsFontFamily
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi", "DefaultLocale")
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel
) {
    val predictionScores = viewModel.predictionScores.collectAsState(initial = emptyList())
    val currentPrediction = viewModel.currentPrediction.collectAsState(initial = null)
    val errorMessage = viewModel.errorMessage.value
    val isLoading = viewModel.getPredictionByDateState.value.isLoading || viewModel.getPredictionScoreByDateState.value.isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                colorResource(id = R.color.primary),
                                colorResource(id = R.color.primary).copy(alpha = 0.8f)
                            )
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Column {
                    Text(
                        text = "Riwayat",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            HorizontalCalendar(
                modifier = Modifier,
                onDateClickListener = { date ->
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    viewModel.setDate(date.format(formatter))
                },
            )

            LineGraph(
                currentDay = calculateCurrentDayForGraph(
                    selectedDate = viewModel.dateState.value,
                    predictionScores = predictionScores.value
                ),
                predictionScores = predictionScores.value
            )

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = colorResource(id = R.color.primary)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Memuat data prediksi...",
                            fontFamily = poppinsFontFamily,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.primary),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (currentPrediction.value == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📊",
                            fontSize = 48.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Tidak Ada Data Prediksi",
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = colorResource(id = R.color.black),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Data prediksi untuk tanggal yang dipilih tidak tersedia. Silakan pilih tanggal lain atau lakukan prediksi terlebih dahulu.",
                            fontFamily = poppinsFontFamily,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.gray),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            } else {
                currentPrediction.value?.let { prediction ->
                    DailySummary(
                        summaryData = DailySummaryData(
                            date = if (viewModel.dateState.value.isNotEmpty()) {
                                LocalDate.parse(viewModel.dateState.value)
                            } else {
                                LocalDate.now()
                            },
                            riskPercentage = (prediction.riskScore * 100).toFloat(),
                            riskFactorContributions = listOf(
                                RiskFactorContribution(
                                    "Indeks Massa Tubuh",
                                    (String.format("%.1f", prediction.bmiContribution * 100)),
                                    prediction.bmiImpact == 1
                                ),
                                RiskFactorContribution(
                                    "Riwayat Hipertensi",
                                    (String.format("%.1f", prediction.isHypertensionContribution * 100)),
                                    prediction.isHypertensionImpact == 1
                                ),
                                RiskFactorContribution(
                                    "Riwayat Bayi Makrosomia",
                                    (String.format("%.1f", prediction.isMacrosomicBabyContribution * 100)),
                                    prediction.isMacrosomicBabyImpact == 1
                                ),
                                RiskFactorContribution(
                                    "Aktivitas Fisik",
                                    (String.format("%.1f", prediction.physicalActivityFrequencyContribution * 100)),
                                    prediction.physicalActivityFrequencyImpact == 1
                                ),
                                RiskFactorContribution(
                                    "Usia",
                                    (String.format("%.1f", prediction.ageContribution * 100)),
                                    prediction.ageImpact == 1
                                ),
                                RiskFactorContribution(
                                    "Status Merokok",
                                    (String.format("%.1f", prediction.smokingStatusContribution * 100)),
                                    prediction.smokingStatusImpact == 1
                                ),
                                RiskFactorContribution(
                                    "Indeks Brinkman",
                                    (String.format("%.1f", prediction.brinkmanScoreContribution * 100)),
                                    prediction.brinkmanScoreImpact == 1
                                ),
                                RiskFactorContribution(
                                    "Riwayat Keluarga",
                                    (String.format("%.1f", prediction.isBloodlineContribution * 100)),
                                    prediction.isBloodlineImpact == 1
                                ),
                                RiskFactorContribution(
                                    "Kolesterol",
                                    (String.format("%.1f", prediction.isCholesterolContribution * 100)),
                                    prediction.isCholesterolImpact == 1
                                )
                            ),
                            dailyInputs = listOf(
                                DailyInput("Usia", "${prediction.age} tahun"),
                                DailyInput("Indeks Massa Tubuh", "${prediction.bmi} kg/m²"),
                                DailyInput("Hipertensi",
                                    if (prediction.isHypertension) "Ya" else "Tidak"
                                ),
                                DailyInput(
                                    "Kolesterol",
                                    if (prediction.isCholesterol) "Ya" else "Tidak"
                                ),
                                DailyInput(
                                    "Riwayat Keluarga",
                                    if (prediction.isBloodline) "Ya" else "Tidak"
                                ),
                                DailyInput(
                                    "Riwayat Bayi Makrosomia",
                                    if (prediction.isMacrosomicBaby == 1) "Ya" else "Tidak"
                                ),
                                DailyInput(
                                    "Status Merokok",
                                    when (prediction.smokingStatus) {
                                        "0" -> "Tidak Pernah"
                                        "1" -> "Sudah Berhenti"
                                        "2" -> "Masih Merokok"
                                        else -> "Tidak Diketahui"
                                    }
                                ),
                                DailyInput(
                                    "Indeks Brinkman",
                                    "${prediction.brinkmanScore}"
                                ),
                                DailyInput(
                                    "Jumlah Rokok",
                                    "${prediction.avgSmokeCount} batang / hari"
                                ),
                                DailyInput(
                                    "Frekuensi Aktivitas Fisik",
                                    "${prediction.physicalActivityFrequency}x / minggu"
                                ),
                            )
                        )
                    )
                }
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
    }
}

@SuppressLint("NewApi")
private fun calculateCurrentDayForGraph(
    selectedDate: String,
    predictionScores: List<PredictionScoreEntry>
): Int? {
    return when {
        predictionScores.isEmpty() -> null

        selectedDate.isEmpty() -> predictionScores.maxByOrNull { it.day }?.day

        selectedDate.isNotEmpty() -> {
            try {
                val selected = LocalDate.parse(selectedDate)
                val today = LocalDate.now()

                val daysDifference = java.time.temporal.ChronoUnit.DAYS.between(selected, today).toInt()
                val targetDay = predictionScores.maxByOrNull { it.day }?.day?.minus(daysDifference)

                if (targetDay != null && predictionScores.any { it.day == targetDay }) {
                    targetDay
                } else {
                    predictionScores.maxByOrNull { it.day }?.day
                }
            } catch (e: Exception) {
                predictionScores.maxByOrNull { it.day }?.day
            }
        }

        else -> null
    }
}