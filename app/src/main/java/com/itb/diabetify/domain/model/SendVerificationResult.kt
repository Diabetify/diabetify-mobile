package com.itb.diabetify.domain.model

import com.itb.diabetify.util.Resource

data class SendVerificationResult(
    val emailError: String? = null,
    val result: Resource<Unit>? = null
)
