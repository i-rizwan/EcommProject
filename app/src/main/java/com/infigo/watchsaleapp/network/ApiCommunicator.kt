package com.infigo.watchsaleapp.network

import com.infigo.watchsaleapp.model.CartItem
import com.infigo.watchsaleapp.model.CategoryItem
import com.infigo.watchsaleapp.model.ProductItem
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

    @GET("/allCategory")
    suspend fun getAllCategories(): Response<ArrayList<CategoryItem>>

    @GET("/byCategory")
    suspend fun getProductByCategory(@Query("pid") byId: String): Response<List<CategoryItem>>


    @GET("/updateProduct")
    suspend fun updateFavProduct(@Body productItem: ProductItem): Response<ProductItem>

    @GET("/addToCart")
    suspend fun addToCart(@Body productItem: ProductItem): Response<List<ProductItem>>
}