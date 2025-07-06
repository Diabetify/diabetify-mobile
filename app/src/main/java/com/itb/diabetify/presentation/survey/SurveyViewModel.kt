package com.itb.diabetify.presentation.survey

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCases
import com.itb.diabetify.domain.usecases.profile.ProfileUseCases
import com.itb.diabetify.domain.usecases.user.UserUseCases
import com.itb.diabetify.presentation.common.FieldState
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    private var _userState = mutableStateOf(DataState())
    val userState: State<DataState> = _userState

    private var _profileState = mutableStateOf(DataState())
    val profileState: State<DataState> = _profileState

    private var _activityState = mutableStateOf(DataState())
    val activityState: State<DataState> = _activityState

    private var _predictionState = mutableStateOf(DataState())
    val predictionState: State<DataState> = _predictionState

    // UI States
    private val _gender = mutableStateOf("")
    val gender: State<String> = _gender

    private val _age = mutableIntStateOf(0)
    val age: State<Int> = _age

    // Survey States
    data class SurveyState(
        val currentPageIndex: Int = 0,
        val fieldStates: Map<String, FieldState> = emptyMap(),
        val isComplete: Boolean = false,
        val showReviewScreen: Boolean = false
    )

    private val _surveyState = mutableStateOf(SurveyState())
    val surveyState: State<SurveyState> = _surveyState

    private val surveyQuestions = questions

    // Initialization
    init {
        collectUserData()
    }

    // Setters for Survey State
    fun setAnswer(questionId: String, answer: String) {
        val newFieldStates = _surveyState.value.fieldStates.toMutableMap()
        val currentFieldState = newFieldStates[questionId] ?: FieldState()

        val validationError = validateField(questionId, answer)
        newFieldStates[questionId] = currentFieldState.copy(
            text = answer,
            error = validationError
        )

        _surveyState.value = _surveyState.value.copy(
            fieldStates = newFieldStates
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

                            if (_age.intValue in 1..<numericValue) {
                                return "Usia mulai merokok tidak boleh lebih dari usia Anda saat ini (${_age.intValue} tahun)"
                            }
                        }
                        "smoking_end_age" -> {
                            if (numericValue < 10 || numericValue > 80) {
                                return "Usia berhenti merokok harus antara 10-80 tahun"
                            }

                            if (_age.intValue in 1..<numericValue) {
                                return "Usia berhenti merokok tidak boleh lebih dari usia Anda saat ini (${_age.intValue} tahun)"
                            }

                            val smokingStartAge = _surveyState.value.fieldStates["smoking_age"]?.text?.toIntOrNull()
                            if (smokingStartAge != null && numericValue <= smokingStartAge) {
                                return "Usia berhenti merokok harus lebih besar dari usia mulai merokok"
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

    fun validateSurveyFields(): Boolean {
        val newFieldStates = _surveyState.value.fieldStates.toMutableMap()
        var hasErrors = false

        displayedSurveyQuestions.forEach { question ->
            val fieldState = _surveyState.value.fieldStates[question.id]
            val answer = fieldState?.text
            val currentFieldState = newFieldStates[question.id] ?: FieldState()

            if (answer.isNullOrBlank()) {
                newFieldStates[question.id] = currentFieldState.copy(
                    error = "Mohon jawab pertanyaan ini"
                )
                hasErrors = true
            } else {
                val validationError = validateField(question.id, answer)
                if (validationError != null) {
                    newFieldStates[question.id] = currentFieldState.copy(
                        error = validationError
                    )
                    hasErrors = true
                } else {
                    newFieldStates[question.id] = currentFieldState.copy(
                        error = null
                    )
                }
            }
        }

        _surveyState.value = _surveyState.value.copy(fieldStates = newFieldStates)

        if (hasErrors) {
            _surveyState.value = _surveyState.value.copy(showReviewScreen = false)
            _errorMessage.value = "Harap perbaiki kesalahan pada form sebelum melanjutkan"
        }

        return !hasErrors
    }

    // Survey Helper Functions
    val displayedSurveyQuestions: List<SurveyQuestion>
        get() {
            val filteredQuestions = surveyQuestions.filter { question ->
                when (question.id) {
                    "smoking_age", "smoking_amount" -> _surveyState.value.fieldStates["smoking_status"]?.text == "1" || _surveyState.value.fieldStates["smoking_status"]?.text == "2"
                    "smoking_end_age" -> _surveyState.value.fieldStates["smoking_status"]?.text == "1"
                    "pregnancy" -> _gender.value.lowercase() != "laki-laki" && _gender.value.lowercase() != "male"
                    "systolic", "diastolic" -> _surveyState.value.fieldStates["bp_unknown"]?.text == "yes"
                    "hypertension" -> _surveyState.value.fieldStates["bp_unknown"]?.text == "no"
                    else -> true
                }
            }
            return filteredQuestions
        }

    fun nextPage() {
        val currentQuestion = displayedSurveyQuestions[_surveyState.value.currentPageIndex]
        val fieldState = _surveyState.value.fieldStates[currentQuestion.id]
        val answer = fieldState?.text

        if (answer.isNullOrBlank()) {
            val newFieldStates = _surveyState.value.fieldStates.toMutableMap()
            val currentFieldState = newFieldStates[currentQuestion.id] ?: FieldState()
            newFieldStates[currentQuestion.id] = currentFieldState.copy(
                error = "Mohon jawab pertanyaan ini dahulu"
            )
            _surveyState.value = _surveyState.value.copy(fieldStates = newFieldStates)
            return
        }

        val validationError = validateField(currentQuestion.id, answer)
        if (validationError != null) {
            val newFieldStates = _surveyState.value.fieldStates.toMutableMap()
            val currentFieldState = newFieldStates[currentQuestion.id] ?: FieldState()
            newFieldStates[currentQuestion.id] = currentFieldState.copy(
                error = validationError
            )
            _surveyState.value = _surveyState.value.copy(fieldStates = newFieldStates)
            return
        }

        val newFieldStates = _surveyState.value.fieldStates.toMutableMap()
        val currentFieldState = newFieldStates[currentQuestion.id] ?: FieldState()
        newFieldStates[currentQuestion.id] = currentFieldState.copy(
            error = null
        )
        _surveyState.value = _surveyState.value.copy(fieldStates = newFieldStates)

        if (currentQuestion.id == "diastolic" && _surveyState.value.fieldStates["bp_unknown"]?.text == "yes") {
            val systolic = _surveyState.value.fieldStates["systolic"]?.text?.toIntOrNull()
            val diastolic = _surveyState.value.fieldStates["diastolic"]?.text?.toIntOrNull()

            if (systolic != null && diastolic != null) {
                val hasHypertension = systolic >= 140 || diastolic >= 90
                setAnswer("hypertension", hasHypertension.toString())
            }
        }

        if (_surveyState.value.currentPageIndex < displayedSurveyQuestions.size - 1) {
            _surveyState.value = _surveyState.value.copy(
                currentPageIndex = _surveyState.value.currentPageIndex + 1
            )
        } else {
            _surveyState.value = _surveyState.value.copy(
                showReviewScreen = true
            )
        }
    }

    fun previousPage() {
        if (_surveyState.value.currentPageIndex > 0) {
            _surveyState.value = _surveyState.value.copy(
                currentPageIndex = _surveyState.value.currentPageIndex - 1
            )
        }
    }

    fun getCurrentQuestion(): SurveyQuestion {
        return displayedSurveyQuestions[_surveyState.value.currentPageIndex]
    }

    fun getProgress(): Float {
        return (_surveyState.value.currentPageIndex + 1).toFloat() / displayedSurveyQuestions.size
    }

    fun canGoNext(): Boolean {
        val currentQuestion = getCurrentQuestion()
        val fieldState = _surveyState.value.fieldStates[currentQuestion.id]
        val answer = fieldState?.text
        val hasError = fieldState?.error != null
        return answer?.isNotBlank() == true && !hasError
    }

    fun canGoPrevious(): Boolean {
        return _surveyState.value.currentPageIndex > 0
    }

    fun backToSurvey() {
        _surveyState.value = _surveyState.value.copy(
            showReviewScreen = false
        )
    }

    fun getAnsweredQuestions(): List<Pair<SurveyQuestion, String>> {
        return displayedSurveyQuestions.mapNotNull { question ->
            _surveyState.value.fieldStates[question.id]?.text?.takeIf { it.isNotBlank() }?.let { answer ->
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

    // Use Case Calls
    private fun collectUserData() {
        viewModelScope.launch {
            _userState.value = userState.value.copy(isLoading = true)

            userUseCases.getUserRepository().onEach { user ->
                _userState.value = userState.value.copy(isLoading = false)

                user?.let {
                    _gender.value = it.gender
                    it.dob.let { dob ->
                        _age.intValue = calculateAgeFromDob(dob)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun submitSurvey() {
        viewModelScope.launch {
            _profileState.value = profileState.value.copy(isLoading = true)

            val weight = _surveyState.value.fieldStates["weight"]?.text?.toIntOrNull() ?: 60
            val height = _surveyState.value.fieldStates["height"]?.text?.toIntOrNull() ?: 170
            val hypertension = _surveyState.value.fieldStates["hypertension"]?.text?.toBoolean() ?: false
            val macrosomicBaby = _surveyState.value.fieldStates["pregnancy"]?.text?.toIntOrNull() ?: 2
            val smoking = _surveyState.value.fieldStates["smoking_status"]?.text?.toIntOrNull() ?: 0
            val ageOfSmoking = _surveyState.value.fieldStates["smoking_age"]?.text?.toIntOrNull() ?: 0
            val ageOfStopSmoking = _surveyState.value.fieldStates["smoking_end_age"]?.text?.toIntOrNull() ?: 0
            val cholesterol = _surveyState.value.fieldStates["cholesterol"]?.text?.toBoolean() ?: false
            val bloodline = _surveyState.value.fieldStates["bloodline"]?.text?.toBoolean() ?: false
            val physicalActivityFrequency = _surveyState.value.fieldStates["activity"]?.text?.toIntOrNull() ?: 0
            val smokingAmount = _surveyState.value.fieldStates["smoking_amount"]?.text?.toIntOrNull() ?: 0

            val addProfileResult = profileUseCases.addProfile(
                weight = weight,
                height = height,
                hypertension = hypertension,
                macrosomicBaby = macrosomicBaby,
                smoking = smoking,
                ageOfSmoking = ageOfSmoking,
                ageOfStopSmoking = ageOfStopSmoking,
                cholesterol = cholesterol,
                bloodline = bloodline,
                physicalActivityFrequency = physicalActivityFrequency,
                smokeCount = smokingAmount
            )

            _profileState.value = profileState.value.copy(isLoading = false)

            val updatedFieldStates = _surveyState.value.fieldStates.toMutableMap()

            if (addProfileResult.weightError != null) {
                val currentFieldState = updatedFieldStates["weight"] ?: FieldState()
                updatedFieldStates["weight"] = currentFieldState.copy(error = addProfileResult.weightError)
            }

            if (addProfileResult.heightError != null) {
                val currentFieldState = updatedFieldStates["height"] ?: FieldState()
                updatedFieldStates["height"] = currentFieldState.copy(error = addProfileResult.heightError)
            }

            if (addProfileResult.hypertensionError != null) {
                val currentFieldState = updatedFieldStates["hypertension"] ?: FieldState()
                updatedFieldStates["hypertension"] = currentFieldState.copy(error = addProfileResult.hypertensionError)
            }

            if (addProfileResult.macrosomicBabyError != null) {
                val currentFieldState = updatedFieldStates["pregnancy"] ?: FieldState()
                updatedFieldStates["pregnancy"] = currentFieldState.copy(error = addProfileResult.macrosomicBabyError)
            }

            if (addProfileResult.smokingError != null) {
                val currentFieldState = updatedFieldStates["smoking_status"] ?: FieldState()
                updatedFieldStates["smoking_status"] = currentFieldState.copy(error = addProfileResult.smokingError)
            }

            if (addProfileResult.ageOfSmokingError != null) {
                val currentFieldState = updatedFieldStates["smoking_age"] ?: FieldState()
                updatedFieldStates["smoking_age"] = currentFieldState.copy(error = addProfileResult.ageOfSmokingError)
            }

            if (addProfileResult.ageOfStopSmokingError != null) {
                val currentFieldState = updatedFieldStates["smoking_end_age"] ?: FieldState()
                updatedFieldStates["smoking_end_age"] = currentFieldState.copy(error = addProfileResult.ageOfStopSmokingError)
            }

            if (addProfileResult.cholesterolError != null) {
                val currentFieldState = updatedFieldStates["cholesterol"] ?: FieldState()
                updatedFieldStates["cholesterol"] = currentFieldState.copy(error = addProfileResult.cholesterolError)
            }

            if (addProfileResult.bloodlineError != null) {
                val currentFieldState = updatedFieldStates["bloodline"] ?: FieldState()
                updatedFieldStates["bloodline"] = currentFieldState.copy(error = addProfileResult.bloodlineError)
            }

            if (addProfileResult.physicalActivityFrequencyError != null) {
                val currentFieldState = updatedFieldStates["activity"] ?: FieldState()
                updatedFieldStates["activity"] = currentFieldState.copy(error = addProfileResult.physicalActivityFrequencyError)
            }

            if (addProfileResult.smokeCountError != null) {
                val currentFieldState = updatedFieldStates["smoking_amount"] ?: FieldState()
                updatedFieldStates["smoking_amount"] = currentFieldState.copy(error = addProfileResult.smokeCountError)
            }

            _surveyState.value = _surveyState.value.copy(fieldStates = updatedFieldStates)

            when (addProfileResult.result) {
                is Resource.Success -> {
                    predict()
                }
                is Resource.Error -> {
                    _errorMessage.value = addProfileResult.result.message ?: "Terjadi kesalahan saat mengirimkan profil"
                    addProfileResult.result.message?.let { Log.e("SurveyViewModel", it) }
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
                    Log.e("SurveyViewModel", "Prediction error: ${predictionResult.result.message}")
                    _navigationEvent.value = "SUCCESS_SCREEN"
                }

                else -> {
                    // Handle unexpected error
                    Log.e("SurveyViewModel", "Unexpected error in prediction")
                    _navigationEvent.value = "SUCCESS_SCREEN"
                }
            }
        }
    }

    // Helper Functions
    private fun calculateAgeFromDob(dob: String): Int {
        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val birthDate = dateFormat.parse(dob)
            val currentDate = Calendar.getInstance()
            val birthCalendar = Calendar.getInstance()

            if (birthDate != null) {
                birthCalendar.time = birthDate

                var age = currentDate.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

                if (currentDate.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                    age--
                }

                age
            } else {
                0
            }
        } catch (e: Exception) {
            Log.e("SurveyViewModel", "Error calculating age from DOB: $dob", e)
            0
        }
    }

    fun onNavigationHandled() {
        _navigationEvent.value = null
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }
}