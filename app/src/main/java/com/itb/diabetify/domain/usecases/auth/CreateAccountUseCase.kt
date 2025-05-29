package com.itb.diabetify.domain.usecases.auth

import android.util.Patterns
import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.domain.model.auth.CreateAccountResult
import com.itb.diabetify.domain.repository.AuthRepository

class CreateAccountUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        dob: String,
        gender: String,
    ): CreateAccountResult {
        val nameError: String? = if (name.isEmpty()) "Nama tidak boleh kosong" else null
        val emailError: String? = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Email tidak valid" else null
        val passwordError: String? = if (password.length < 8) "Password harus lebih dari 8 karakter" else null
        val dobError: String? = if (dob.isEmpty()) "Tanggal lahir tidak boleh kosong" else null
        val genderError: String? = if (gender.isEmpty()) "Jenis kelamin tidak boleh kosong" else null

        if (nameError != null) {
            return CreateAccountResult(
                nameError = nameError
            )
        }

        if (emailError != null) {
            return CreateAccountResult(
                emailError = emailError
            )
        }

        if (passwordError != null) {
            return CreateAccountResult(
                passwordError = passwordError
            )
        }

        if (dobError != null) {
            return CreateAccountResult(
                dobError = dobError
            )
        }

        if (genderError != null) {
            return CreateAccountResult(
                genderError = genderError
            )
        }

        val createAccountRequest = CreateAccountRequest(
            name = name.trim(),
            email = email.trim(),
            password = password.trim(),
            dob = dob.trim(),
            gender = gender.trim(),
        )

        return CreateAccountResult(
            result = repository.createAccount(createAccountRequest)
        )
    }
}