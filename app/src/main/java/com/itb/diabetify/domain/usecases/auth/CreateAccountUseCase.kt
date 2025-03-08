package com.itb.diabetify.domain.usecases.auth

import android.util.Patterns
import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.domain.model.CreateAccountResult
import com.itb.diabetify.domain.repository.AuthRepository

class CreateAccountUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        dob: String,
        hypertension: Boolean,
        gender: String,
        cholesterol: Boolean,
        disturbedVision: Boolean,
        weight: Int,
        height: Int,
        verified: Boolean
    ): CreateAccountResult {
        val nameError: String? = if (name.isEmpty()) "Nama tidak boleh kosong" else null
        val emailError: String? = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Email tidak valid" else null
        val passwordError: String? = if (password.length < 8) "Password harus lebih dari 8 karakter" else null
        val dobError: String? = if (dob.isEmpty()) "Tanggal lahir tidak boleh kosong" else null
        val genderError: String? = if (gender.isEmpty()) "Jenis kelamin tidak boleh kosong" else null
        val weightError: String? = if (weight < 0) "Weight cannot be negative" else null
        val heightError: String? = if (height < 0) "Height cannot be negative" else null

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

        if (weightError != null) {
            return CreateAccountResult(
                weightError = weightError
            )
        }

        if (heightError != null) {
            return CreateAccountResult(
                heightError = heightError
            )
        }

        val createAccountRequest = CreateAccountRequest(
            name = name.trim(),
            email = email.trim(),
            password = password.trim(),
            dob = dob.trim(),
            hypertension = hypertension,
            gender = gender.trim(),
            cholesterol = cholesterol,
            disturbedVision = disturbedVision,
            weight = weight,
            height = height,
            verified = verified
        )

        return CreateAccountResult(
            result = repository.createAccount(createAccountRequest)
        )
    }
}