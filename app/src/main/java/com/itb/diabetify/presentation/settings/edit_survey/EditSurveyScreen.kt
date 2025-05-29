package com.itb.diabetify.presentation.settings.edit_survey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.settings.SettingsViewModel
import com.itb.diabetify.presentation.settings.edit_survey.components.SurveyQuestionCard
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun EditSurveyScreen(
    navController: NavController,
    viewModel: SettingsViewModel
) {
    var answers by remember { mutableStateOf(mapOf<String, String>()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = colorResource(id = R.color.primary)
                    )
                }

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Edit Survey",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.primary)
                )
            }

            // Scrollable content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 1.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                questions.forEach { question ->
                    SurveyQuestionCard(
                        question = question,
                        answer = answers[question.id] ?: "",
                        onAnswerChanged = { newAnswer ->
                            answers = answers + (question.id to newAnswer)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(70.dp))
            }
        }

        // Save Button
        PrimaryButton(
            text = "Simpan Perubahan",
            onClick = {
                val isValid = viewModel.validateEditProfileFields()
                if (isValid) {
                    viewModel.editProfile()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
//            enabled =
        )
    }
}