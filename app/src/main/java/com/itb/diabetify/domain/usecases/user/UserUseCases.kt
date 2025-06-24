package com.itb.diabetify.domain.usecases.user

data class UserUseCases(
    val getUser: GetUserUseCase,
    val getUserRepository: GetUserRepositoryUseCase,
    val editUser: EditUserUseCase,
)