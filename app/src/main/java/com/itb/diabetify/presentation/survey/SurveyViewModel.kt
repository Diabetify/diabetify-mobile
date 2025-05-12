package com.itb.diabetify.presentation.survey

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor() : ViewModel() {
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent


    private val _state = mutableStateOf(SurveyState())
    val state: State<SurveyState> = _state

    private val surveyQuestions = questions

    val displayedSurveyQuestions: List<SurveyQuestion>
        get() = surveyQuestions.filter { question ->
            when (question.id) {
                "smoking_age", "smoking_amount" -> _state.value.answers["smoking_status"] == "active"
                else -> true
            }
        }

    fun nextPage() {
        val currentQuestion = displayedSurveyQuestions[_state.value.currentPageIndex]
        val answer = _state.value.answers[currentQuestion.id]

        if (answer.isNullOrBlank()) {
            showSnackbar("Mohon jawab pertanyaan ini dahulu")
            return
        }

        if (_state.value.currentPageIndex < displayedSurveyQuestions.size - 1) {
            _state.value = _state.value.copy(
                currentPageIndex = _state.value.currentPageIndex + 1
            )
        } else {
            submitSurvey()
        }
    }

    fun previousPage() {
        if (_state.value.currentPageIndex > 0) {
            _state.value = _state.value.copy(
                currentPageIndex = _state.value.currentPageIndex - 1
            )
        }
    }

    private fun submitSurvey() {
        Log.d("SurveyViewModel", "Submitting survey with answers: ${_state.value.answers}")
        _navigationEvent.value = "SUCCESS_SCREEN"
    }

    fun setAnswer(questionId: String, answer: String) {
        val newAnswers = _state.value.answers.toMutableMap()
        newAnswers[questionId] = answer

        _state.value = _state.value.copy(
            answers = newAnswers
        )
    }

    private fun showSnackbar(message: String) {
        _state.value = _state.value.copy(
            showSnackbar = true,
            snackbarMessage = message
        )
    }

    fun clearSnackbar() {
        _state.value = _state.value.copy(
            showSnackbar = false,
            snackbarMessage = ""
        )
    }

    fun getCurrentQuestion(): SurveyQuestion {
        return displayedSurveyQuestions[_state.value.currentPageIndex]
    }

    fun getProgress(): Float {
        return (_state.value.currentPageIndex + 1).toFloat() / displayedSurveyQuestions.size
    }

    fun canGoNext(): Boolean {
        val currentQuestion = getCurrentQuestion()
        return _state.value.answers[currentQuestion.id]?.isNotBlank() == true
    }

    fun canGoPrevious(): Boolean {
        return _state.value.currentPageIndex > 0
    }

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }
}