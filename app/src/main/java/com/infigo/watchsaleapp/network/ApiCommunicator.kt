package com.infigo.watchsaleapp.network

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCommunicator {

    @GET("tickers/24hr")
    suspend fun getCryptos(): Response<JsonObject>

    @GET("ticker/24hr")
    suspend fun getCryptoDetails(@Query("symbol") symbol: String): Response<JsonObject>





}