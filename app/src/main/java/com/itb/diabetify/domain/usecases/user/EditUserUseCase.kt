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
        gender: String,
        dob: String
    ): EditUserResult {
        val nameError: String? = if (name.isEmpty()) "Nama tidak boleh kosong" else null
        val emailError: String? = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Email tidak valid" else null
        val genderError: String? = if (gender.isEmpty()) "Jenis kelamin tidak boleh kosong" else null
        val dobError: String? = if (dob.isEmpty()) "Tanggal lahir tidak boleh kosong" else null

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

        if (genderError != null) {
            return EditUserResult(
                genderError = genderError
            )
        }

        if (dobError != null) {
            return EditUserResult(
                dobError = dobError
            )
        }


        val editUserRequest = EditUserRequest(
            name = name.trim(),
            email = email.trim(),
            gender = gender.trim(),
            dob = dob.trim(),
        )

        return EditUserResult(
            result = repository.editUser(editUserRequest)
        )
    }
}