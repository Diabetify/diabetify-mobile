package com.itb.diabetify.domain.model.auth

import com.itb.diabetify.util.Resource

data class CreateAccountResult(
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val dobError: String? = null,
    val genderError: String? = null,
    val result:Resource<Unit>? = null
)