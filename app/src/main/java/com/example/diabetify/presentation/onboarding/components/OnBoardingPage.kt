package com.example.diabetify.presentation.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diabetify.R
import com.example.diabetify.ui.theme.poppinsFontFamily

@Composable
fun OnBoardingPage(
    modifier: Modifier = Modifier,
    imageResId: Int,
    title: String,
    description: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Onboarding Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            alignment = Alignment.TopCenter
        )
        Text(
            modifier = Modifier.padding(
                start = 30.dp,
                end = 30.dp,
                top = 40.dp,
                bottom = 5.dp
            ),
            text = title,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 30.sp,
            color = colorResource(id = R.color.primary)
        )
        Text(
            modifier = Modifier.padding(horizontal = 30.dp),
            text = description,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            color = colorResource(id = R.color.secondary).copy(alpha = 0.6f)
        )
    }
}