package com.itb.diabetify.presentation.survey

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.profile.AddProfileUseCase
import com.itb.diabetify.domain.repository.UserRepository
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCase
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val addProfileUseCase: AddProfileUseCase,
    private val userRepository: UserRepository,
    private val predictionUseCase: PredictionUseCase
) : ViewModel() {
    private var _profileState = mutableStateOf(DataState())
    val profileState: State<DataState> = _profileState

    private var _activityState = mutableStateOf(DataState())
    val activityState: State<DataState> = _activityState

    private var _predictionState = mutableStateOf(DataState())
    val predictionState: State<DataState> = _predictionState

    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


    private val _state = mutableStateOf(SurveyState())
    val state: State<SurveyState> = _state

    private val surveyQuestions = questions

    val displayedSurveyQuestions: List<SurveyQuestion>
        get() {
            var filteredQuestions = surveyQuestions.filter { question ->
                when (question.id) {
                    "smoking_age", "smoking_amount" -> _state.value.answers["smoking_status"] == "1" || _state.value.answers["smoking_status"] == "2"
                    "pregnancy" -> {
                        val user = runBlocking { userRepository.getUser().first() }
                        user?.gender?.lowercase() != "laki-laki" && user?.gender?.lowercase() != "male"
                    }
                    "systolic", "diastolic" -> _state.value.answers["bp_unknown"] == "yes"
                    "hypertension" -> _state.value.answers["bp_unknown"] == "no"
                    else -> true
                }
            }
            return filteredQuestions
        }

    fun nextPage() {
        val currentQuestion = displayedSurveyQuestions[_state.value.currentPageIndex]
        val answer = _state.value.answers[currentQuestion.id]

        if (answer.isNullOrBlank()) {
            _errorMessage.value = "Mohon jawab pertanyaan ini dahulu"
            return
        }

        if (currentQuestion.id == "diastolic" && _state.value.answers["bp_unknown"] == "yes") {
            val systolic = _state.value.answers["systolic"]?.toIntOrNull()
            val diastolic = _state.value.answers["diastolic"]?.toIntOrNull()
            
            if (systolic != null && diastolic != null) {
                val hasHypertension = systolic >= 140 || diastolic >= 90
                setAnswer("hypertension", hasHypertension.toString())
            }
        }

        if (_state.value.currentPageIndex < displayedSurveyQuestions.size - 1) {
            _state.value = _state.value.copy(
                currentPageIndex = _state.value.currentPageIndex + 1
            )
        } else {
            // Show review screen before submitting
            _state.value = _state.value.copy(
                showReviewScreen = true
            )
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

            val weight = _state.value.answers["weight"]?.toIntOrNull() ?: 0
            val height = _state.value.answers["height"]?.toIntOrNull() ?: 0
            val hypertension = _state.value.answers["hypertension"]?.toBoolean() ?: false
            val macrosomicBaby = _state.value.answers["pregnancy"]?.toBoolean() ?: false
            val smoking = _state.value.answers["smoking_status"]?.toBoolean() ?: false
            val yearOfSmoking = _state.value.answers["smoking_age"]?.toIntOrNull() ?: 0
            val cholesterol = _state.value.answers["cholesterol"]?.toBoolean() ?: false
            val bloodline = _state.value.answers["bloodline"]?.toBoolean() ?: false
            val physicalActivityFrequency = _state.value.answers["activity"]?.toIntOrNull() ?: 0
            val smokingAmount = _state.value.answers["smoking_amount"]?.toIntOrNull() ?: 0

            val addProfileResult = addProfileUseCase(
                weight = weight,
                height = height,
                hypertension = hypertension,
                macrosomicBaby = macrosomicBaby,
                smoking = smoking,
                yearOfSmoking = yearOfSmoking,
                cholesterol = cholesterol,
                bloodline = bloodline,
                physicalActivityFrequency = physicalActivityFrequency,
                smokeCount = smokingAmount
            )

            _profileState.value = profileState.value.copy(isLoading = false)

            when (addProfileResult.result) {
                is Resource.Success -> {
                    Log.d("SurveyViewModel", "Profile submission successful, proceeding with activity submission")
                    predict()
                }
                is Resource.Error -> {
                    Log.d("SurveyViewModel", "Profile submission error: ${addProfileResult.result.message}")
                    _errorMessage.value = addProfileResult.result.message ?: "Terjadi kesalahan saat mengirimkan profil"
                }
                is Resource.Loading -> {
                    Log.d("SurveyViewModel", "Profile submission loading")
                }
                else -> {
                    // Handle unexpected error
                    Log.e("SurveyViewModel", "Unexpected error in profile submission")
                    _errorMessage.value = "Terjadi kesalahan saat mengirimkan profil"
                }
            }
        }
    }

    private fun predict() {
        viewModelScope.launch {
            _predictionState.value = predictionState.value.copy(isLoading = true)

            val predictionResult = predictionUseCase()

            _predictionState.value = predictionState.value.copy(isLoading = false)

            when (predictionResult.result) {
                is Resource.Success -> {
                    Log.d("SurveyViewModel", "Prediction successful")
                    _navigationEvent.value = "SUCCESS_SCREEN"
                }
                is Resource.Error -> {
                    Log.d("SurveyViewModel", "Prediction error: ${predictionResult.result.message}")
                    _errorMessage.value = predictionResult.result.message ?: "Terjadi kesalahan saat memprediksi"
                }
                is Resource.Loading -> {
                    Log.d("SurveyViewModel", "Prediction loading")
                }
                else -> {
                    // Handle unexpected error
                    Log.e("SurveyViewModel", "Unexpected error in prediction")
                    _errorMessage.value = "Terjadi kesalahan saat memprediksi"
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

    fun onErrorShown() {
        _errorMessage.value = null
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

    fun confirmAndSubmit() {
        submitSurvey()
    }

    fun backToSurvey() {
        _state.value = _state.value.copy(
            showReviewScreen = false
        )
    }

    fun getAnsweredQuestions(): List<Pair<SurveyQuestion, String>> {
        return displayedSurveyQuestions.mapNotNull { question ->
            _state.value.answers[question.id]?.let { answer ->
                question to answer
            }
        }
    }

    fun getFormattedAnswer(question: SurveyQuestion, answer: String): String {
        return when (question.questionType) {
            SurveyQuestionType.Selection -> {
                question.options.find { it.id == answer }?.text ?: answer
            }
            SurveyQuestionType.Numeric -> {
                "$answer ${question.numericUnit}"
            }
        }
    }
}