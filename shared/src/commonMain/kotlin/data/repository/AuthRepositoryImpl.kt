package data.repository

import co.touchlab.kermit.Logger
import data.service.AuthApiService
import domain.repository.AuthRepository

class AuthRepositoryImpl(private val authApiService: AuthApiService) : AuthRepository {
    override suspend fun login(username: String, password: String): Result<String> {
        return try {
            val authUserDto = authApiService.logIn(username, password)
            Logger.d { authUserDto.toString() }
            val token = authUserDto.token
            if (token.isNullOrBlank()) throw IllegalStateException("Authentication token")
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<String> {
        TODO("Not yet implemented")
    }
}