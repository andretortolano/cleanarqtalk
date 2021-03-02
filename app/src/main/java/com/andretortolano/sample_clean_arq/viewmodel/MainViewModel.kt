package com.andretortolano.sample_clean_arq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andretortolano.sample_clean_arq.interactor.GetProductListUseCase
import com.andretortolano.sample_clean_arq.interactor.GetUserUseCase
import com.andretortolano.sample_clean_arq.interactor.ProductEntity
import com.andretortolano.sample_clean_arq.interactor.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val getProductListUseCase: GetProductListUseCase
) : ViewModel() {

    sealed class UserViewState {
        data class Success(val userEntity: UserEntity) : UserViewState()
        object NoInternetError : UserViewState()
        object NotFoundError : UserViewState()
        object OtherError : UserViewState()
        object Loading : UserViewState()
    }

    private val _userState: MutableLiveData<UserViewState> = MutableLiveData()

    val userState: LiveData<UserViewState> = _userState

    sealed class ProductListViewState {
        data class Success(val productList: List<ProductEntity>) : ProductListViewState()
        object NoInternetError : ProductListViewState()
        object OtherError : ProductListViewState()
        object Loading : ProductListViewState()
    }

    private val _productListState: MutableLiveData<ProductListViewState> = MutableLiveData()

    val productListState: LiveData<ProductListViewState> = _productListState

    fun onCreate(userId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            _userState.value = UserViewState.Loading
            _productListState.value = ProductListViewState.Loading

            handleGetUserUseCase(getUserUseCase(GetUserUseCase.Request(userId)))
        }
    }

    private suspend fun handleGetUserUseCase(result: GetUserUseCase.Result) {
        when (result) {
            GetUserUseCase.Result.Error -> {
                _userState.value = UserViewState.OtherError
                _productListState.value = ProductListViewState.OtherError
            }
            GetUserUseCase.Result.NoInternetError -> {
                _userState.value = UserViewState.NoInternetError
                _productListState.value = ProductListViewState.NoInternetError
            }
            GetUserUseCase.Result.NotFoundError -> {
                _userState.value = UserViewState.NotFoundError
                _productListState.value = ProductListViewState.OtherError
            }
            is GetUserUseCase.Result.Success -> {
                handleGetProductListUseCase(getProductListUseCase(GetProductListUseCase.Request(result.userEntity.eatsCandy)))
            }
        }
    }

    private fun handleGetProductListUseCase(result: GetProductListUseCase.Result) {
        when (result) {
            GetProductListUseCase.Result.Error -> {
                _productListState.value = ProductListViewState.OtherError
            }
            GetProductListUseCase.Result.NoInternetError -> {
                _productListState.value = ProductListViewState.NoInternetError
            }
            is GetProductListUseCase.Result.Success -> {
                _productListState.value = ProductListViewState.Success(result.productList)
            }
        }
    }
}
