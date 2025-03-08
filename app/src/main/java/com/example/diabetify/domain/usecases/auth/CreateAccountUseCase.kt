package com.example.diabetify.domain.usecases.auth

import com.example.diabetify.data.remote.auth.request.CreateAccountRequest
import com.example.diabetify.domain.model.CreateAccountResult
import com.example.diabetify.domain.repository.AuthRepository

class CreateAccountUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        age: Int,
        hipertension: Boolean,
        cholesterol: Boolean,
        disturbedVision: Boolean,
        weight: Int,
        height: Int,
        verified: Boolean
    ): CreateAccountResult {
        val nameError: String? = if (name.isBlank()) "Name cannot be empty" else null
        val emailError: String? = if (email.isBlank()) "Email cannot be empty" else null
        val passwordError: String? = if (password.isBlank()) "Password cannot be empty" else null
        val ageError: String? = if (age < 0) "Age cannot be negative" else null
        val hipertensionError: String? = null
        val cholesterolError: String? = null
        val disturbedVisionError: String? = null
        val weightError: String? = if (weight < 0) "Weight cannot be negative" else null
        val heightError: String? = if (height < 0) "Height cannot be negative" else null
        val verifyError: String? = null

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

        if (ageError != null) {
            return CreateAccountResult(
                ageError = ageError
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
            age = age,
            hipertension = hipertension,
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