package com.itb.diabetify.presentation.home.risk_factor_detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.home.HomeViewModel
import com.itb.diabetify.presentation.home.components.PieChart
import com.itb.diabetify.ui.theme.poppinsFontFamily

data class RiskFactor(
    val name: String,
    val fullName: String,
    val impactPercentage: Float,
    val color: Color,
    val description: String,
    val idealValue: String,
    val currentValue: String,
    val isModifiable: Boolean = true
)

@Composable
fun RiskFactorDetailScreen(
    navController: NavController,
    viewModel: HomeViewModel,
) {
    val scrollState = rememberScrollState()
//    val riskFactors = listOf(
//        RiskFactor(
//            name = "IMT",
//            fullName = "Indeks Massa Tubuh",
//            impactPercentage = 35.2f,
//            color = Color(0xFFFF0000),
//            description = "Indeks Massa Tubuh adalah pengukuran yang menggunakan berat dan tinggi badan untuk mengestimasikan jumlah lemak tubuh. IMT yang lebih tinggi dikaitkan dengan risiko yang lebih besar untuk berbagai penyakit.",
//            idealValue = "18.5 - 24.9 kg/m²",
//            currentValue = "28.4 kg/m²"
//        ),
//        RiskFactor(
//            name = "HTN",
//            fullName = "Hipertensi",
//            impactPercentage = 25.8f,
//            color = Color(0xFFE63946),
//            description = "Hipertensi atau tekanan darah tinggi adalah kondisi medis kronis dengan tekanan darah di arteri meningkat. Tanpa pengobatan, hipertensi meningkatkan risiko penyakit jantung dan stroke.",
//            idealValue = "< 120/80 mmHg",
//            currentValue = "145/90 mmHg"
//        ),
//        RiskFactor(
//            name = "RK",
//            fullName = "Riwayat Kelahiran",
//            impactPercentage = 12.5f,
//            color = Color(0xFFB23A48),
//            description = "Faktor riwayat kelahiran termasuk berat badan lahir, kelahiran prematur, atau komplikasi kelahiran lainnya yang dapat memengaruhi risiko kesehatan di masa depan.",
//            idealValue = "Berat lahir normal, kelahiran cukup bulan",
//            currentValue = "Riwayat kelahiran prematur"
//        ),
//        RiskFactor(
//            name = "AF",
//            fullName = "Aktivitas Fisik",
//            impactPercentage = 10.3f,
//            color = Color(0xFFC1666B),
//            description = "Aktivitas fisik mengacu pada tingkat olahraga dan gerakan fisik yang dilakukan secara rutin. Aktivitas fisik yang cukup membantu mengurangi risiko berbagai penyakit kronis.",
//            idealValue = "Min. 150 menit aktivitas sedang per minggu",
//            currentValue = "60 menit per minggu"
//        ),
//        RiskFactor(
//            name = "U",
//            fullName = "Usia",
//            impactPercentage = 9.2f,
//            color = Color(0xFF0000FF),
//            description = "Usia adalah faktor risiko yang tidak dapat dimodifikasi namun memiliki pengaruh signifikan terhadap risiko kesehatan. Risiko berbagai penyakit meningkat seiring bertambahnya usia.",
//            idealValue = "Tidak dapat dimodifikasi",
//            currentValue = "58 tahun",
//            isModifiable = false
//        ),
//        RiskFactor(
//            name = "IM",
//            fullName = "Indeks Merokok",
//            impactPercentage = 7.0f,
//            color = Color(0xFF4361EE),
//            description = "Indeks Merokok mengukur kebiasaan merokok seseorang termasuk jumlah dan durasi merokok. Merokok meningkatkan risiko berbagai penyakit kardiovaskular dan kanker.",
//            idealValue = "0 (tidak merokok)",
//            currentValue = "10 batang per hari"
//        )
//    )

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
                text = "Perhitungan faktor risiko",
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
            SummarySection(viewModel.riskFactors)

            Spacer(modifier = Modifier.height(10.dp))

//            riskFactors.forEach { factor ->
//                RiskFactorCard(
//                    riskFactor = factor,
//                    onManageClick = { }
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            RecommendationsCard()
        }
    }
}

@Composable
fun SummarySection(riskFactors: List<HomeViewModel.RiskFactor>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Risk Pie Chart
            PieChart(
                riskFactors = riskFactors,
                centerText = "Faktor\nRisiko",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Penjelasan:",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = colorResource(id = R.color.primary)
            )

            Text(
                text = "Faktor IMT berkontribusi sebesar 35% dari total risiko Anda.",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = colorResource(id = R.color.primary)
            )
        }
    }
}

@Composable
fun RiskFactorCard(
    riskFactor: RiskFactor,
    onManageClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(riskFactor.color, RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${riskFactor.name}: ${riskFactor.fullName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = riskFactor.color.copy(alpha = 0.15f)
                    )
                ) {
                    Text(
                        text = "${riskFactor.impactPercentage}%",
                        fontWeight = FontWeight.Bold,
                        color = riskFactor.color,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Text(
                text = riskFactor.description,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Progress indicator for comparison between current and ideal values
            if (riskFactor.isModifiable) {
                ComparisonBar(riskFactor)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Current and Ideal Values
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nilai Ideal",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )

                    Text(
                        text = riskFactor.idealValue,
                        fontSize = 14.sp
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nilai Anda",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )

                    val isHighRisk = riskFactor.impactPercentage > 15f
                    Text(
                        text = riskFactor.currentValue,
                        fontSize = 14.sp,
                        color = if (isHighRisk && riskFactor.isModifiable) Color.Red else Color.Black
                    )
                }
            }

            // Management button for major risk factors
            if (riskFactor.impactPercentage > 15f && riskFactor.isModifiable) {
                Button(
                    onClick = onManageClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = riskFactor.color
                    )
                ) {
                    Text("Kelola ${riskFactor.name}", modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun ComparisonBar(riskFactor: RiskFactor) {
    // This is a simple visualization for comparing current vs ideal values
    // In a real app, you would parse the numeric values and create appropriate comparison
    val isNumber = riskFactor.currentValue.any { it.isDigit() }

    if (!isNumber) return

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Perbandingan dengan Nilai Ideal",
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        val currentValueText = riskFactor.currentValue.filter { it.isDigit() || it == '.' }
        val currentValue = currentValueText.toFloatOrNull() ?: 0f

        // For simplicity, we're extracting the first number from the ideal value
        val idealValueText = riskFactor.idealValue.filter { it.isDigit() || it == '.' }
        val idealValue = idealValueText.toFloatOrNull() ?: 0f

        // Skip if we couldn't parse values
        if (currentValue == 0f || idealValue == 0f) return

        val ratio = when (riskFactor.name) {
            "AF" -> currentValue / idealValue // Higher is better (more activity)
            else -> idealValue / currentValue // Lower is better (for BMI, BP, smoking)
        }

        val progress = (ratio * 100).coerceIn(0f, 100f)
        val color = when {
            progress > 80f -> Color.Green
            progress > 50f -> Color(0xFFFFA500) // Orange
            else -> Color.Red
        }

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress / 100)
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(color)
            )
        }

        // Progress label
        Text(
            text = when {
                progress > 80f -> "Baik"
                progress > 50f -> "Perlu Perbaikan"
                else -> "Jauh dari Ideal"
            },
            fontSize = 12.sp,
            color = color,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun RecommendationsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Rekomendasi Tindakan",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            RecommendationItem(
                title = "Kurangi Indeks Massa Tubuh (IMT)",
                description = "Menurunkan berat badan hingga mencapai IMT normal (18.5-24.9) dapat mengurangi risiko sebesar 20%.",
                priority = "Tinggi"
            )

            RecommendationItem(
                title = "Kelola Hipertensi",
                description = "Menurunkan tekanan darah hingga di bawah 120/80 mmHg dapat mengurangi risiko sebesar 15%.",
                priority = "Tinggi"
            )

            RecommendationItem(
                title = "Tingkatkan Aktivitas Fisik",
                description = "Mencapai 150 menit aktivitas fisik sedang per minggu dapat mengurangi risiko sebesar 8%.",
                priority = "Sedang"
            )

            RecommendationItem(
                title = "Kurangi atau Hentikan Merokok",
                description = "Berhenti merokok sepenuhnya dapat mengurangi risiko sebesar 7%.",
                priority = "Sedang"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Navigate to action plan */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2D4356)
                )
            ) {
                Text("Lihat Rencana Tindakan Detail", modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
fun RecommendationItem(
    title: String,
    description: String,
    priority: String
) {
    val priorityColor = when (priority) {
        "Tinggi" -> Color.Red
        "Sedang" -> Color(0xFFFFA500) // Orange
        else -> Color.Green
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(priorityColor.copy(alpha = 0.2f))
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = priorityColor,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.DarkGray
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "Prioritas:",
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.width(4.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = priorityColor.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = priority,
                        fontSize = 12.sp,
                        color = priorityColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }

    Divider(modifier = Modifier.padding(start = 44.dp, top = 8.dp))
}