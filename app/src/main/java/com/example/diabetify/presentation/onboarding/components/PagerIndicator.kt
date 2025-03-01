package com.example.diabetify.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.diabetify.R

@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    pagesSize: Int,
    selectedPage: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagesSize) { page ->
            Box(
                modifier = Modifier
                    .size(if (page == selectedPage) 12.dp else 8.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (page == selectedPage) {
                            colorResource(id = R.color.primary)
                        } else {
                            colorResource(id = R.color.secondary).copy(alpha = 0.3f)
                        }
                    )
            )
        }
    }
}