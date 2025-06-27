package com.itb.diabetify.domain.usecases.connectivity

import com.itb.diabetify.domain.manager.ConnectivityManager
import javax.inject.Inject

class CheckConnectivityUseCase @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {
    operator fun invoke(): Boolean = connectivityManager.isConnected()
} 