package com.itb.diabetify.e2e.repository

import com.itb.diabetify.data.remote.auth.request.ChangePasswordRequest
import com.itb.diabetify.data.remote.auth.request.CreateAccountRequest
import com.itb.diabetify.data.remote.auth.request.LoginRequest
import com.itb.diabetify.data.remote.auth.request.SendVerificationRequest
import com.itb.diabetify.data.remote.auth.request.VerifyOtpRequest
import com.itb.diabetify.domain.repository.AuthRepository
import com.itb.diabetify.util.Resource
import javax.inject.Inject

class FakeAuthRepository @Inject constructor() : AuthRepository {

    var shouldFailCreateAccount = false
    var shouldFailSendVerification = false
    var shouldFailVerifyOtp = false
    var shouldFailLogin = false
    var shouldFailChangePassword = false
    var createAccountErrorType = "duplicate_email"
    var sendVerificationErrorType = "email_not_found"
    var verifyOtpErrorType = "invalid_otp"
    var loginErrorType = "wrong_password"
    var changePasswordErrorType = "network_error"

    override suspend fun createAccount(
        createAccountRequest: CreateAccountRequest
    ): Resource<Unit> {
        if (shouldFailCreateAccount) {
            return when (createAccountErrorType) {
                "duplicate_email" -> Resource.Error("Akun dengan email ini sudah terdaftar")
                "network_error" -> Resource.Error("Network error occurred")
                else -> Resource.Error("Unknown error")
            }
        }
        return Resource.Success(Unit)
    }

    override suspend fun sendVerification(
        sendVerificationRequest: SendVerificationRequest,
        type: String
    ): Resource<Unit> {
        if (shouldFailSendVerification) {
            return when (sendVerificationErrorType) {
                "email_not_found" -> Resource.Error("Email tidak terdaftar")
                "network_error" -> Resource.Error("Network error occurred")
                else -> Resource.Error("Unknown error")
            }
        }

        return when (type) {
            "register" -> Resource.Success(Unit)
            "reset-password" -> Resource.Success(Unit)
            else -> Resource.Error("Invalid type")
        }
    }

    override suspend fun verifyOtp(
        verifyOtpRequest: VerifyOtpRequest
    ): Resource<Unit> {
        if (shouldFailVerifyOtp) {
            return when (verifyOtpErrorType) {
                "invalid_otp" -> Resource.Error("Kode OTP salah atau sudah kadaluarsa")
                "expired_otp" -> Resource.Error("Kode OTP sudah kadaluarsa")
                "network_error" -> Resource.Error("Network error occurred")
                else -> Resource.Error("Unknown error")
            }
        }

        return Resource.Success(Unit)
    }

    override suspend fun login(loginRequest: LoginRequest): Resource<Unit> {
        if (shouldFailLogin) {
            return when (loginErrorType) {
                "wrong_password" -> Resource.Error("Password salah")
                "account_not_found" -> Resource.Error("Akun tidak ditemukan")
                "network_error" -> Resource.Error("Network error occurred")
                else -> Resource.Error("Unknown error")
            }
        }

        return Resource.Success(Unit)
    }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Resource<Unit> {
        if (shouldFailChangePassword) {
            return when (changePasswordErrorType) {
                "invalid_otp" -> Resource.Error("Kode OTP salah atau sudah kadaluarsa")
                "weak_password" -> Resource.Error("Kata sandi harus lebih dari 8 karakter")
                "network_error" -> Resource.Error("Network error occurred")
                else -> Resource.Error("Unknown error")
            }
        }
        return Resource.Success(Unit)
    }

    override suspend fun logout(): Resource<Unit> {
        return Resource.Success(Unit)
    }

    fun reset() {
        shouldFailCreateAccount = false
        shouldFailSendVerification = false
        shouldFailVerifyOtp = false
        shouldFailLogin = false
        shouldFailChangePassword = false
        createAccountErrorType = "duplicate_email"
        sendVerificationErrorType = "email_not_found"
        verifyOtpErrorType = "invalid_otp"
        loginErrorType = "wrong_password"
        changePasswordErrorType = "network_error"
    }
}