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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.navgraph.Route
import com.itb.diabetify.presentation.survey.components.SurveyPage
import com.itb.diabetify.ui.theme.poppinsFontFamily
import kotlinx.coroutines.launch

@Composable
fun SurveyScreen(
    navController: NavController,
    viewModel: SurveyViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.showSnackbar) {
        if (state.showSnackbar) {
            scope.launch {
                snackbarHostState.showSnackbar(state.snackbarMessage)
                viewModel.clearSnackbar()
            }
        }
    }

    val navigationEvent = viewModel.navigationEvent.value
    LaunchedEffect(navigationEvent) {
        navigationEvent?.let {
            when (it) {
                "SUCCESS_SCREEN" -> {
                    navController.navigate(Route.SurveySuccessScreen.route)
                    viewModel.onNavigationHandled()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Survey question page
                if (viewModel.displayedQuestions.isNotEmpty()) {
                    SurveyPage(
                        question = viewModel.getCurrentQuestion(),
                        onAnswerSelected = { questionId, answer ->
                            viewModel.setAnswer(questionId, answer)
                        },
                        selectedAnswer = state.answers[viewModel.getCurrentQuestion().id]
                    )
                }
            }

            // Bottom section with progress
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Current question number
                Text(
                    text = "Pertanyaan ${state.currentPageIndex + 1} dari ${viewModel.displayedQuestions.size}",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(5.dp))

                // Progress indicator
                LinearProgressIndicator(
                    progress = { viewModel.getProgress() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    color = colorResource(R.color.primary),
                    trackColor = colorResource(R.color.background),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Previous and Next buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PrimaryButton(
                        text = "<",
                        onClick = { viewModel.previousPage() },
                        enabled = viewModel.canGoPrevious(),
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp),
                    )

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
}