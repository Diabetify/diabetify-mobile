package com.itb.diabetify.presentation.survey

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCases
import com.itb.diabetify.domain.usecases.profile.ProfileUseCases
import com.itb.diabetify.domain.usecases.user.UserUseCases
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val profileUseCases: ProfileUseCases,
    private val userUseCases: UserUseCases,
    private val predictionUseCases: PredictionUseCases
) : ViewModel() {
    // Navigation and Error States
    private val _navigationEvent = mutableStateOf<String?>(null)
    val navigationEvent: State<String?> = _navigationEvent

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Operational States
    private var _profileState = mutableStateOf(DataState())
    val profileState: State<DataState> = _profileState

    private var _activityState = mutableStateOf(DataState())
    val activityState: State<DataState> = _activityState

    private var _predictionState = mutableStateOf(DataState())
    val predictionState: State<DataState> = _predictionState

    // Survey State
    private val _state = mutableStateOf(SurveyState())
    val state: State<SurveyState> = _state

    private val surveyQuestions = questions

    // Setters for Survey State
    fun setAnswer(questionId: String, answer: String) {
        val newAnswers = _state.value.answers.toMutableMap()
        newAnswers[questionId] = answer

        val newErrors = _state.value.fieldErrors.toMutableMap()
        val validationError = validateField(questionId, answer)
        if (validationError != null) {
            newErrors[questionId] = validationError
        } else {
            newErrors.remove(questionId)
        }

        _state.value = _state.value.copy(
            answers = newAnswers,
            fieldErrors = newErrors
        )
    }

    // Validation Function
    private fun validateField(questionId: String, answer: String): String? {
        if (answer.isBlank()) {
            return "Mohon jawab pertanyaan ini"
        }

        val question = displayedSurveyQuestions.find { it.id == questionId }
        question?.let {
            when (it.questionType) {
                is SurveyQuestionType.Numeric -> {
                    val numericValue = answer.toIntOrNull() ?: return "Harap masukkan angka yang valid"

                    when (questionId) {
                        "weight" -> {
                            if (numericValue < 30 || numericValue > 300) {
                                return "Berat badan harus antara 30-300 kg"
                            }
                        }
                        "height" -> {
                            if (numericValue < 100 || numericValue > 250) {
                                return "Tinggi badan harus antara 100-250 cm"
                            }
                        }
                        "smoking_age" -> {
                            if (numericValue < 10 || numericValue > 80) {
                                return "Usia mulai merokok harus antara 10-80 tahun"
                            }
                        }
                        "smoking_amount" -> {
                            if (numericValue < 0 || numericValue > 60) {
                                return "Jumlah rokok harus antara 0-60 batang"
                            }
                        }
                        "systolic" -> {
                            if (numericValue < 70 || numericValue > 250) {
                                return "Tekanan sistolik harus antara 70-250 mmHg"
                            }
                        }
                        "diastolic" -> {
                            if (numericValue < 40 || numericValue > 150) {
                                return "Tekanan diastolik harus antara 40-150 mmHg"
                            }
                        }
                        "activity" -> {
                            if (numericValue < 0 || numericValue > 7) {
                                return "Aktivitas fisik harus antara 0-7 hari"
                            }
                        }
                    }
                }
                is SurveyQuestionType.Selection -> {
                    val validOptionIds = it.options.map { option -> option.id }
                    if (!validOptionIds.contains(answer)) {
                        return "Pilihan tidak valid"
                    }
                }
            }
        }
        
        return null
    }

    // Survey Helper Functions
    val displayedSurveyQuestions: List<SurveyQuestion>
        get() {
            val filteredQuestions = surveyQuestions.filter { question ->
                when (question.id) {
                    "smoking_age", "smoking_amount" -> _state.value.answers["smoking_status"] == "1" || _state.value.answers["smoking_status"] == "2"
                    "pregnancy" -> {
                        val user = runBlocking { userUseCases.getUserRepository().first() }
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
            val newErrors = _state.value.fieldErrors.toMutableMap()
            newErrors[currentQuestion.id] = "Mohon jawab pertanyaan ini dahulu"
            _state.value = _state.value.copy(fieldErrors = newErrors)
            return
        }

        val validationError = validateField(currentQuestion.id, answer)
        if (validationError != null) {
            val newErrors = _state.value.fieldErrors.toMutableMap()
            newErrors[currentQuestion.id] = validationError
            _state.value = _state.value.copy(fieldErrors = newErrors)
            return
        }

        val newErrors = _state.value.fieldErrors.toMutableMap()
        newErrors.remove(currentQuestion.id)
        _state.value = _state.value.copy(fieldErrors = newErrors)

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

    fun getCurrentQuestion(): SurveyQuestion {
        return displayedSurveyQuestions[_state.value.currentPageIndex]
    }

    fun getProgress(): Float {
        return (_state.value.currentPageIndex + 1).toFloat() / displayedSurveyQuestions.size
    }

    fun canGoNext(): Boolean {
        val currentQuestion = getCurrentQuestion()
        val answer = _state.value.answers[currentQuestion.id]
        val hasError = _state.value.fieldErrors[currentQuestion.id] != null
        return answer?.isNotBlank() == true && !hasError
    }

    fun canGoPrevious(): Boolean {
        return _state.value.currentPageIndex > 0
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

    // API Call Functions
    fun submitSurvey() {
        val errors = mutableMapOf<String, String>()
        
        displayedSurveyQuestions.forEach { question ->
            val answer = _state.value.answers[question.id]
            if (answer.isNullOrBlank()) {
                errors[question.id] = "Mohon jawab pertanyaan ini"
            } else {
                val validationError = validateField(question.id, answer)
                if (validationError != null) {
                    errors[question.id] = validationError
                }
            }
        }
        
        if (errors.isNotEmpty()) {
            _state.value = _state.value.copy(
                fieldErrors = errors,
                showReviewScreen = false
            )
            _errorMessage.value = "Harap perbaiki kesalahan pada form sebelum melanjutkan"
            return
        }
        
        _state.value = _state.value.copy(fieldErrors = emptyMap())
        
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)

            val weight = _state.value.answers["weight"]?.toIntOrNull() ?: 0
            val height = _state.value.answers["height"]?.toIntOrNull() ?: 0
            val hypertension = _state.value.answers["hypertension"]?.toBoolean() ?: false
            val macrosomicBaby = _state.value.answers["pregnancy"]?.toIntOrNull() ?: 2
            val smoking = _state.value.answers["smoking_status"]?.toIntOrNull() ?: 0
            val yearOfSmoking = _state.value.answers["smoking_age"]?.toIntOrNull() ?: 0
            val cholesterol = _state.value.answers["cholesterol"]?.toBoolean() ?: false
            val bloodline = _state.value.answers["bloodline"]?.toBoolean() ?: false
            val physicalActivityFrequency = _state.value.answers["activity"]?.toIntOrNull() ?: 0
            val smokingAmount = _state.value.answers["smoking_amount"]?.toIntOrNull() ?: 0

            val addProfileResult = profileUseCases.addProfile(
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

            if (addProfileResult.weightError != null) {
                _state.value = _state.value.copy(
                    fieldErrors = _state.value.fieldErrors + ("weight" to addProfileResult.weightError)
                )
            }

            if (addProfileResult.heightError != null) {
                _state.value = _state.value.copy(
                    fieldErrors = _state.value.fieldErrors + ("height" to addProfileResult.heightError)
                )
            }

            if (addProfileResult.hypertensionError != null) {
                _state.value = _state.value.copy(
                    fieldErrors = _state.value.fieldErrors + ("hypertension" to addProfileResult.hypertensionError)
                )
            }

            if (addProfileResult.yearOfSmokingError != null) {
                _state.value = _state.value.copy(
                    fieldErrors = _state.value.fieldErrors + ("smoking_age" to addProfileResult.yearOfSmokingError)
                )
            }

            if (addProfileResult.smokeCountError != null) {
                _state.value = _state.value.copy(
                    fieldErrors = _state.value.fieldErrors + ("smoking_amount" to addProfileResult.smokeCountError)
                )
            }

            if (addProfileResult.physicalActivityFrequencyError != null) {
                _state.value = _state.value.copy(
                    fieldErrors = _state.value.fieldErrors + ("activity" to addProfileResult.physicalActivityFrequencyError)
                )
            }

            when (addProfileResult.result) {
                is Resource.Success -> {
                    predict()
                }
                is Resource.Error -> {
                    _errorMessage.value = addProfileResult.result.message ?: "Terjadi kesalahan saat mengirimkan profil"
                    addProfileResult.result.message?.let { Log.d("SurveyViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("SurveyViewModel", "Profile submission loading")
                }

                else -> {
                    // Handle unexpected error
                    _errorMessage.value = "Terjadi kesalahan saat mengirimkan profil"
                    Log.e("SurveyViewModel", "Unexpected error in profile submission")
                }
            }
        }
    }

    private fun predict() {
        viewModelScope.launch {
            _predictionState.value = predictionState.value.copy(isLoading = true)

            val predictionResult = predictionUseCases.predict()

            _predictionState.value = predictionState.value.copy(isLoading = false)

            when (predictionResult.result) {
                is Resource.Success -> {
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
                    _errorMessage.value = "Terjadi kesalahan saat memprediksi"
                    Log.e("SurveyViewModel", "Unexpected error in prediction")
                }
            }
        }
    }

    // Helper Functions
    fun onNavigationHandled() {
        _navigationEvent.value = null
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }
}