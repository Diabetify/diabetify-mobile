package com.itb.diabetify.presentation.home.components

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.itb.diabetify.R
import kotlin.math.abs

@Composable
fun PieChart(
    dataPercentages: List<Float>,
    centerText: String? = null,
    centerTextColor: Color = Color.Black,
    holeRadius: Float = 30f,
    showLegend: Boolean = true,
    animationDuration: Int = 1000,
    modifier: Modifier
) {
    val context = LocalContext.current

    val poppinsTypeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            ResourcesCompat.getFont(context, R.font.poppins_regular)
        } catch (e: Exception) {
            Typeface.DEFAULT
        }
    } else {
        Typeface.DEFAULT
    }

    val poppinsBoldTypeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            ResourcesCompat.getFont(context, R.font.poppins_bold)
        } catch (e: Exception) {
            Typeface.DEFAULT_BOLD
        }
    } else {
        Typeface.DEFAULT_BOLD
    }

    val riskFactors = listOf(
        "Indeks Massa Tubuh" to "IMT",
        "Hipertensi" to "HTN",
        "Riwayat Kelahiran" to "RK",
        "Aktivitas Fisik" to "AF",
        "Usia" to "U",
        "Indeks Merokok" to "IM"
    )

    val data = dataPercentages.mapIndexed { index, percentage ->
        val pair = riskFactors.getOrNull(index)
        percentage to (pair?.first ?: "Unknown")
    }

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
                    setCenterTextColor(centerTextColor.toArgb())
                    setCenterTextTypeface(poppinsBoldTypeface)
                } else {
                    setDrawCenterText(false)
                }

                legend.isEnabled = showLegend
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                legend.textSize = 12f
                legend.formSize = 12f
                legend.isWordWrapEnabled = true
                legend.setDrawInside(false)
                legend.typeface = poppinsTypeface

                rotationAngle = 0f
                isRotationEnabled = true
                isHighlightPerTapEnabled = true

                setDrawEntryLabels(false)
                setEntryLabelColor(Color.Transparent.toArgb())
                setEntryLabelTextSize(0f)

                val entries = data.map { (value, label) ->
                    PieEntry(abs(value), label)
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
                            val originalValue = dataPercentages[entries.indexOfFirst { it.value == value }]
                            return if (originalValue >= 0) {
                                String.format("%.1f", originalValue)
                            } else {
                                String.format("%.1f", originalValue)
                            }
                        }

                        override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
                            val fullName = pieEntry?.label
                            return riskFactors.find { it.first == fullName }?.second ?: ""
                        }
                    }
                }

                this.data = PieData(dataSet)
                this.data.setDrawValues(true)

                legend.setCustom(
                    riskFactors.mapIndexed { index, (fullName, abbreviation) ->
                        legend.entries?.get(index)?.apply {
                            label = "$abbreviation: $fullName"
                        }
                    }.toTypedArray()
                )

                animateY(animationDuration)
            }
        },
        update = { chart ->
            chart.setDrawEntryLabels(false)
            chart.setEntryLabelColor(Color.Transparent.toArgb())
            chart.setEntryLabelTextSize(0f)

            val entries = data.map { (value, label) ->
                PieEntry(abs(value), label)
            }
            val dataSet = PieDataSet(entries, "").apply {
                this.colors = chartColors
                valueTextSize = 15f
                valueTextColor = Color.White.toArgb()
                valueTypeface = poppinsBoldTypeface
                sliceSpace = 2f
                selectionShift = 5f

                valueFormatter = object : ValueFormatter() {
                    @SuppressLint("DefaultLocale")
                    override fun getFormattedValue(value: Float): String {
                        val originalValue = dataPercentages[entries.indexOfFirst { it.value == value }]
                        return if (originalValue >= 0) {
                            String.format("%.1f", originalValue)
                        } else {
                            String.format("%.1f", originalValue)
                        }
                    }

                    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
                        val fullName = pieEntry?.label
                        return riskFactors.find { it.first == fullName }?.second ?: ""
                    }
                }
            }

            chart.data = PieData(dataSet)
            chart.data.setDrawValues(true)

            if (centerText != null) {
                chart.centerText = centerText
                chart.setCenterTextColor(centerTextColor.toArgb())
                chart.setCenterTextTypeface(poppinsBoldTypeface)
            }

            chart.legend.typeface = poppinsTypeface

            chart.legend.setCustom(
                riskFactors.mapIndexed { index, (fullName, abbreviation) ->
                    chart.legend.entries?.get(index)?.apply {
                        label = "$abbreviation: $fullName"
                    }
                }.toTypedArray()
            )

            chart.invalidate()
        },
        modifier = modifier.fillMaxSize()
    )
}