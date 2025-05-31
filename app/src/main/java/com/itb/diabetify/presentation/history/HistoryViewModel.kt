package com.itb.diabetify.presentation.history

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itb.diabetify.domain.usecases.prediction.GetPredictionScoreByDateUseCase
import com.itb.diabetify.util.DataState
import com.itb.diabetify.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _errorMessage = mutableStateOf<String?>(null)

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