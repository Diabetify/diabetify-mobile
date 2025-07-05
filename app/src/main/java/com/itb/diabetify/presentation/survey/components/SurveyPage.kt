package com.itb.diabetify.presentation.survey.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.presentation.survey.SurveyQuestion
import com.itb.diabetify.presentation.survey.SurveyQuestionType
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun SurveyPage(
    question: SurveyQuestion,
    onAnswerSelected: (String, String) -> Unit,
    selectedAnswer: String?,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    var numericValue by remember { mutableStateOf(selectedAnswer ?: "") }
    LaunchedEffect(selectedAnswer) {
        if (question.questionType is SurveyQuestionType.Numeric) {
            numericValue = selectedAnswer ?: ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(35.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // Category
        Text(
            text = question.category,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = colorResource(R.color.primary),
            textAlign = TextAlign.Center,
        )

        // Question
        Text(
            text = question.questionText,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = colorResource(R.color.black),
        )

        // Additional Info
        if (question.additionalInfo.isNotEmpty()) {
            Text(
                text = question.additionalInfo,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.gray),
            )
        }

        // Answer options
        when (question.questionType) {
            is SurveyQuestionType.Selection -> {
                Column {
                    question.options.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { onAnswerSelected(question.id, option.id) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedAnswer == option.id,
                                onClick = { onAnswerSelected(question.id, option.id) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = if (errorMessage != null) colorResource(R.color.red) else colorResource(R.color.primary),
                                    unselectedColor = if (errorMessage != null) colorResource(R.color.red) else colorResource(R.color.black)
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = option.text,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                                color = colorResource(R.color.black),
                            )
                        }
                    }
                    
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = colorResource(R.color.red),
                            fontSize = 12.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }
            }
            is SurveyQuestionType.Numeric -> {
                Column {
                    OutlinedTextField(
                        value = numericValue,
                        onValueChange = {
                            numericValue = it
                            onAnswerSelected(question.id, it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Masukkan nilai",
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Normal
                            )
                        },
                        textStyle = TextStyle(
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = colorResource(R.color.black)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            if (question.numericUnit.isNotEmpty()) {
                                Text(
                                    text = question.numericUnit,
                                    modifier = Modifier.padding(end = 16.dp),
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        },
                        isError = errorMessage != null
                    )
                    
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = colorResource(R.color.red),
                            fontSize = 12.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}