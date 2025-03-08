package com.itb.diabetify.presentation.common

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun Lottie(
    size: Int = 200,
    @RawRes rawResId: Int,
    iterations: Int = LottieConstants.IterateForever,
    isPlaying: Boolean = true
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawResId))

    Box(
        modifier = Modifier.size(size.dp),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            iterations = iterations,
            isPlaying = isPlaying
        )
    }
}