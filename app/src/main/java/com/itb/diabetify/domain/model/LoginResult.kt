package com.itb.diabetify.domain.model

import com.itb.diabetify.util.Resource

class LoginResult(
    val emailError: String? = null,
    val passwordError: String? = null,
    val result: Resource<Unit>? = null
)