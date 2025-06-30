package com.itb.diabetify.presentation.history

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.prediction.PredictionUseCases
import com.itb.diabetify.presentation.history.components.PredictionScoreEntry
import com.itb.diabetify.data.remote.prediction.response.PredictionData
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val predictionUseCases: PredictionUseCases
) : ViewModel() {
    private var _getPredictionByDateState = mutableStateOf(DataState())
    val getPredictionByDateState: State<DataState> = _getPredictionByDateState

    private var _getPredictionScoreByDateState = mutableStateOf(DataState())
    val getPredictionScoreByDateState: State<DataState> = _getPredictionScoreByDateState

    private val _selectedDate = mutableStateOf("")
    val selectedDate: State<String> = _selectedDate

    private val _predictionData = mutableStateOf(emptyList<List<Float>>())
    val predictionData: State<List<List<Float>>> = _predictionData

    private val _predictionScore = mutableStateOf(0.0f)
    val predictionScore: State<Float> = _predictionScore

    private val _predictionPercent = mutableStateOf("0.0")
    val predictionPercent: State<String> = _predictionPercent

    private val _riskLevel = mutableStateOf("Rendah")
    val riskLevel: State<String> = _riskLevel

    private val _lastPredictionAt = mutableStateOf("")
    val lastPredictionAt: State<String> = _lastPredictionAt

    private val _riskColor = mutableStateOf(androidx.compose.ui.graphics.Color.Green)

    private val _predictionScores = MutableStateFlow<List<PredictionScoreEntry>>(emptyList())
    val predictionScores: StateFlow<List<PredictionScoreEntry>> = _predictionScores

    private val _currentPrediction = MutableStateFlow<PredictionData?>(null)
    val currentPrediction: StateFlow<PredictionData?> = _currentPrediction

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val _dateState = mutableStateOf(
        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    )
    val dateState: State<String> = _dateState

    init {
        loadPredictionScores()
        loadPredictionForDate(_dateState.value)
    }

    fun setDate(date: String) {
        _dateState.value = date
        loadPredictionScores()
        loadPredictionForDate(date)
    }

    private fun loadPredictionForDate(date: String) {
        viewModelScope.launch {
            _getPredictionByDateState.value = getPredictionByDateState.value.copy(isLoading = true)

            val getPredictionResult = predictionUseCases.getPredictionByDate(
                startDate = date,
                endDate = date
            )

            _getPredictionByDateState.value = getPredictionByDateState.value.copy(isLoading = false)

            when (getPredictionResult.result) {
                is Resource.Success -> {
                    val prediction = getPredictionResult.result.data?.data?.firstOrNull()

                    _currentPrediction.value = prediction
                    Log.d("HistoryViewModel", "Prediction loaded successfully for date: $date")
                }
                is Resource.Error -> {
                    _errorMessage.value = getPredictionResult.result.message
                    Log.d("HistoryViewModel", "Error loading prediction: ${getPredictionResult.result.message}")
                }
                is Resource.Loading -> {
                    Log.d("HistoryViewModel", "Loading prediction...")
                }
                else -> {
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("HistoryViewModel", "Unexpected error")
                }
            }
        }
    }

    private fun loadPredictionScores() {
        viewModelScope.launch {
            _getPredictionScoreByDateState.value = getPredictionScoreByDateState.value.copy(isLoading = true)

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val selectedDate = LocalDate.parse(_dateState.value ?: LocalDate.now().format(formatter))

            val startDate = selectedDate.minusDays(15).format(formatter)
            val endDate = selectedDate.plusDays(15).format(formatter)

            val getPredictionScoreByDateResult = predictionUseCases.getPredictionScoreByDate(
                startDate = startDate,
                endDate = endDate
            )

            _getPredictionScoreByDateState.value = getPredictionScoreByDateState.value.copy(isLoading = false)

            when (getPredictionScoreByDateResult.result) {
                is Resource.Success -> {
                    val scores = if (getPredictionScoreByDateResult.result.data?.data != null) {
                        getPredictionScoreByDateResult.result.data.data.mapIndexedNotNull { index, scoreData ->
                            scoreData?.let {
                                val localDate = convertUtcToLocalDate(it.createdAt)
                                PredictionScoreEntry(
                                    day = index + 1,
                                    score = (it.riskScore.toFloat()) * 100f,
                                    date = localDate
                                )
                            }
                        }.sortedBy { it.date }
                    } else {
                        Log.d("HistoryViewModel", "No prediction scores data available")
                        emptyList()
                    }
                    _predictionScores.value = scores
                    Log.d("HistoryViewModel", "Prediction scores loaded successfully for range: $startDate to $endDate")
                }
                is Resource.Error -> {
                    _errorMessage.value = getPredictionScoreByDateResult.result.message
                    getPredictionScoreByDateResult.result.message?.let { Log.d("HistoryViewModel", it) }
                }
                is Resource.Loading -> {
                    Log.d("HistoryViewModel", "Loading prediction scores...")
                }
                else -> {
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("HistoryViewModel", "Unexpected error")
                }
            }
        }
    }

    private fun convertUtcToLocalDate(timestamp: String): String {
        return try {
            val zonedDateTime = ZonedDateTime.parse(timestamp.replace(" ", "T"))

            val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())

            localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } catch (e: Exception) {
            Log.e("HistoryViewModel", "Error parsing timestamp: $timestamp", e)

            try {
                val isoFormatted = timestamp.replace(" ", "T")
                val zonedDateTime = ZonedDateTime.parse(isoFormatted)
                val localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())
                localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (e2: Exception) {
                Log.e("HistoryViewModel", "Fallback parsing also failed for: $timestamp", e2)
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }
        }
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }
}