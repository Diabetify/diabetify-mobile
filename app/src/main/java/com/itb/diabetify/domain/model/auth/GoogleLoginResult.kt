package com.itb.diabetify.domain.model.auth

import com.itb.diabetify.util.Resource

data class GoogleLoginResult(
    val tokenError: String? = null,
    val result: Resource<Unit>? = null
)