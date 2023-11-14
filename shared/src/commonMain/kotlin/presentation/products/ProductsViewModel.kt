package presentation.products

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.UiEvent
import domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsViewModel(private val productRepository: ProductRepository) :
    StateScreenModel<ProductsUiState>(ProductsUiState()) {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        fetchProducts()
    }

    private var page = 1
    fun fetchProducts() {
        screenModelScope.launch(Dispatchers.IO) {
            toggleLoading(page == 1)
            if (page > 6) {
                showUserMessage("No more products. Come to an end.")
                return@launch
            }
            val productResult = productRepository.getProducts(page++)
            productResult.fold(
                onSuccess = { products ->
                    mutableState.update {
                        val currentProducts = it.products.toMutableList()
                        currentProducts.addAll(products)
                        it.copy(products = currentProducts)
                    }
                },
                onFailure = { e -> showUserMessage(e.message) },
            )
            toggleLoading(false)
        }
    }

    private fun showUserMessage(message: String?, isError: Boolean = false) {
        screenModelScope.launch {
            if (message.isNullOrBlank()) return@launch
            if (isError) _uiEvent.emit(UiEvent.Error(message))
            else _uiEvent.emit(UiEvent.Warning(message))
        }
    }

    private fun toggleLoading(loading: Boolean) {
        mutableState.update { it.copy(isLoading = loading) }
    }
}