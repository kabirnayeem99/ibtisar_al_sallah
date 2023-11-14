package domain.repository

import domain.entity.ProductItem

interface ProductRepository {
    suspend fun getProducts(page: Int =1): Result<List<ProductItem>>
}