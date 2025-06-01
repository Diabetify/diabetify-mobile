package com.itb.diabetify.presentation.history

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.prediction.GetPredictionScoreByDateUseCase
import com.itb.diabetify.presentation.history.components.PredictionScoreEntry
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
) : ViewModel() {
    private var _predictionScoreState = mutableStateOf(DataState())
    val predictionScoreState: State<DataState> = _predictionScoreState

    private val _predictionScores = MutableStateFlow<List<PredictionScoreEntry>>(emptyList())
    val predictionScores: StateFlow<List<PredictionScoreEntry>> = _predictionScores

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


    private val _dateState = mutableStateOf(
        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    )
    val dateState: State<String> = _dateState

    fun setDate(date: String) {
        _dateState.value = date
        loadPredictionScore()
    }

    init {
        loadPredictionScore()
    }

    private fun loadPredictionScore() {
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
                    val scores = getPredictionScoreByDateResult.result.data?.let { response ->
                        response.data.mapIndexed { index, scoreData ->
                            PredictionScoreEntry(
                                day = index + 1,
                                score = (scoreData?.riskScore?.toFloat() ?: 0f) * 100f
                            )
                        }
                    } ?: emptyList()
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
                    // Handle unexpected error
                    _errorMessage.value = "Unknown error occurred"
                    Log.d("HistoryViewModel", "Unexpected error")
                }
            }
        }
    }
}