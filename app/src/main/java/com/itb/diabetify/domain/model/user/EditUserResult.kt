package com.itb.diabetify.domain.model.user

import com.itb.diabetify.util.Resource

class EditUserResult (
    val nameError: String? = null,
    val emailError: String? = null,
    val genderError: String? = null,
    val dobError: String? = null,
    val result: Resource<Unit>? = null
)