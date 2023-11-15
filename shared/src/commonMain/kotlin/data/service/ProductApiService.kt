package data.service

import data.dto.ProductsDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

class ProductApiService(private val client: HttpClient) {
    suspend fun getProducts(skip: Int = 0): ProductsDto {
        val req = client.get("https://dummyjson.com/products?limit=10&skip=$skip")
        if (req.status == HttpStatusCode.OK) return req.body()
        throw Exception(req.status.description)
    }

    suspend fun getCategories(): List<String> {
        val req = client.get("https://dummyjson.com/products/categories")
        if (req.status == HttpStatusCode.OK) return req.body()
        throw Exception(req.status.description)
    }
}