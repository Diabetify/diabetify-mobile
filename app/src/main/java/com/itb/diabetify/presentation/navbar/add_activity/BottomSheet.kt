package com.itb.diabetify.presentation.navbar.add_activity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun BottomSheet(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    questionType: String = "weight",
    currentNumericValue: String? = null,
    currentSelectionValue: String? = null,
    onSaveResponse: (String) -> Unit = { _ -> },
    viewModel: AddActivityViewModel
) {
    val surveyQuestions = questions
    val currentQuestion = surveyQuestions.find { it.id == questionType } ?: surveyQuestions.first()

    if (isVisible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable { onDismissRequest() },
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(300)) +
                            slideInVertically(
                                animationSpec = tween(300, easing = FastOutSlowInEasing),
                                initialOffsetY = { it / 2 }
                            )
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = Color.Transparent
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(id = R.color.white)
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            ),
                            onClick = {}
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            ) {
                                // Header
                                Text(
                                    text = currentQuestion.category,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = colorResource(id = R.color.primary)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Question
                                Text(
                                    text = currentQuestion.questionText,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                // Content based on question type
                                when (currentQuestion.questionType) {
                                    DataInputQuestionType.Numeric -> NumericInput(
                                        question = currentQuestion,
                                        currentValue = currentNumericValue,
                                        onSave = {
                                            onSaveResponse(currentQuestion.id)
                                            onDismissRequest()
                                        },
                                        viewModel = viewModel
                                    )
                                    DataInputQuestionType.Selection -> SelectionInput(
                                        question = currentQuestion,
                                        currentValue = currentSelectionValue,
                                        onSave = {
                                            onSaveResponse(currentQuestion.id)
                                            onDismissRequest()
                                        },
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumericInput(
    question: DataInputQuestion,
    currentValue: String? = null,
    onSave: (String) -> Unit,
    viewModel: AddActivityViewModel
) {
    var inputValue by remember { mutableStateOf(currentValue ?: "") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(18.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = inputValue,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.all { char -> char.isDigit() || char == '.' }) {
                        inputValue = newValue
                        when (question.id) {
                            "cigarette" -> viewModel.setSmokeValue(newValue)
                            "activity" -> viewModel.setWorkoutValue(newValue)
                            "weight" -> viewModel.setWeightValue(newValue)
                            "height" -> viewModel.setHeightValue(newValue)
                        }
                    }
                },
                textStyle = TextStyle(
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = colorResource(R.color.black)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = colorResource(id = R.color.primary),
                    unfocusedIndicatorColor = colorResource(id = R.color.secondary)
                ),
                trailingIcon = {
                    question.numericUnit?.let {
                        Text(
                            text = question.numericUnit,
                            modifier = Modifier.padding(end = 10.dp),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit button
        PrimaryButton(
            text = "Simpan",
            onClick = { onSave(inputValue) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp),
        )
    }
}

@Composable
fun SelectionInput(
    question: DataInputQuestion,
    currentValue: String? = null,
    onSave: (String) -> Unit,
    viewModel: AddActivityViewModel
) {
    // Convert the currentValue string to match our option IDs
    val initialValue = when (currentValue?.lowercase()) {
        "true" -> "yes"
        "false" -> "no"
        else -> currentValue
    }
    
    var selectedOption by remember { mutableStateOf(initialValue) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(Modifier.height(8.dp))

        // Radio options
        question.options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { 
                        selectedOption = option.id
                        when (question.id) {
                            "birth" -> viewModel.setBirthValue((option.id == "yes").toString())
                            "hypertension" -> viewModel.setHypertensionValue((option.id == "yes").toString())
                        }
                    }
            ) {
                RadioButton(
                    selected = selectedOption == option.id,
                    onClick = { 
                        selectedOption = option.id
                        when (question.id) {
                            "birth" -> viewModel.setBirthValue((option.id == "yes").toString())
                            "hypertension" -> viewModel.setHypertensionValue((option.id == "yes").toString())
                        }
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorResource(id = R.color.primary)
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = option.label,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit button
        PrimaryButton(
            text = "Simpan",
            onClick = { selectedOption?.let { onSave(it) } },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp),
        )
    }
}