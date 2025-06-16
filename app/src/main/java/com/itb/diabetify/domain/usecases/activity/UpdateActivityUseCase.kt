package com.itb.diabetify.domain.usecases.activity

import com.itb.diabetify.data.remote.activity.request.UpdateActivityRequest
import com.itb.diabetify.domain.model.activity.UpdateActivityResult
import com.itb.diabetify.domain.repository.ActivityRepository

class UpdateActivityUseCase(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(
        activityId: String,
        activityDate: String,
        activityType: String,
        value: Int,
    ): UpdateActivityResult {
        val activityIdError: String? = if (activityId.isEmpty()) "ID aktivitas tidak boleh kosong" else null
        val activityDateError: String? = if (activityDate.isEmpty()) "Tanggal aktivitas tidak boleh kosong" else null
        val activityTypeError: String? = if (activityType.isEmpty()) "Jenis aktivitas tidak boleh kosong" else null
        val valueError: String? = if (value < 0) "Nilai aktivitas harus lebih dari 0" else null

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