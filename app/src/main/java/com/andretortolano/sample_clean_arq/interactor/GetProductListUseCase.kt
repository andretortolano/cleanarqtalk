package com.andretortolano.sample_clean_arq.interactor

import com.andretortolano.sample_clean_arq.api.Api
import com.andretortolano.sample_clean_arq.api.HttpException
import com.andretortolano.sample_clean_arq.api.NetworkException
import com.andretortolano.sample_clean_arq.api.ProductResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetProductListUseCase(private val api: Api) {

    data class Request(val candySort: Boolean)

    sealed class Result {
        data class Success(val productList: List<ProductEntity>) : Result()
        object NoInternetError : Result()
        object Error : Result()
    }

    suspend operator fun invoke(request: Request): Result = withContext(Dispatchers.IO) {
        try {
            val response = api.getProductList()
            val productEntityList = response.toProductEntityList()
            val data = if (request.candySort) {
                productEntityList.sortedByDescending { it.type == ProductEntity.ProductType.CANDY }
            } else {
                productEntityList
            }
            Result.Success(data)
        } catch (httpException: HttpException) {
            Result.Error
        } catch (networkException: NetworkException) {
            Result.NoInternetError
        }
    }

    private fun List<ProductResponse>.toProductEntityList(): List<ProductEntity> {
        return map { ProductEntity(it.name, it.type.toProductType(), it.price) }
    }

    private fun String.toProductType(): ProductEntity.ProductType {
        return if (this == "candy") {
            ProductEntity.ProductType.CANDY
        } else {
            ProductEntity.ProductType.SAVORY
        }
    }
}
