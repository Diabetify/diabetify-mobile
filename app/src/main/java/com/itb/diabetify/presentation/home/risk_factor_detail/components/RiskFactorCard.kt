package com.itb.diabetify.presentation.home.risk_factor_detail.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.presentation.home.HomeViewModel
import com.itb.diabetify.presentation.home.HomeViewModel.RiskFactorDetails
import com.itb.diabetify.presentation.home.components.calculateRiskFactorColor
import com.itb.diabetify.ui.theme.poppinsFontFamily

@SuppressLint("DefaultLocale")
@Composable
fun RiskFactorCard(
    riskFactor: RiskFactorDetails,
    riskFactors: List<HomeViewModel.RiskFactor>
) {
    val color = calculateRiskFactorColor(
        percentage = riskFactor.impactPercentage,
        riskFactors = riskFactors
    )

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
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(color, RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${riskFactor.name}: ${riskFactor.fullName}",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Impact Badge
                Box(
                    modifier = Modifier
                        .background(
                            color = color.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    val formattedValue = if (riskFactor.impactPercentage >= 0)
                        "+${String.format("%.1f", riskFactor.impactPercentage)}%"
                    else
                        "${String.format("%.1f", riskFactor.impactPercentage)}%"

                    Text(
                        text = formattedValue,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = color
                    )
                }
            }

            // Explanation Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = colorResource(id = R.color.primary).copy(alpha = 0.03f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                val explanationParts = riskFactor.explanation.split(Regex("(?<=[a-zA-Z])\\. "))
                val userImpact = explanationParts.getOrNull(0)?.trim().orEmpty()
                val globalImpact = explanationParts.drop(1).joinToString(". ").trim()

                if (userImpact.isNotEmpty()) {
                    Text(
                        text = userImpact + if (!userImpact.endsWith(".")) "." else "",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = colorResource(id = R.color.primary),
                        modifier = Modifier.padding(bottom = if (globalImpact.isNotEmpty()) 8.dp else 0.dp)
                    )
                }

                if (globalImpact.isNotEmpty()) {
                    Text(
                        text = globalImpact,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = colorResource(id = R.color.primary).copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = colorResource(id = R.color.primary).copy(alpha = 0.1f),
                thickness = 1.dp
            )

            // Description
            if (riskFactor.description != null) {
                Text(
                    text = riskFactor.description,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = colorResource(id = R.color.primary).copy(alpha = 0.7f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Ideal Value
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = colorResource(id = R.color.primary).copy(alpha = 0.03f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Nilai Ideal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )

                    Text(
                        text = riskFactor.idealValue,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 18.sp,
                        color = colorResource(id = R.color.primary)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Current Value
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = colorResource(id = R.color.primary).copy(alpha = 0.03f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Nilai Anda",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )

                    Text(
                        text = riskFactor.currentValue,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 18.sp,
                        color = colorResource(id = R.color.primary)
                    )
                }
            }
        }
    }
}