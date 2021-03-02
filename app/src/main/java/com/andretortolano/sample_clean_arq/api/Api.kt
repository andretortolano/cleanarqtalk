package com.andretortolano.sample_clean_arq.api

import kotlin.Throws

interface Api {

    /**
     * Exceptions:
     *   HttpException 404 - NotFound
     *   HttpException 500 - InternalServerError
     *   NetworkException - NoInternet
     */
    @Throws(HttpException::class, NetworkException::class)
    fun getUser(body: GetUserBody): UserResponse

    /**
     * Exceptions:
     *   HttpException 500 - InternalServerError
     *   NetworkException - NoInternet
     */
    @Throws(HttpException::class, NetworkException::class)
    fun getProductList(): List<ProductResponse>
}