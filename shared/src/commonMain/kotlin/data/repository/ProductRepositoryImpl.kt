package data.repository

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import co.touchlab.kermit.Logger
import data.dto.ProductsDto
import data.service.ProductApiService
import domain.entity.Product
import domain.repository.ProductRepository
import kotlinx.datetime.Clock

class ProductRepositoryImpl(private val productApiService: ProductApiService) : ProductRepository {
    override suspend fun getProducts(page: Int): Result<List<Product>> {
        return try {
            val skip = (page - 1) * 10
            val productDto = productApiService.getProducts(skip)
            val products = mapDtoToProductItem(productDto)
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

    private fun mapDtoToProductItem(productDto: ProductsDto): List<Product> {
        val clock = Clock.System.now()
        return try {
            productDto.products.map { pd ->
                val id = "${pd.id}_${clock.toEpochMilliseconds()}"
                Product(
                    thumbnail = pd.thumbnail,
                    price = pd.price,
                    rating = pd.rating,
                    id = id,
                    title = pd.title.capitalize(Locale.current),
                    category = pd.category.capitalize(Locale.current),
                    brand = pd.brand.capitalize(Locale.current)
                )
            }
        } catch (e: Exception) {
            Logger.e(TAG, e)
            return emptyList()
        }
    }
}

private const val TAG = "ProductRepositoryImpl"