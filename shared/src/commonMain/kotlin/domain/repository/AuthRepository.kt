package domain.repository

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<String>
    suspend fun logout(): Result<String>
}