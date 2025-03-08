package com.itb.diabetify.presentation.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.itb.diabetify.R
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.ui.theme.poppinsFontFamily
import androidx.compose.ui.text.style.TextAlign

@Composable
fun GetStartedPage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.width(200.dp),
            painter = painterResource(id = R.drawable.ic_diabetify),
            contentDescription = "Onboarding Image"
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
            text = "Diabetify",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            color = colorResource(id = R.color.primary)
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Kenali Risiko,\nKendalikan Diabetes",
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.secondary).copy(alpha = 0.6f)
        )
    }
}
