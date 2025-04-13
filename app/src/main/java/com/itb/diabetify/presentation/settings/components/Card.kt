package com.itb.diabetify.presentation.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.presentation.settings.CardData
import com.itb.diabetify.presentation.settings.ContentData
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun Card (
    cardData: CardData,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Gray.copy(alpha = 0.5f)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.white))
    ) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 15.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 10.dp),
                text = cardData.title,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.primary),
            )

            cardData.contents.forEachIndexed { index, content ->
                ContentItem(
                    contentData = content,
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun ContentItem(
    contentData: ContentData,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = contentData.icon),
                contentDescription = contentData.name,
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 10.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )

            Text(
                text = contentData.name,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = colorResource(id = R.color.gray),
                textAlign = TextAlign.Start
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Arrow Right",
            tint = colorResource(id = R.color.gray)
        )
    }
}