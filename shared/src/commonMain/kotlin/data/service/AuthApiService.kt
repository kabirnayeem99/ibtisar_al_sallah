package data.service

import data.dto.AuthUserDto
import data.dto.ProductsDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class AuthApiService {
    private val httpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                })
            }
        }
    }

    suspend fun logIn(username: String, password: String) =
        httpClient.get("https://dummyjson.com/auth/login").body<AuthUserDto>()

//    suspend fun getProducts() =
//        httpClient.get("https://dummyjson.com/products").body<ProductsDto>()

}