package domain.repository

import domain.entity.Category
import domain.entity.Product

interface ProductRepository {
    suspend fun getProducts(page: Int = 1): Result<List<Product>>
    suspend fun getCategories(amount: Int): Result<List<Category>>
}