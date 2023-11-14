package presentation.products

import domain.entity.ProductItem

data class ProductsUiState(
    val isLoading: Boolean = false,
    val products: List<ProductItem> = emptyList()
)