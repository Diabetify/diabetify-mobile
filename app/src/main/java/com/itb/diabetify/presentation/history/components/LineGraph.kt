package com.itb.diabetify.presentation.history.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.util.Calendar
import java.util.Random

@Composable
fun LineGraph(
    currentDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    improvementData: List<Float> = generateSampleData(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    setupChart(this, improvementData, currentDay)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            update = { chart ->
                setupChart(chart, improvementData, currentDay)
            }
        )
    }
}

private fun setupChart(
    chart: LineChart,
    data: List<Float>,
    currentDay: Int
) {
    // Create entries for the chart
    val entries = data.mapIndexed { index, value ->
        Entry((index + 1).toFloat(), value)
    }

    // Create dataset
    val dataSet = LineDataSet(entries, "Improvement").apply {
        color = Color(0xFF2196F3).toArgb()
        setCircleColor(Color(0xFF2196F3).toArgb())
        lineWidth = 2f
        circleRadius = 4f
        setDrawCircleHole(false)
        valueTextSize = 0f // Hide value labels on points
        setDrawFilled(true)
        fillColor = Color(0xFF2196F3).toArgb()
        fillAlpha = 30
    }

    // Highlight current day
    if (currentDay in 1..data.size) {
        val currentDayEntry = Entry(currentDay.toFloat(), data[currentDay - 1])
        val highlightDataSet = LineDataSet(listOf(currentDayEntry), "Current Day").apply {
            color = Color(0xFFFF5722).toArgb()
            setCircleColor(Color(0xFFFF5722).toArgb())
            circleRadius = 8f
            lineWidth = 0f // No line, just the point
            setDrawCircleHole(false)
            valueTextSize = 0f
        }

        val lineData = LineData(dataSet, highlightDataSet)
        chart.data = lineData
    } else {
        chart.data = LineData(dataSet)
    }

    // Configure chart appearance
    chart.apply {
        description.isEnabled = false
        legend.isEnabled = false
        setTouchEnabled(true)
        setDragEnabled(true)
        setScaleEnabled(false)
        setPinchZoom(false)
        setDrawGridBackground(false)

        // Configure X-axis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(true)
            granularity = 1f
            axisMinimum = 1f
            axisMaximum = 30f
            textSize = 12f
            textColor = Color.Gray.toArgb()
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "Day ${value.toInt()}"
                }
            }
        }

        // Configure Y-axis
        axisLeft.apply {
            setDrawGridLines(true)
            textSize = 12f
            textColor = Color.Gray.toArgb()
            axisMinimum = 0f
        }

        axisRight.isEnabled = false

        // Add selection listener for interaction
        setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                // Handle point selection if needed
            }

            override fun onNothingSelected() {
                // Handle deselection if needed
            }
        })

        // Refresh chart
        invalidate()
    }
}

private fun generateSampleData(): List<Float> {
    val random = Random()
    val data = mutableListOf<Float>()
    var currentValue = 10f

    repeat(30) {
        // Simulate gradual improvement with some random variation
        currentValue += random.nextFloat() * 2f + 0.5f
        data.add(currentValue)
    }

    return data
}