package com.itb.diabetify.presentation.onboarding

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.onboarding.components.GetStartedPage
import com.itb.diabetify.presentation.onboarding.components.OnBoardingPage
import com.itb.diabetify.presentation.onboarding.components.PagerIndicator
import com.itb.diabetify.ui.theme.poppinsFontFamily
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@Composable
fun OnBoardingScreen(
    event: (OnBoardingEvent) -> Unit
) {
    val (showGetStarted, setShowGetStarted) = remember { mutableStateOf(true) }

    val pagerState = rememberPagerState(initialPage = 0) {
        pages.size
    }

    val buttonText = derivedStateOf {
        if (showGetStarted) {
            "Mulai"
        } else {
            ">"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (showGetStarted) colorResource(id = R.color.background)
                else colorResource(id = R.color.white)
            )
    ) {
        if (showGetStarted) {
            GetStartedPage(modifier = Modifier.weight(1f))
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { index ->
                OnBoardingPage(
                    imageResId = pages[index].imageResId,
                    title = pages[index].title,
                    description = pages[index].description,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .navigationBarsPadding()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!showGetStarted) {
                PagerIndicator(
                    modifier = Modifier.width(100.dp),
                    pagesSize = pages.size,
                    selectedPage = pagerState.currentPage
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (showGetStarted) Arrangement.Center else Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val scope = rememberCoroutineScope()

                if (showGetStarted) {
                    PrimaryButton(
                        text = buttonText.value,
                        onClick = {
                            scope.launch {
                                setShowGetStarted(false)
                            }
                        },
                    )
                } else {
                    Button(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage == pages.size - 1) {
                                    event(OnBoardingEvent.SaveAppEntry)
                                } else {
                                    pagerState.animateScrollToPage(
                                        page = pagerState.currentPage + 1
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                                .height(56.dp)
                                .width(56.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.primary)),
                        shape = androidx.compose.foundation.shape.CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = buttonText.value,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 25.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}