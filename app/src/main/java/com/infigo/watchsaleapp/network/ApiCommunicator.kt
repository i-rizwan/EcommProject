package com.infigo.watchsaleapp.network

import com.example.watchstoreapp.model.CartItem
import com.infigo.watchsaleapp.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCommunicator {

    @GET("/login")
    suspend fun getLoginDetail(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<User>

    @GET("/register")
    suspend fun getRegisterDetail(@Body user: User): Response<User>

    @GET("/cartItem")
    suspend fun getCartItemResponse(): Response<CartItem>

    @GET("/delCartItem")
    suspend fun delCartItemResponse(@Body cartItem: ArrayList<CartItem>): Response<List<CartItem>>


}