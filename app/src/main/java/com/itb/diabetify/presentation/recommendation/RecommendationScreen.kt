package com.itb.diabetify.presentation.recommendation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.presentation.recommendation.components.FAQCard
import com.itb.diabetify.presentation.recommendation.components.GuideCard
import com.itb.diabetify.presentation.recommendation.components.TipsCard
import com.itb.diabetify.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.ceil


@Composable
fun RecommendationScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 30.dp),
                text = "Panduan",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(id = R.color.primary)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 35.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                // Guide
                Text(
                    modifier = Modifier.padding(top = 0.dp, bottom = 15.dp),
                    text = "Diabetes dan XAI",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.primary)
                )

                val chunkedCards = guideCards.chunked(2)
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    chunkedCards.forEach { rowCards ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            rowCards.forEach { cardData ->
                                GuideCard(
                                    guideCardData = cardData,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (rowCards.size == 1) {
                                androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // Tips
                Text(
                    modifier = Modifier.padding(top = 30.dp, bottom = 15.dp),
                    text = "Tips Kesehatan",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.primary)
                )

                val pageCount = ceil(tipsCard.size / 2f).toInt()
                val pagerState = rememberPagerState(pageCount = { pageCount })
                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(5000)
                        val nextPage = (pagerState.currentPage + 1) % pageCount
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(nextPage)
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) { page ->
                        val startIndex = page * 2

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (startIndex < tipsCard.size) {
                                Box(modifier = Modifier.weight(1f)) {
                                    TipsCard(tipsCardData = tipsCard[startIndex])
                                }
                            }

                            if (startIndex + 1 < tipsCard.size) {
                                Box(modifier = Modifier.weight(1f)) {
                                    TipsCard(tipsCardData = tipsCard[startIndex + 1])
                                }
                            } else if (startIndex < tipsCard.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (i in 0 until pageCount) {
                            val dotColor = if (i == pagerState.currentPage) Color.DarkGray else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(color = dotColor, shape = androidx.compose.foundation.shape.CircleShape)
                            )
                        }
                    }
                }

                // FAQ
                Text(
                    modifier = Modifier.padding(top = 15.dp, bottom = 15.dp),
                    text = "FAQ",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.primary)
                )

                faqCards.forEach{ faqCardData ->
                    FAQCard(faqCardData)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}