package com.itb.diabetify.domain.usecases.connectivity

data class ConnectivityUseCases(
    val observeConnectivity: ObserveConnectivityUseCase,
    val checkConnectivity: CheckConnectivityUseCase
) 