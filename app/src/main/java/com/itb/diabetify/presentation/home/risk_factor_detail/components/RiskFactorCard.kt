package com.itb.diabetify.presentation.home.risk_factor_detail.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import kotlin.math.abs

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
                        .background(color, RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Name and Impact Percentage
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

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = color.copy(alpha = 0.15f)
                    )
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
                        color = color,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Text(
                text = "Deskripsi: " + riskFactor.description,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = colorResource(id = R.color.primary).copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = riskFactor.explanation,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = colorResource(id = R.color.primary),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Current and Ideal Values
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nilai Ideal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        color = colorResource(id = R.color.primary)
                    )

                    Text(
                        text = riskFactor.idealValue,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        lineHeight = 22.sp,
                        color = colorResource(id = R.color.primary)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Nilai Anda",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        color = colorResource(id = R.color.primary)
                    )

                    val isHighRisk = abs(riskFactor.impactPercentage) > 15f
                    Text(
                        text = riskFactor.currentValue,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        lineHeight = 22.sp,
                        color = if (isHighRisk) Color.Red else colorResource(id = R.color.primary)
                    )
                }
            }

            if (riskFactor.categories != null) {
                Text(
                    text = "Kategori: ",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "${riskFactor.categories}",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = colorResource(id = R.color.primary),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    }
}