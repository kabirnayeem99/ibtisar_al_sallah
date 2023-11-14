package data.service

import data.dto.AuthRequestDto
import data.dto.AuthUserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

class AuthApiService(private val client: HttpClient) {


    suspend fun logIn(username: String, password: String): AuthUserDto {
        val requestDto = AuthRequestDto(username, password)
        val req = client.post("https://dummyjson.com/auth/login") {
            headers {
                this["Content-Type"] = "application/json"
            }
            setBody(requestDto)
        }
        if (req.status == HttpStatusCode.OK) return req.body()
        throw Exception(req.status.description)
    }
}