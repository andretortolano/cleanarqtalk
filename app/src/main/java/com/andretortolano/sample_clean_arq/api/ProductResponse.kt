package com.andretortolano.sample_clean_arq.api

data class ProductResponse(
    val id: Int,
    val name: String,
    val type: String, // candy, savory
    val price: Double
)
