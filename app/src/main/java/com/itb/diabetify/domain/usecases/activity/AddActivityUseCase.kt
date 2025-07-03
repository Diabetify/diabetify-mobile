package com.itb.diabetify.domain.usecases.activity

import android.annotation.SuppressLint
import com.itb.diabetify.data.remote.activity.request.AddActivityRequest
import com.itb.diabetify.domain.model.activity.AddActivityResult
import com.itb.diabetify.domain.repository.ActivityRepository
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

class AddActivityUseCase(
    private val repository: ActivityRepository
) {
    @SuppressLint("NewApi")
    suspend operator fun invoke(
        activityDate: String,
        activityType: String,
        value: Int,
    ): AddActivityResult {
        val activityDateError: String? = when {
            try {
                ZonedDateTime.parse(activityDate)
                false
            } catch (e: DateTimeParseException) {
                true
            } -> "Format tanggal tidak valid. Gunakan format ISO 8601"
            else -> null
        }
        val activityTypeError: String? = if (activityType != "workout" && activityType != "smoke") "Jenis aktivitas harus 'workout' atau 'smoke'" else null
        val valueError: String? = when {
            activityType == "smoke" && (value < 0 || value > 60) -> "Nilai untuk aktivitas 'smoke' harus antara 0 dan 60"
            activityType == "workout" && (value < 0 || value > 1) -> "Nilai untuk aktivitas 'workout' harus 0 atau 1"
            else -> null
        }

        if (activityDateError != null) {
            return AddActivityResult(
                activityDateError = activityDateError
            )
        }

        if (activityTypeError != null) {
            return AddActivityResult(
                activityTypeError = activityTypeError
            )
        }

        if (valueError != null) {
            return AddActivityResult(
                valueError = valueError
            )
        }

        val addActivityResult = AddActivityRequest(
            activityDate = activityDate.trim(),
            activityType = activityType.trim(),
            value = value
        )

        return AddActivityResult(
            result = repository.addActivity(addActivityResult)
        )
    }
}