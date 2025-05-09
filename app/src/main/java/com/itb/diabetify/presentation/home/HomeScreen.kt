package com.itb.diabetify.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.home.components.PieChart

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
) {
    Box(modifier = Modifier.fillMaxSize().background(colorResource(R.color.white)), contentAlignment = Alignment.Center) {
        PieChart(
            data = listOf(
                35f to "BMI",
                25f to "Blood Glucose",
                15f to "Family History",
                10f to "Physical Activity",
                10f to "Age",
                5f to "Blood Pressure"
            ),
            centerText = "Risk\nFactors",
            centerTextColor = Color.DarkGray,
            showPercentValues = true,
            modifier = Modifier.height(300.dp)
        )
    }
}