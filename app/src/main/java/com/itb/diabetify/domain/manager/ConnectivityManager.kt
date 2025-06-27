package com.itb.diabetify.domain.manager

import kotlinx.coroutines.flow.Flow

interface ConnectivityManager {
    fun isConnected(): Boolean
    fun observeConnectivity(): Flow<Boolean>
} 