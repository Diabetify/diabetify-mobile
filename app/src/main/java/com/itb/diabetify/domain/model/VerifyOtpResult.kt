package com.itb.diabetify.domain.model

import com.itb.diabetify.util.Resource

class VerifyOtpResult (
    val emailError: String? = null,
    val codeError: String? = null,
    val result: Resource<Unit>? = null
)