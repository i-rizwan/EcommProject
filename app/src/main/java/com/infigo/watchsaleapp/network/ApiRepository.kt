package com.infigo.watchsaleapp.network

import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiCommunicator: ApiCommunicator) {

    suspend fun getCryptos() = apiCommunicator.getCryptos()

    suspend fun getCryptoDetails(symbol: String) = apiCommunicator.getCryptoDetails(symbol)

}