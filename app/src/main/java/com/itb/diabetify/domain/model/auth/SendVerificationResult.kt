package com.itb.diabetify.domain.model.auth

import com.itb.diabetify.util.Resource

data class SendVerificationResult(
    val emailError: String? = null,
    val result: Resource<Unit>? = null
)
