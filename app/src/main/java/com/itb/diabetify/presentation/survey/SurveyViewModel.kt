package com.itb.diabetify.presentation.survey

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.profile.AddProfileUseCase
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val addProfileUseCase: AddProfileUseCase
) : ViewModel() {
    private var _profileState = mutableStateOf(DataState())
    val profileState: State<DataState> = _profileState

    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


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
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)

            val weight = _state.value.answers["weight"]?.toIntOrNull()
            val height = _state.value.answers["height"]?.toIntOrNull()
            val hypertension = _state.value.answers["hypertension"]?.toBoolean() ?: false
            val macrosomicBaby = _state.value.answers["pregnancy"]?.toBoolean() ?: false
            val smoking = _state.value.answers["smoking_status"]?.toBoolean() ?: false
            val yearOfSmoking = _state.value.answers["smoking_age"]?.toIntOrNull()

            val addProfileResult = addProfileUseCase(
                weight = weight.toString(),
                height = height.toString(),
                hypertension = hypertension,
                macrosomicBaby = macrosomicBaby,
                smoking = smoking,
                yearOfSmoking = yearOfSmoking
            )

            _profileState.value = profileState.value.copy(isLoading = false)

            when (addProfileResult.result) {
                is Resource.Success -> {
                    Log.d("SurveyViewModel", "Survey submission successful")
                    _navigationEvent.value = "SUCCESS_SCREEN"
                }
                is Resource.Error -> {
                    Log.d("SurveyViewModel", "Survey submission error: ${addProfileResult.result.message}")
                    showSnackbar(addProfileResult.result.message ?: "Terjadi kesalahan saat mengirimkan survei")
                    _errorMessage.value = addProfileResult.result.message ?: "Unknown error occurred"
                    addProfileResult.result.message?.let { Log.d("SurveyViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("SurveyViewModel", "Loading")
                }
                else -> {
                    // Handle unexpected error
                    Log.e("SurveyViewModel", "Unexpected error state")
                    showSnackbar("Terjadi kesalahan saat mengirimkan survei")
                    _errorMessage.value = "Unknown error occurred"
                }
            }
        }
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