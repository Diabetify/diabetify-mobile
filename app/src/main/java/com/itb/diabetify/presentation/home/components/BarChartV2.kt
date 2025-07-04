package com.itb.diabetify.presentation.home.components

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.itb.diabetify.R
import com.itb.diabetify.ui.theme.poppinsFontFamily
import kotlin.math.abs

@Composable
fun BarChartV2(
    entries: List<BarChartEntry>,
    modifier: Modifier = Modifier,
    animationDuration: Int = 1000
) {
    val sortedDescendingEntries = remember(entries) {
        entries.sortedByDescending { abs(it.value) }
    }
    val sortedAscendingEntries = remember(entries) {
        entries.sortedBy { abs(it.value) }
    }
    
    // Calculate max value for axis rounded to next multiple of 10
    // For V2, we always start from 0 and only go to the right
    val maxValue = remember(entries) {
        val max = entries.maxOf { abs(it.value) }
        (((max + 10f) / 10f).toInt() * 10f) // Round up to next multiple of 10
    }

    Column(modifier = modifier.wrapContentHeight()) {
        AndroidView(
            factory = { context ->
                HorizontalBarChart(context).apply {
                    description.isEnabled = false
                    legend.isEnabled = false
                    setDrawGridBackground(false)
                    setDrawBarShadow(false)
                    setDrawValueAboveBar(false)

                    // Disable zoom and scaling
                    setPinchZoom(false)
                    setScaleEnabled(false)

                    // Disable X axis completely
                    xAxis.isEnabled = false

                    // Configure right axis
                    axisRight.isEnabled = false

                    // Configure left axis - start from 0 (leftmost) and go to maxValue
                    axisLeft.apply {
                        setDrawGridLines(true)
                        setDrawAxisLine(true)
                        axisMinimum = 0f // Always start from 0
                        axisMaximum = maxValue
                        // Set label count to show multiples of 10
                        setLabelCount((maxValue / 10f).toInt() + 1, true)
                    }

                    // Configure the horizontal axis (bottom)
                    extraLeftOffset = 35f

                    // Make chart fill the width
                    setFitBars(true)
                }
            },
            update = { chart ->
                val dataSets = mutableListOf<IBarDataSet>()

                // Create entries for labels at the start of each bar
                val labelEntries = mutableListOf<BarEntry>()
                val labelColors = mutableListOf<Int>()

                // Create entries for actual bars (all extend to the right from 0)
                val barEntries = mutableListOf<BarEntry>()
                val barColors = mutableListOf<Int>()

                sortedAscendingEntries.forEachIndexed { index, entry ->
                    // All bars start from 0 and extend to the right using absolute value
                    barEntries.add(BarEntry(index.toFloat(), abs(entry.value.toFloat())))
                    barColors.add(
                        if (entry.isNegative) {
                            Color.rgb(46, 125, 50) // Green for negative values
                        } else {
                            Color.rgb(198, 40, 40) // Red for positive values
                        }
                    )

                    // Add invisible entries at zero for label positioning
                    labelEntries.add(BarEntry(index.toFloat(), 0f))
                    labelColors.add(Color.TRANSPARENT)
                }

                // Main bar dataset
                val mainDataSet = BarDataSet(barEntries, "Main").apply {
                    colors = barColors
                    valueTextColor = Color.TRANSPARENT
                    valueTextSize = 12f
                    setDrawValues(false) // Don't draw values on main bars
                }
                dataSets.add(mainDataSet)

                // Label dataset (invisible bars at zero line for abbreviations)
                val labelDataSet = BarDataSet(labelEntries, "Labels").apply {
                    colors = labelColors
                    valueTextColor = Color.BLACK
                    valueTextSize = 12f
                    setDrawValues(true)
                    valueFormatter = object : ValueFormatter() {
                        override fun getBarLabel(barEntry: BarEntry?): String {
                            return if (barEntry != null) {
                                val index = barEntry.x.toInt()
                                val originalEntry = sortedAscendingEntries.getOrNull(index)
                                originalEntry?.abbreviation ?: ""
                            } else ""
                        }
                    }
                }
                dataSets.add(labelDataSet)

                val barData = BarData(dataSets).apply {
                    barWidth = 0.5f
                }

                chart.data = barData
                chart.invalidate()
                chart.animateY(animationDuration)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Legend card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.ui.graphics.Color(0xFFF9FAFB)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .wrapContentHeight()
            ) {
                sortedDescendingEntries.forEach { entry ->
                    LegendItemsV2(
                        color = if (entry.isNegative) {
                            androidx.compose.ui.graphics.Color(0xFF2E7D32)
                        } else {
                            androidx.compose.ui.graphics.Color(0xFFC62828)
                        },
                        label = entry.label,
                        value = entry.value
                    )
                }
            }
        }
    }
}

@Composable
fun LegendItemsV2(
    color: androidx.compose.ui.graphics.Color,
    label: String,
    value: Double
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
            text = when {
                abs(value) < 0.000001 -> "${String.format("%.1f", value)}%" // Handle effectively zero values
                value > 0 -> "+${String.format("%.1f", value)}%"
                else -> "${String.format("%.1f", value)}%"
            },
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = when {
                abs(value) < 0.000001 -> androidx.compose.ui.graphics.Color(0xFF2E7D32) // Handle effectively zero values
                value > 0 -> androidx.compose.ui.graphics.Color(0xFFC62828)
                else -> androidx.compose.ui.graphics.Color(0xFF2E7D32)
            }
        )
    }
} 