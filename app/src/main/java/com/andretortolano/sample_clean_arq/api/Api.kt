package com.andretortolano.sample_clean_arq.api

interface Api {

    /**
     * Exceptions:
     *   HttpException 404 - NotFound
     *   HttpException 500 - InternalServerError
     *   NetworkException - NoInternet
     */
    fun getUser(body: GetUserBody): List<UserResponse>

    /**
     * Exceptions:
     *   HttpException 500 - InternalServerError
     *   NetworkException - NoInternet
     */
    fun getProductList(): List<ProductResponse>
}