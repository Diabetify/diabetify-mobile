package com.itb.diabetify.presentation.history

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.prediction.GetPredictionByDateUseCase
import com.itb.diabetify.domain.usecases.prediction.GetPredictionScoreByDateUseCase
import com.itb.diabetify.presentation.history.components.PredictionScoreEntry
import com.itb.diabetify.data.remote.prediction.response.PredictionData
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getPredictionScoreByDateUseCase: GetPredictionScoreByDateUseCase,
    private val getPredictionByDateUseCase: GetPredictionByDateUseCase
) : ViewModel() {
    private var _predictionScoreState = mutableStateOf(DataState())
    val predictionScoreState: State<DataState> = _predictionScoreState

    private val _predictionState = mutableStateOf(DataState())
    val predictionState: State<DataState> = _predictionState

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
            _predictionState.value = _predictionState.value.copy(isLoading = true)

            val getPredictionResult = getPredictionByDateUseCase(
                startDate = date,
                endDate = date
            )

            _predictionState.value = _predictionState.value.copy(isLoading = false)

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
            _predictionScoreState.value = predictionScoreState.value.copy(isLoading = true)

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val selectedDate = LocalDate.parse(_dateState.value ?: LocalDate.now().format(formatter))
            
            val startDate = selectedDate.minusDays(15).format(formatter)
            val endDate = selectedDate.plusDays(15).format(formatter)
            
            val getPredictionScoreByDateResult = getPredictionScoreByDateUseCase(
                startDate = startDate,
                endDate = endDate
            )

            _predictionScoreState.value = predictionScoreState.value.copy(isLoading = false)

            when (getPredictionScoreByDateResult.result) {
                is Resource.Success -> {
                    val scores = if (getPredictionScoreByDateResult.result.data?.data != null) {
                        getPredictionScoreByDateResult.result.data.data.mapIndexed { index, scoreData ->
                            PredictionScoreEntry(
                                day = index + 1,
                                score = (scoreData?.riskScore?.toFloat() ?: 0f) * 100f
                            )
                        }
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
}