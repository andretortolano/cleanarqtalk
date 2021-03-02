package com.andretortolano.sample_clean_arq.interactor

import com.andretortolano.sample_clean_arq.api.Api
import com.andretortolano.sample_clean_arq.api.ProductResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetProductListUseCase(private val api: Api) {

    suspend operator fun invoke(): List<ProductResponse> =
        withContext(Dispatchers.IO) {
            return@withContext api.getProductList()
        }
}