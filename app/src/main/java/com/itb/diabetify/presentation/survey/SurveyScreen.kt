package com.itb.diabetify.presentation.survey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.activity.compose.BackHandler
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.ErrorNotification
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.presentation.survey.components.SurveyPage
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun SurveyScreen(
    navController: NavController,
    viewModel: SurveyViewModel
) {
    BackHandler {
        // Do nothing
    }

    // States
    val surveyState = viewModel.surveyState.value
    val errorMessage = viewModel.errorMessage.value

    // Navigation Event
    val navigationEvent = viewModel.navigationEvent.value
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let {
            when (it) {
                "SUCCESS_SCREEN" -> {
                    navController.navigate(Route.SurveySuccessScreen.route) {
                        popUpTo(Route.SurveyScreen.route) {
                            inclusive = true
                        }
                    }
                    viewModel.onNavigationHandled()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (surveyState.showReviewScreen) {
                    // Review Screen
                    ReviewScreen(
                        answeredQuestions = viewModel.getAnsweredQuestions(),
                        onConfirm = { 
                            val isValid = viewModel.validateSurveyFields()
                            if (isValid) {
                                viewModel.submitSurvey()
                            }
                        },
                        onBack = { viewModel.backToSurvey() },
                        viewModel = viewModel
                    )
                } else {
                    // Survey Page
                    if (viewModel.displayedSurveyQuestions.isNotEmpty()) {
                        val currentQuestion = viewModel.getCurrentQuestion()
                        SurveyPage(
                            question = currentQuestion,
                            onAnswerSelected = { questionId, answer ->
                                viewModel.setAnswer(questionId, answer)
                            },
                            selectedAnswer = surveyState.fieldStates[currentQuestion.id]?.text,
                            errorMessage = surveyState.fieldStates[currentQuestion.id]?.error
                        )
                    }
                }
            }

            if (!surveyState.showReviewScreen) {
                // Bottom Navigation
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Pertanyaan ${surveyState.currentPageIndex + 1} dari ${viewModel.displayedSurveyQuestions.size}",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    LinearProgressIndicator(
                        progress = { viewModel.getProgress() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        color = colorResource(R.color.primary),
                        trackColor = colorResource(R.color.background),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous Button
                        PrimaryButton(
                            text = "<",
                            onClick = { viewModel.previousPage() },
                            enabled = viewModel.canGoPrevious(),
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp),
                        )

                        // Next Button
                        PrimaryButton(
                            text = ">",
                            onClick = { viewModel.nextPage() },
                            enabled = viewModel.canGoNext(),
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp),
                        )
                    }
                }
            }
        }

        // Error notification
        ErrorNotification(
            showError = errorMessage != null,
            errorMessage = errorMessage,
            onDismiss = { viewModel.onErrorShown() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1000f)
        )
    }
}