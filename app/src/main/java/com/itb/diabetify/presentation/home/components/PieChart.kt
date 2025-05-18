package com.itb.diabetify.presentation.home.components

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.itb.diabetify.R
import com.itb.diabetify.presentation.home.HomeViewModel
import com.itb.diabetify.ui.theme.poppinsFontFamily
import kotlin.math.abs

@Composable
fun PieChart(
    riskFactors: List<HomeViewModel.RiskFactor>,
    centerText: String? = null,
    holeRadius: Float = 30f,
    animationDuration: Int = 1000,
    modifier: Modifier
) {
    val context = LocalContext.current

    val poppinsBoldTypeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            ResourcesCompat.getFont(context, R.font.poppins_bold)
        } catch (e: Exception) {
            Typeface.DEFAULT_BOLD
        }
    } else {
        Typeface.DEFAULT_BOLD
    }

    val sortedRiskFactors = riskFactors.sortedByDescending { abs(it.percentage) }

    val dataPercentages = sortedRiskFactors.map { it.percentage }

    val maxPositiveValue = dataPercentages.filter { it >= 0 }.maxOrNull() ?: 1f
    val maxNegativeValue = dataPercentages.filter { it < 0 }.minOrNull()?.let { abs(it) } ?: 1f

    val chartColors = dataPercentages.map { percentage ->
        when {
            percentage >= 0 -> {
                val intensity = if (maxPositiveValue > 0) percentage / maxPositiveValue else 0f
                val red = (255 * (0.5f + 0.5f * intensity)).toInt()
                val green = (128 * (1 - intensity)).toInt()
                Color(red, green, green).toArgb()
            }
            else -> {
                val intensity = abs(percentage) / maxNegativeValue
                val blue = (255 * (0.5f + 0.5f * intensity)).toInt()
                val red = (128 * (1 - intensity)).toInt()
                Color(red, red, blue).toArgb()
            }
        }
    }

    Column(modifier = modifier.wrapContentHeight()) {
        // PieChart
        AndroidView(
            factory = { context ->
                PieChart(context).apply {
                    description.isEnabled = false
                    isDrawHoleEnabled = true
                    setHoleColor(Color.Transparent.toArgb())
                    this.holeRadius = holeRadius
                    transparentCircleRadius = holeRadius + 5f

                    if (centerText != null) {
                        setDrawCenterText(true)
                        this.centerText = centerText
                        setCenterTextSize(16f)
                        setCenterTextColor(Color.Black.toArgb())
                        setCenterTextTypeface(poppinsBoldTypeface)
                    } else {
                        setDrawCenterText(false)
                    }

                    legend.isEnabled = false

                    rotationAngle = 0f
                    isRotationEnabled = true
                    isHighlightPerTapEnabled = true

                    setDrawEntryLabels(false)
                    setEntryLabelColor(Color.Transparent.toArgb())
                    setEntryLabelTextSize(0f)

                    val entries = sortedRiskFactors.map { riskFactor ->
                        PieEntry(abs(riskFactor.percentage), riskFactor.name)
                    }

                    val dataSet = PieDataSet(entries, "").apply {
                        this.colors = chartColors

                        valueTextSize = 12f
                        valueTextColor = Color.White.toArgb()
                        valueTypeface = poppinsBoldTypeface

                        sliceSpace = 2f
                        selectionShift = 5f

                        valueFormatter = object : ValueFormatter() {
                            @SuppressLint("DefaultLocale")
                            override fun getFormattedValue(value: Float): String {
                                val index = entries.indexOfFirst { it.value == value }
                                val originalValue = sortedRiskFactors[index].percentage
                                return String.format("%.1f", originalValue)
                            }

                            override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
                                val fullName = pieEntry?.label ?: ""
                                return sortedRiskFactors.find { it.name == fullName }?.abbreviation ?: ""
                            }
                        }
                    }

                    this.data = PieData(dataSet)
                    this.data.setDrawValues(true)

                    animateY(animationDuration)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF9FAFB)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .wrapContentHeight()
            ) {
                sortedRiskFactors.forEach { riskFactor ->
                    LegendItem(
                        color = Color(chartColors[sortedRiskFactors.indexOf(riskFactor)]),
                        label = riskFactor.name,
                        value = riskFactor.percentage
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun LegendItem(
    color: Color,
    label: String,
    value: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Color box
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color)
        )

        // Label
        Text(
            text = label,
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = colorResource(id = R.color.primary)
        )

        // Value with sign
        Text(
            text = if (value >= 0) "+${String.format("%.1f", value)}%" else "${String.format("%.1f", value)}%",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = if (value >= 0) Color(0xFFC62828) else Color(0xFF2E7D32)
        )
    }
}