package com.itb.diabetify.domain.usecases.activity

import com.itb.diabetify.data.remote.activity.request.AddActivityRequest
import com.itb.diabetify.domain.model.activity.AddActivityResult
import com.itb.diabetify.domain.repository.ActivityRepository

class AddActivityUseCase(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(
        activityDate: String,
        activityType: String,
        value: Int,
    ): AddActivityResult {
        val activityDateError: String? = if (activityDate.isEmpty()) "Tanggal aktivitas tidak boleh kosong" else null
        val activityTypeError: String? = if (activityType.isEmpty()) "Jenis aktivitas tidak boleh kosong" else null
        val valueError: String? = if (value < 0) "Nilai aktivitas harus lebih dari 0" else null

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