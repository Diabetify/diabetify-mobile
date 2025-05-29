package com.itb.diabetify.domain.model.auth

import com.itb.diabetify.util.Resource

data class ChangePasswordResult(
    val emailError: String? = null,
    val newPasswordError: String? = null,
    val codeError: String? = null,
    val result: Resource<Unit>? = null
)