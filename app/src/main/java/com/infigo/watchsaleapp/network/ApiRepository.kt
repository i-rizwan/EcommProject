package com.infigo.watchsaleapp.network

import com.example.watchstoreapp.model.CartItem
import com.infigo.watchsaleapp.model.ProductItem
import com.infigo.watchsaleapp.model.User
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiCommunicator: ApiCommunicator) {

    suspend fun getLoginDetail(email: String, password: String) =
        apiCommunicator.getLoginDetail(email, password)


    suspend fun getRegisterDetailResponse(user: User) = apiCommunicator.getRegisterDetail(user)

    suspend fun getCartItemResponse() = apiCommunicator.getCartItemResponse()

    suspend fun delCartItemResponse(cartList: ArrayList<CartItem>) =
        apiCommunicator.delCartItemResponse(cartList)

    suspend fun getAllCategories() = apiCommunicator.getAllCategories()

    suspend fun getProductByCategory(categoryId: String) =
        apiCommunicator.getProductByCategory(categoryId)


    suspend fun updateFavProduct(productItem: ProductItem) =
        apiCommunicator.updateFavProduct(productItem)

 suspend fun addToCart(productItem: ProductItem) =
        apiCommunicator.addToCart(productItem)


}