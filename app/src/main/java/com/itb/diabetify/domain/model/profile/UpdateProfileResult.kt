package com.itb.diabetify.domain.model.profile

import com.itb.diabetify.util.Resource

class UpdateProfileResult (
    val weightError: String? = null,
    val heightError: String? = null,
    val hypertensionError: String? = null,
    val macrosomicBabyError: String? = null,
    val cholesterolError: String? = null,
    val bloodlineError: String? = null,
    val result: Resource<Unit>? = null
)