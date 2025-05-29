package com.itb.diabetify.presentation.settings.edit_survey.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.presentation.settings.edit_survey.SurveyQuestion
import com.itb.diabetify.presentation.settings.edit_survey.SurveyQuestionType
import com.itb.diabetify.ui.theme.poppinsFontFamily


@Composable
fun SurveyQuestionCard(
    question: SurveyQuestion,
    answer: String,
    onAnswerChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.white)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Category
            Text(
                text = question.category,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorResource(R.color.primary)
            )

            // Question
            Text(
                text = question.questionText,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                color = colorResource(R.color.primary)
            )

            // Additional Info if present
            if (question.additionalInfo.isNotEmpty()) {
                Text(
                    text = question.additionalInfo,
                    fontFamily = poppinsFontFamily,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    color = colorResource(R.color.gray),
                )
            }

            // Answer input
            when (question.questionType) {
                is SurveyQuestionType.Numeric -> {
//                    var inputValue by remember { mutableStateOf(currentValue ?: "") }
                    var inputValue by remember { mutableStateOf("5") }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = {
                                if (it.isEmpty() || it.all { char -> char.isDigit() || char == '.' }) {
                                    inputValue = it
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
                }
                is SurveyQuestionType.Selection -> {
//                    var selectedOption by remember { mutableStateOf(currentValue) }
                    var selectedOption by remember { mutableStateOf(answer) }

                    Column() {
                        question.options.forEach { option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedOption = option.id }
                            ) {
                                RadioButton(
                                    selected = selectedOption == option.id,
                                    onClick = { selectedOption = option.id },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = colorResource(id = R.color.primary)
                                    )
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = option.text,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}