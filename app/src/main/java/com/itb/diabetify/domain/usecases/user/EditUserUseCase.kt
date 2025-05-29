package com.itb.diabetify.domain.usecases.user

import android.util.Patterns
import com.itb.diabetify.data.remote.user.request.EditUserRequest
import com.itb.diabetify.domain.model.user.EditUserResult
import com.itb.diabetify.domain.repository.UserRepository

class EditUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
    ): EditUserResult {
        val nameError: String? = if (name.isEmpty()) "Nama tidak boleh kosong" else null
        val emailError: String? = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Email tidak valid" else null

        if (nameError != null) {
            return EditUserResult(
                nameError = nameError
            )
        }

        if (emailError != null) {
            return EditUserResult(
                emailError = emailError
            )
        }

        val editUserRequest = EditUserRequest(
            name = name.trim(),
            email = email.trim(),
        )

        return EditUserResult(
            result = repository.editUser(editUserRequest)
        )
    }
}