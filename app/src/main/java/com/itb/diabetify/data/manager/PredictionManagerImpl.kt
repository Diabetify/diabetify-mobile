package com.itb.diabetify.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.itb.diabetify.domain.manager.PredictionManager
import com.itb.diabetify.domain.model.Prediction
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.predictionDataStore: DataStore<Preferences> by preferencesDataStore(name = "prediction_data")

@Singleton
class PredictionManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : PredictionManager {

    private object PreferencesKeys {
        val PREDICTION_DATA = stringPreferencesKey("prediction_data")
    }

    override suspend fun savePrediction(prediction: Prediction) {
        context.predictionDataStore.edit { preferences ->
            preferences[PreferencesKeys.PREDICTION_DATA] = gson.toJson(prediction)
        }
    }

    override fun getLatestPrediction(): Flow<Prediction?> {
        return context.predictionDataStore.data.map { preferences ->
            preferences[PreferencesKeys.PREDICTION_DATA]?.let {
                gson.fromJson(it, Prediction::class.java)
            }
        }
    }

    override suspend fun clearPrediction() {
        context.predictionDataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.PREDICTION_DATA)
        }
    }

}