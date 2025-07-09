package com.itb.diabetify.presentation.home.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.ui.theme.poppinsFontFamily

@SuppressLint("DefaultLocale")
@Composable
fun RiskIndicator(
    percentage: Double,
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    strokeWidth: Dp = 16.dp,
    animationDuration: Int = 1000,
    backgroundColor: Color = Color(0xFFE0E0E0),
    textColor: Color = Color.Black
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val currentPercentage by animateFloatAsState(
        targetValue = if (animationPlayed) percentage.toFloat() else 0f,
        animationSpec = tween(durationMillis = animationDuration),
        label = "percentage_animation"
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val sweepAngle = currentPercentage * 360f / 100f
            val strokeWidthPx = strokeWidth.toPx()

            drawArc(
                color = backgroundColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                size = Size(size.toPx() - strokeWidthPx, size.toPx() - strokeWidthPx),
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2)
            )

            val percentageColor = when {
                currentPercentage <= 35f -> Color(0xFF8BC34A) // Green
                currentPercentage <= 55f -> Color(0xFFFFC107) // Yellow
                currentPercentage <= 70f -> Color(0xFFFA821F) // Orange
                else -> Color(0xFFF44336) // Red
            }

            drawArc(
                color = percentageColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                size = Size(size.toPx() - strokeWidthPx, size.toPx() - strokeWidthPx),
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${String.format("%.1f", currentPercentage)}%",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily,
                color = textColor,
                textAlign = TextAlign.Center
            )

            val riskLevel = when {
                currentPercentage <= 35f -> "Rendah"
                currentPercentage <= 55f -> "Sedang"
                currentPercentage <= 70f -> "Tinggi"
                else -> "Sangat\nTinggi"
            }

            val riskColor = when {
                currentPercentage <= 35f -> Color(0xFF8BC34A) // Green
                currentPercentage <= 55f -> Color(0xFFFFC107) // Yellow
                currentPercentage <= 70f -> Color(0xFFFA821F) // Orange
                else -> Color(0xFFF44336) // Red
            }

            Text(
                text = riskLevel,
                fontSize = 14.sp,
                color = riskColor,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily,
                textAlign = TextAlign.Center,
            )
        }
    }
}