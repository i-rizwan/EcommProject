package com.infigo.watchsaleapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchstoreapp.model.CartItem
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