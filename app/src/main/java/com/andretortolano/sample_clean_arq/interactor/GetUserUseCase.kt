package com.andretortolano.sample_clean_arq.interactor

import com.andretortolano.sample_clean_arq.api.Api
import com.andretortolano.sample_clean_arq.api.GetUserBody
import com.andretortolano.sample_clean_arq.api.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserUseCase(private val api: Api) {

    suspend operator fun invoke(userBody: GetUserBody): UserResponse =
        withContext(Dispatchers.IO) {
            return@withContext api.getUser(userBody)
        }
}