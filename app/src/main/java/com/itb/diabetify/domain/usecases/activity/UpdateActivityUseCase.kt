package com.itb.diabetify.domain.usecases.activity

import android.annotation.SuppressLint
import com.itb.diabetify.data.remote.activity.request.UpdateActivityRequest
import com.itb.diabetify.domain.model.activity.UpdateActivityResult
import com.itb.diabetify.domain.repository.ActivityRepository
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

class UpdateActivityUseCase(
    private val repository: ActivityRepository
) {
    @SuppressLint("NewApi")
    suspend operator fun invoke(
        activityId: String,
        activityDate: String,
        activityType: String,
        value: Int,
    ): UpdateActivityResult {
        val activityIdError: String? = if (activityId.isEmpty()) "ID aktivitas tidak boleh kosong" else null
        val activityDateError: String? = when {
            try {
                ZonedDateTime.parse(activityDate)
                false
            } catch (e: DateTimeParseException) {
                true
            } -> "Format tanggal tidak valid"
            else -> null
        }
        val activityTypeError: String? = if (activityType != "workout" && activityType != "smoke") "Jenis aktivitas tidak valid" else null
        val valueError: String? = when {
            activityType == "smoke" && (value < 0 || value > 60) -> "Jumlah rokok tidak valid"
            activityType == "workout" && (value < 0 || value > 1) -> "Nilai aktivitas tidak valid"
            else -> null
        }

        if (activityIdError != null) {
            return UpdateActivityResult(
                activityIdError = activityIdError
            )
        }

        if (activityDateError != null) {
            return UpdateActivityResult(
                activityDateError = activityDateError
            )
        }

        if (activityTypeError != null) {
            return UpdateActivityResult(
                activityTypeError = activityTypeError
            )
        }

        if (valueError != null) {
            return UpdateActivityResult(
                valueError = valueError
            )
        }

        val updateActivityRequest = UpdateActivityRequest(
            activityDate = activityDate.trim(),
            activityType = activityType.trim(),
            value = value
        )

        return UpdateActivityResult(
            result = repository.updateActivity(activityId, updateActivityRequest)
        )
    }
}