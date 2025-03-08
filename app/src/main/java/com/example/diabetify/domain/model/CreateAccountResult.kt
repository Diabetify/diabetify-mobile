package com.example.diabetify.domain.model

import com.example.diabetify.util.Resource

data class CreateAccountResult(
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val ageError: String? = null,
    val hipertensionError: String? = null,
    val cholesterolError: String? = null,
    val disturbedVisionError: String? = null,
    val weightError: String? = null,
    val heightError: String? = null,
    val verifyError: String? = null,
    val result:Resource<Unit>? = null
)