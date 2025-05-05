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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.itb.diabetify.presentation.survey.components.SurveyPage
import com.itb.diabetify.ui.theme.poppinsFontFamily
import kotlinx.coroutines.launch

@Composable
fun SurveyScreen() {
    val questions = SurveyData.questions
    val answersState = remember { mutableStateMapOf<String, String>() }
    var currentPage by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Logic to handle conditional questions
    val displayedQuestions = remember(answersState) {
        questions.filter { question ->
            when (question.id) {
                "smoking_age", "smoking_amount" -> answersState["smoking_status"] == "active"
                else -> true
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
            // Middle section with survey questions
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // Survey question page
                if (displayedQuestions.isNotEmpty()) {
                    SurveyPage(
                        question = displayedQuestions[currentPage],
                        onAnswerSelected = { questionId, answer ->
                            answersState[questionId] = answer
                        },
                        selectedAnswer = answersState[displayedQuestions[currentPage].id]
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
                    text = "Pertanyaan ${currentPage + 1} dari ${displayedQuestions.size}",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(5.dp))

                // Progress indicator
                LinearProgressIndicator(
                    progress = { (currentPage + 1).toFloat() / displayedQuestions.size },
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
                        onClick = {
                            if (currentPage > 0) {
                                currentPage--
                            }
                        },
                        enabled = currentPage > 0,
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp),
                    )

                    PrimaryButton(
                        text = ">",
                        onClick = {
                            val currentQuestion = displayedQuestions[currentPage]
                            val answer = answersState[currentQuestion.id]

                            // Validate answer
                            if (answer.isNullOrBlank()) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Mohon jawab pertanyaan ini dahulu")
                                }
                                return@PrimaryButton
                            }

                            if (currentPage < displayedQuestions.size - 1) {
                                currentPage++
                            } else {
                                // Submit survey
                                scope.launch {
                                    snackbarHostState.showSnackbar("Survey berhasil dikirim!")
                                }
                            }
                        },
                        enabled = answersState[displayedQuestions[currentPage].id]?.isNotBlank() == true,
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp),
                    )
                }
            }
        }
    }
}