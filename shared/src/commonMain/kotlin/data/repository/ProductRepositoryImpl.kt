package data.repository

import data.service.ProductApiService
import domain.entity.ProductItem
import domain.repository.ProductRepository

class ProductRepositoryImpl(private val productApiService: ProductApiService) : ProductRepository {
    override suspend fun getProducts(): Result<List<ProductItem>> {
        return try {
            val productDto = productApiService.getProducts()
            val products = productDto.products.map { pd ->
                ProductItem(
                    pd.thumbnail, pd.price, pd.rating, pd.id, pd.title, pd.category, pd.brand
                )
            }
            if (products.isEmpty()) throw IllegalStateException("No products")
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}