package com.andretortolano.sample_clean_arq.interactor

data class ProductEntity(
    val name: String,
    val type: ProductType,
    val price: Double
) {
    enum class ProductType {
        CANDY, SAVORY
    }
}
