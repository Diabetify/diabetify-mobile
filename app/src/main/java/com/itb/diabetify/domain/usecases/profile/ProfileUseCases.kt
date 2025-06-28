package com.itb.diabetify.domain.usecases.profile

data class ProfileUseCases(
    val addProfile: AddProfileUseCase,
    val getProfile: GetProfileUseCase,
    val getProfileRepository: GetProfileRepositoryUseCase,
    val updateProfile: UpdateProfileUseCase
)