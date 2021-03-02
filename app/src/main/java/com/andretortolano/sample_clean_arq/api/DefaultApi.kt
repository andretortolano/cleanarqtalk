package com.andretortolano.sample_clean_arq.api

class DefaultApi: Api {
    override fun getUser(body: GetUserBody): UserResponse {
        return when(body.id) {
            1 -> UserResponse(1, "Pedro", 20, true)
            else -> UserResponse(2, "Joao", 22, false)
        }
    }

    override fun getProductList(): List<ProductResponse> {
        return arrayListOf(
            ProductResponse(1, "Pastel", "savory", 5.5),
            ProductResponse(2, "Bis", "candy", 3.0),
            ProductResponse(3, "KitKat", "candy", 10.1),
            ProductResponse(4, "Kibe", "savory", 2.72),
            ProductResponse(5, "Sonho de Valsa", "candy", 1.50)
        )
    }
}