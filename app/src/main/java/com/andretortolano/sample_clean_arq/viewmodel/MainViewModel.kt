package com.andretortolano.sample_clean_arq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andretortolano.sample_clean_arq.api.*
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
): ViewModel() {

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

    private var userEntity: UserEntity? = null

    fun fetchUser(extraId: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            _userState.value = UserViewState.Loading

            try {
                val body = GetUserBody(extraId)
                val result = getUserUseCase.invoke(body)

                userEntity = result.toUserEntity().also {
                    _userState.value = UserViewState.Success(it)
                }
            } catch (httpException: HttpException) {
                if(httpException.statusCode == 404) {
                    _userState.value = UserViewState.NotFoundError
                } else {
                    _userState.value = UserViewState.OtherError
                }
            } catch (networkException: NetworkException) {
                _userState.value = UserViewState.NoInternetError
            }
        }
    }

    fun fetchProducts() {
        GlobalScope.launch(Dispatchers.Main) {
            _productListState.value = ProductListViewState.Loading
            try {
                val result = getProductListUseCase.invoke()
                val productEntityList = result.toProductEntityList()
                val sortedList = sortCandyFirstIfUserEatsCandy(productEntityList)
                _productListState.value = ProductListViewState.Success(sortedList)
            } catch (httpException: HttpException) {
                _productListState.value = ProductListViewState.OtherError
            } catch (networkException: NetworkException) {
                _productListState.value = ProductListViewState.NoInternetError
            }
        }
    }

    private fun sortCandyFirstIfUserEatsCandy(productEntityList: List<ProductEntity>): List<ProductEntity> {
        return if(userEntity != null && userEntity!!.eatsCandy) {
            productEntityList.sortedByDescending { it.type == ProductEntity.ProductType.CANDY }
        } else {
            productEntityList
        }
    }

    private fun UserResponse.toUserEntity(): UserEntity {
        return UserEntity(this.name, this.age, this.eatsCandy)
    }

    private fun List<ProductResponse>.toProductEntityList(): List<ProductEntity> {
        return map { ProductEntity(it.name, it.type.toProductType(), it.price) }
    }

    private fun String.toProductType(): ProductEntity.ProductType {
        return if(this == "candy") {
            ProductEntity.ProductType.CANDY
        } else {
            ProductEntity.ProductType.SAVORY
        }
    }
}
