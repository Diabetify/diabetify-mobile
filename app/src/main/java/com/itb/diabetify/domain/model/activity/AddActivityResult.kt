package com.itb.diabetify.domain.model.activity

import com.itb.diabetify.util.Resource

class AddActivityResult (
    val activityDateError: String? = null,
    val activityTypeError: String? = null,
    val valueError: String? = null,
    val result: Resource<Unit>? = null
)