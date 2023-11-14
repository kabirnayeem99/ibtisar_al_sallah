package domain.repository

import domain.entity.ProductItem

interface ProductRepository {
    suspend fun getProducts(): Result<List<ProductItem>>
}