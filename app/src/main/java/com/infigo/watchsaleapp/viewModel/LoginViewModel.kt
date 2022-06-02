package com.infigo.watchsaleapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchstoreapp.model.CartItem
import com.example.watchstoreapp.model.CategoryItem
import com.infigo.watchsaleapp.model.ProductItem
import com.infigo.watchsaleapp.model.User
import com.infigo.watchsaleapp.network.ApiRepository
import com.infigo.watchsaleapp.network.NetworkHelper
import com.infigo.watchsaleapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {


    private val _loginResponse = MutableLiveData<Resource<User>>()
    val loginResponse: LiveData<Resource<User>> get() = _loginResponse


    private val _registerResponse = MutableLiveData<Resource<User>>()
    val registerResponse: LiveData<Resource<User>> get() = _registerResponse


    private val _cartListResponse = MutableLiveData<Resource<CartItem>>()
    val cartList: LiveData<Resource<CartItem>> get() = _cartListResponse

    private val _delCartItemResponse = MutableLiveData<Resource<List<CartItem>>>()
    val delCartItemResponse: LiveData<Resource<List<CartItem>>> get() = _delCartItemResponse


    private val _allCategory = MutableLiveData<Resource<List<CategoryItem>>>()
    val allCategory: LiveData<Resource<List<CategoryItem>>> get() = _allCategory
    private val _productListResponse = MutableLiveData<Resource<List<ProductItem>>>()

    private val _productItemByCategory = MutableLiveData<Resource<List<CategoryItem>>>()
    val productItemByCategory: LiveData<Resource<List<CategoryItem>>> get() = _productItemByCategory

    private val _updateFavProductResponse = MutableLiveData<Resource<ProductItem>>()

    val updateFavProductResponse: LiveData<Resource<ProductItem>> get() = _updateFavProductResponse

    private val _getAddToCartResponse = MutableLiveData<Resource<List<ProductItem>>>()
    val getAddToCartResponse: LiveData<Resource<List<ProductItem>>> get() = _getAddToCartResponse

    fun addToCart(productItem: ProductItem) {

        if (networkHelper.isNetworkConnected()) {

            viewModelScope.launch {
                repository.addToCart(productItem).let {
                    if (it.isSuccessful)
                        _getAddToCartResponse.postValue(Resource.success(it.body()))
                    else
                        _getAddToCartResponse.postValue(Resource.error(it.message(), null))
                }
            }
        } else
            _getAddToCartResponse.postValue(Resource.error("No internet", null))
    }

    fun getProductByCategory(byCategory: String) {

        if (networkHelper.isNetworkConnected()) {
            viewModelScope.launch {
                repository.getProductByCategory(byCategory).let {
                    if (it.isSuccessful)
                        _productItemByCategory.postValue(Resource.success(it.body()))
                    else
                        _productItemByCategory.postValue(Resource.error("Error", null))
                }
            }
        } else
            _productItemByCategory.postValue(Resource.error("No Internet", null))

    }


    fun updateFavProduct(favProduct: ProductItem) {

        viewModelScope.launch {

            if (networkHelper.isNetworkConnected()) {
                repository.updateFavProduct(favProduct).let {
                    if (it.isSuccessful)
                        _updateFavProductResponse.postValue(Resource.success(it.body()))
                    else
                        _updateFavProductResponse.postValue(Resource.error("error", null))
                }

            } else
                _updateFavProductResponse.postValue(Resource.error("No internet", null))
        }
    }


    fun getAllCategories() {
        viewModelScope.launch {

            if (networkHelper.isNetworkConnected()) {
                repository.getAllCategories().let {
                    if (it.isSuccessful) {
                        _allCategory.postValue(Resource.success(it.body()))
                    } else
                        _allCategory.postValue(Resource.error("No Data", null))
                }

            } else
                _allCategory.postValue(Resource.error("No Internet", null))
        }

    }

    fun deleteAllCartItems(cartList: ArrayList<CartItem>) {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.delCartItemResponse(cartList).let {
                    if (it.isSuccessful)
                        _delCartItemResponse.postValue(Resource.success(it.body()))
                    else
                        _delCartItemResponse.postValue(
                            Resource.error(it.errorBody().toString(), null)
                        )
                }
            } else
                _delCartItemResponse.postValue(Resource.error("No internet connection", null))

        }

    }


    public fun requestForLoginResponse(email: String, password: String) {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getLoginDetail(email, password).let {
                    if (it.isSuccessful)
                        _loginResponse.postValue(Resource.success(it.body()))
                    else
                        _loginResponse.postValue(Resource.error(it.errorBody().toString(), null))
                }

            } else
                _loginResponse.postValue(Resource.error("No internet connection", null))
        }
    }


    public fun requestForRegisterResponse(user: User) {

        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getRegisterDetailResponse(user).let {
                    if (it.isSuccessful)
                        _registerResponse.postValue(Resource.success(it.body()))
                    else
                        _registerResponse.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else
                _registerResponse.postValue(Resource.error("No internet connection", null))
        }
    }

    public fun requestForCartList() {

        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                repository.getCartItemResponse().let {
                    if (it.isSuccessful)
                        _cartListResponse.postValue(Resource.success(it.body()))
                    else
                        _cartListResponse.postValue(Resource.error(it.message(), null))
                }
            } else
                _cartListResponse.postValue(Resource.error("no Internet connection", null))
        }

    }
}