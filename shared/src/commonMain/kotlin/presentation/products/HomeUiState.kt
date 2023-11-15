package presentation.products

import domain.entity.Category
import domain.entity.Product

data class HomeUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
)