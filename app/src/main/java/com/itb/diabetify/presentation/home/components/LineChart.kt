package com.itb.diabetify.presentation.home.components

import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

@Composable
fun PieChart(
    data: List<Pair<Float, String>>,
    colors: List<Color>? = null,
    centerText: String? = null,
    centerTextColor: Color = Color.Black,
    holeRadius: Float = 50f,
    showPercentValues: Boolean = false,
    showLegend: Boolean = true,
    animationDuration: Int = 1000,
    modifier: Modifier
) {
    // Default colors if none provided
    val chartColors = colors?.map { it.toArgb() } ?:
    listOf(
        Color(0xFF5D9CEC).toArgb(),
        Color(0xFFA0D468).toArgb(),
        Color(0xFFFFCE54).toArgb(),
        Color(0xFFED5565).toArgb(),
        Color(0xFFAC92EC).toArgb(),
        Color(0xFF48CFAD).toArgb()
    )

    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                // Basic chart setup
                description.isEnabled = false
                isDrawHoleEnabled = true
                setHoleColor(Color.Transparent.toArgb())
                this.holeRadius = holeRadius
                transparentCircleRadius = holeRadius + 5f

                // Center text setup if provided
                if (centerText != null) {
                    setDrawCenterText(true)
                    this.centerText = centerText
                    setCenterTextSize(16f)
                    setCenterTextColor(centerTextColor.toArgb())
                    setCenterTextTypeface(Typeface.DEFAULT_BOLD)
                } else {
                    setDrawCenterText(false)
                }

                // Legend setup
                legend.isEnabled = showLegend
                legend.textSize = 12f
                legend.formSize = 12f

                // Rotation and touch
                rotationAngle = 0f
                isRotationEnabled = true
                isHighlightPerTapEnabled = true

                // Data setup
                val entries = data.map { PieEntry(it.first, it.second) }
                val dataSet = PieDataSet(entries, "").apply {
                    // Use provided colors, recycling if needed
                    this.colors = chartColors

                    // Value text formatting
                    valueTextSize = if (showPercentValues) 12f else 0f
                    valueTextColor = Color.White.toArgb()
//                    valueFormatter = PercentFormatter(this@apply)
                    valueTypeface = Typeface.DEFAULT_BOLD

                    // Slice appearance
                    sliceSpace = 2f
                    selectionShift = 5f
                }

                this.data = PieData(dataSet)
                this.data.setValueFormatter(PercentFormatter())
                this.data.setDrawValues(showPercentValues)

                // Animation
                animateY(animationDuration)
            }
        },
        update = { chart ->
            // Update method for recomposition
            val entries = data.map { PieEntry(it.first, it.second) }
            val dataSet = PieDataSet(entries, "").apply {
                this.colors = chartColors
                valueTextSize = if (showPercentValues) 12f else 0f
                valueTextColor = Color.White.toArgb()
//                valueFormatter = PercentFormatter(this@apply)
                sliceSpace = 2f
                selectionShift = 5f
            }

            chart.data = PieData(dataSet)
            chart.data.setDrawValues(showPercentValues)

            if (centerText != null) {
                chart.centerText = centerText
                chart.setCenterTextColor(centerTextColor.toArgb())
            }

            chart.invalidate() // Refresh chart
        },
        modifier = modifier.fillMaxSize()
    )
}