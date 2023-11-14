package data.repository

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import data.service.ProductApiService
import domain.entity.ProductItem
import domain.repository.ProductRepository
import kotlinx.datetime.Clock

class ProductRepositoryImpl(private val productApiService: ProductApiService) : ProductRepository {
    override suspend fun getProducts(page: Int): Result<List<ProductItem>> {
        return try {
            val clock = Clock.System.now()
            val skip = page * 10
            val productDto = productApiService.getProducts(skip)
            val products = productDto.products.take(10).map { pd ->
                ProductItem(
                    thumbnail = pd.thumbnail,
                    price = pd.price,
                    rating = pd.rating,
                    id = "${pd.id}_${clock.toEpochMilliseconds()}",
                    title = pd.title.capitalize(Locale.current),
                    category = pd.category.capitalize(Locale.current),
                    brand = pd.brand.capitalize(Locale.current)
                )
            }
            if (products.isEmpty() && page == 1) {
                throw IllegalStateException("No products")
            } else if (products.isEmpty() && page > 1) {
                throw IllegalStateException("Come to the end of the page")
            }
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}