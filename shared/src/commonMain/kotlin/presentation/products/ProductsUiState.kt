package presentation.products

import domain.entity.Product

data class ProductsUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList()
)