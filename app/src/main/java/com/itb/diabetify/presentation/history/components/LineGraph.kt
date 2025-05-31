package com.itb.diabetify.presentation.history.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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

data class PredictionScoreEntry(
    val day: Int,
    val score: Float
)

@Composable
fun LineGraph(
    currentDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    predictionScores: List<PredictionScoreEntry> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    setupChart(this, predictionScores, currentDay)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            update = { chart ->
                setupChart(chart, predictionScores, currentDay)
            }
        )
    }
}

private fun setupChart(
    chart: LineChart,
    data: List<PredictionScoreEntry>,
    currentDay: Int
) {
    val entries = data.map { entry ->
        Entry(entry.day.toFloat(), entry.score)
    }

    val dataSet = LineDataSet(entries, "Risk Score").apply {
        color = Color(0xFF2196F3).toArgb()
        setCircleColor(Color(0xFF2196F3).toArgb())
        lineWidth = 2f
        circleRadius = 4f
        setDrawCircleHole(false)
        valueTextSize = 0f
        setDrawFilled(true)
        fillColor = Color(0xFF2196F3).toArgb()
        fillAlpha = 30
    }

    val currentDayEntry = data.find { it.day == currentDay }?.let { entry ->
        Entry(entry.day.toFloat(), entry.score)
    }

    if (currentDayEntry != null) {
        val highlightDataSet = LineDataSet(listOf(currentDayEntry), "Current Day").apply {
            color = Color(0xFFFF5722).toArgb()
            setCircleColor(Color(0xFFFF5722).toArgb())
            circleRadius = 8f
            lineWidth = 0f
            setDrawCircleHole(false)
            valueTextSize = 0f
        }

        val lineData = LineData(dataSet, highlightDataSet)
        chart.data = lineData
    } else {
        chart.data = LineData(dataSet)
    }

    chart.apply {
        description.isEnabled = false
        legend.isEnabled = false
        setTouchEnabled(true)
        setDragEnabled(true)
        setScaleEnabled(false)
        setPinchZoom(false)
        setDrawGridBackground(false)

        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(true)
            granularity = 1f
            axisMinimum = if (data.isNotEmpty()) data.minOf { it.day }.toFloat() else 1f
            axisMaximum = if (data.isNotEmpty()) data.maxOf { it.day }.toFloat() else 30f
            textSize = 12f
            textColor = Color.Gray.toArgb()
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "Day ${value.toInt()}"
                }
            }
        }

        axisLeft.apply {
            setDrawGridLines(true)
            textSize = 12f
            textColor = Color.Gray.toArgb()
            axisMinimum = 0f
            axisMaximum = 100f
        }

        axisRight.isEnabled = false

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