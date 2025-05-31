package com.itb.diabetify.presentation.history

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.itb.diabetify.R
import com.itb.diabetify.presentation.history.components.HorizontalCalendar
import com.itb.diabetify.presentation.history.components.LineGraph
import com.itb.diabetify.presentation.history.components.DailySummary
import com.itb.diabetify.presentation.history.components.DailySummaryData
import com.itb.diabetify.presentation.history.components.RiskFactorContribution
import com.itb.diabetify.presentation.history.components.DailyInput
import com.itb.diabetify.ui.theme.poppinsFontFamily
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel
) {
    val context = LocalContext.current

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
                    Toast.makeText(context, "Selected date: ${date.format(formatter)}", Toast.LENGTH_SHORT).show()
                },
            )

            LineGraph(
                currentDay = 15,
                improvementData = listOf(
                    10f, 12f, 11f, 14f, 16f, 15f, 18f, 20f, 19f, 22f,
                    24f, 23f, 26f, 28f, 27f, 30f, 32f, 31f, 34f, 36f,
                    35f, 38f, 40f, 39f, 42f, 44f, 43f, 46f, 48f, 47f
                ),
            )

            // Daily Summary Section
            DailySummary(
                summaryData = DailySummaryData(
                    date = if (viewModel.dateState.value.isNotEmpty()) {
                        LocalDate.parse(viewModel.dateState.value)
                    } else {
                        LocalDate.now()
                    },
                    riskPercentage = 65f,
                    riskFactorContributions = listOf(
                        RiskFactorContribution("BMI", 25f, true),
                        RiskFactorContribution("Tekanan Darah", 15f, true),
                        RiskFactorContribution("Aktivitas Fisik", -10f, false),
                        RiskFactorContribution("Pola Makan", 20f, true),
                        RiskFactorContribution("Riwayat Keluarga", 15f, true)
                    ),
                    dailyInputs = listOf(
                        DailyInput("Berat Badan", "68 kg", true),
                        DailyInput("Tekanan Darah", "130/85 mmHg", true),
                        DailyInput("Gula Darah", "Belum diisi", false),
                        DailyInput("Aktivitas Fisik", "30 menit", true),
                        DailyInput("Konsumsi Obat", "Sudah", true)
                    )
                )
            )
        }
    }
}