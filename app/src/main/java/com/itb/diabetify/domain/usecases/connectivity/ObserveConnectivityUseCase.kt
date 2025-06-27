package com.itb.diabetify.domain.usecases.connectivity

import com.itb.diabetify.domain.manager.ConnectivityManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveConnectivityUseCase @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {
    operator fun invoke(): Flow<Boolean> = connectivityManager.observeConnectivity()
} 