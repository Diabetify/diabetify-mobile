package com.itb.diabetify.domain.usecases.auth

data class AuthUseCases(
    val changePassword: ChangePasswordUseCase,
    val createAccount: CreateAccountUseCase,
    val login: LoginUseCase,
    val logout: LogoutUseCase,
    val sendVerification: SendVerificationUseCase,
    val verifyOtp: VerifyOtpUseCase,
)