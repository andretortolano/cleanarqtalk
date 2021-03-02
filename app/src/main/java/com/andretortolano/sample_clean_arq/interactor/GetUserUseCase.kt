package com.andretortolano.sample_clean_arq.interactor

import com.andretortolano.sample_clean_arq.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetUserUseCase(private val api: Api) {

    data class Request(val userId: Int)

    sealed class Result {
        data class Success(val userEntity: UserEntity) : Result()
        object NoInternetError : Result()
        object NotFoundError : Result()
        object Error : Result()
    }

    suspend operator fun invoke(request: Request): Result = withContext(Dispatchers.IO) {
        try {
            val response = api.getUser(GetUserBody(request.userId))
            val userEntity = response.toUserEntity()
            Result.Success(userEntity)
        } catch (httpException: HttpException) {
            if (httpException.statusCode == 404) {
                Result.NotFoundError
            } else {
                Result.Error
            }
        } catch (networkException: NetworkException) {
            Result.NoInternetError
        }
    }

    private fun UserResponse.toUserEntity(): UserEntity {
        return UserEntity(this.name, this.age, this.eatsCandy)
    }
}