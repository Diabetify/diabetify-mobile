package com.itb.diabetify.presentation.no_internet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun NoInternetScreen(
    navController: NavController,
    onRetryClicked: () -> Unit
) {
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
            Image(
                painter = painterResource(id = R.drawable.ic_no_wifi),
                contentDescription = "No Internet",
                modifier = Modifier.size(120.dp)
            )

            Text(
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 10.dp, bottom = 5.dp),
                text = "Tidak Ada Koneksi Internet",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.primary)
            )

            Text(
                modifier = Modifier.padding(start = 50.dp, end = 50.dp, top = 0.dp, bottom = 25.dp),
                text = "Silakan periksa koneksi internet Anda dan coba lagi. Diabetify memerlukan koneksi internet aktif untuk berfungsi dengan baik.",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.gray)
            )
        }

        // Try Again Button
        PrimaryButton(
            text = "Coba Lagi",
            onClick = onRetryClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-30).dp),
            enabled = true
        )
    }

    BackHandler {
        // Do Nothing
    }
} 