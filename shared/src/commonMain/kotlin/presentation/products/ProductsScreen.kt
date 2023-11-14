package presentation.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import core.UiEvent
import domain.entity.Product
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.flow.distinctUntilChanged

class ProductsScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ProductsViewModel>()
        val uiState = viewModel.state.collectAsState()
        val products = uiState.value.products
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(true) {
            viewModel.uiEvent.distinctUntilChanged().collect { event ->
                when (event) {
                    is UiEvent.Warning -> snackbarHostState.showSnackbar(event.message)
                    is UiEvent.Error -> snackbarHostState.showSnackbar(event.message)
                }
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) {
            AnimatedVisibility(visible = uiState.value.isLoading) {
                Box(modifier = Modifier.fillMaxSize().height(45.dp)) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(45.dp).align(
                            Alignment.Center
                        )
                    )
                }
            }
            LazyVerticalGrid(columns = GridCells.Adaptive(150.dp)) {

                items(products.size, key = { index -> products[index].id }) { index ->
                    val product = products[index]
                    if (product.id == products.lastOrNull()?.id) viewModel.fetchProducts()

                    ProductItem(product)
                }
            }
        }
    }


    @Composable
    private fun ProductItem(product: Product) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color(0xfffefefe),
            elevation = 0.05.dp,
            modifier = Modifier.padding(4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                KamelImage(
                    resource = asyncPainterResource(product.thumbnail),
                    contentDescription = product.title + " image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(90.dp)
                )
                Text(product.title, style = MaterialTheme.typography.h6)
                Text("Brand: ${product.brand}")
                Text("Price: \$${product.price}")
                Text("Category: ${product.category}")
            }
        }
    }
}

