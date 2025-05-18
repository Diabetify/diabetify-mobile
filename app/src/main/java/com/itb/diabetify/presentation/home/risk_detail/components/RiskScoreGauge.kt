package com.itb.diabetify.presentation.home.risk_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun RiskScoreGauge(
    score: Int,
    lowRiskColor: Color,
    mediumRiskColor: Color,
    highRiskColor: Color,
    veryHighRiskColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colorStops = arrayOf(
                            0.0f to lowRiskColor,
                            0.3f to mediumRiskColor,
                            0.5f to highRiskColor,
                            0.65f to veryHighRiskColor,
                            1.0f to veryHighRiskColor
                        )
                    )
                )
        )

        val normalizedScore = score.coerceIn(0, 100) / 100f

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (normalizedScore > 0) {
                Spacer(
                    modifier = Modifier
                        .weight(normalizedScore)
                        .height(1.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = colorResource(R.color.primary), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = score.toString(),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.white)
                )
            }

            if (normalizedScore < 1) {
                Spacer(
                    modifier = Modifier
                        .weight(1f - normalizedScore)
                        .height(1.dp)
                )
            }
        }
    }
}