package com.itb.diabetify.presentation.survey

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.Lottie
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun SuccessScreen(
    navController: NavController
) {
    BackHandler {
        // Do nothing to disable back navigation
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Lottie(
                rawResId = R.raw.success,
                size = 300
            )

            Text(
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp),
                text = "Data Pribadi Berhasil Dikirim",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.primary)
            )

            Text(
                modifier = Modifier.padding(start = 50.dp, end = 50.dp, top = 0.dp, bottom = 25.dp),
                text = "Data pribadi Anda telah berhasil dikirim. Jangan lupa untuk mengubah data pribadi Anda jika ada perubahan.",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.gray)
            )
        }

        // Go to Home button
        PrimaryButton(
            text = "Beranda",
            onClick = {
                navController.navigate(Route.HomeScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp),
            enabled = true
        )
    }
}