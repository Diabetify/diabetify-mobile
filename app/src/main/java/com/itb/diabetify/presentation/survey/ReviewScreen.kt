package com.itb.diabetify.presentation.survey

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.CustomizableButton
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun ReviewScreen(
    answeredQuestions: List<Pair<SurveyQuestion, String>>,
    onConfirm: () -> Unit,
    onBack: () -> Unit,
    viewModel: SurveyViewModel
) {
    // States
    val isLoading = viewModel.profileState.value.isLoading || viewModel.activityState.value.isLoading || viewModel.predictionState.value.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Description Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.primary).copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = colorResource(id = R.color.primary),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Pastikan jawaban yang Anda berikan sudah benar sebelum mengirimkan survei ini.",
                    fontFamily = poppinsFontFamily,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.primary),
                    lineHeight = 20.sp
                )
            }
        }

        // Answers list
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(answeredQuestions) { (question, answer) ->
                ReviewItem(
                    question = question,
                    formattedAnswer = viewModel.getFormattedAnswer(question, answer)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Back button
            CustomizableButton(
                text = "Kembali",
                onClick = { onBack() },
                enabled = !isLoading,
                backgroundColor = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            )

            // Confirm button
            PrimaryButton(
                text = "Kirim",
                onClick = onConfirm,
                enabled = !isLoading,
                isLoading = isLoading,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            )
        }
    }
}

@Composable
fun ReviewItem(
    question: SurveyQuestion,
    formattedAnswer: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Category
            Text(
                text = question.category,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = colorResource(R.color.primary)
            )
            
            // Question
            Text(
                text = question.questionText,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color.Black
            )
            
            // Answer
            Text(
                text = "Jawaban: $formattedAnswer",
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = colorResource(R.color.primary)
            )
        }
    }
} 