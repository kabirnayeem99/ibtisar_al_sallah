package presentation.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import core.UiEvent
import domain.entity.Product
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.flow.distinctUntilChanged
import presentation.common.SallahColor
import presentation.common.uiitems.ProgressIndicator

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
            topBar = { ProductScreenTopBar() },
            backgroundColor = SallahColor.WhiteWhisper,
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                ProgressIndicator(
                    uiState.value.isLoading, modifier = Modifier.align(Alignment.Center)
                )
                Column {
                    HeaderText()
                    SearchButton()
                    ProductListLabel()
                    ProductItemList(products) { viewModel.fetchProducts() }
                }
            }
        }
    }

    @Composable
    private fun ProductListLabel() {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth().padding(bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "New Arrival", style = MaterialTheme.typography.h6.copy(
                    color = Color.Black,
                    fontSize = 22.sp,
                )
            )
            Text(
                "See All", style = MaterialTheme.typography.h6.copy(
                    color = Color.Black.copy(alpha = 0.5F),
                    fontSize = 16.sp,
                )
            )
        }
    }

    @Composable
    private fun SearchButton() {
        Box(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp).fillMaxWidth()
                .height(60.dp).background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = SallahColor.GreyWoodBark.copy(0.5F),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Search what you are looking for...",
                    style = MaterialTheme.typography.body1.copy(
                        color = SallahColor.GreyWoodBark.copy(0.5F),
                        fontSize = 14.sp,
                    )
                )
                Spacer(Modifier.weight(1.0F))
                Box(
                    modifier = Modifier.height(45.dp).width(48.dp).background(
                        SallahColor.OrangeJaffa.copy(alpha = 0.1F),
                        shape = RoundedCornerShape(10.dp)
                    )
                ) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp).align(Alignment.Center),
                        tint = SallahColor.PinkLipstick,
                    )
                }
            }
        }
    }

    @Composable
    private fun HeaderText() {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
            Text(
                "New Arriving", style = MaterialTheme.typography.h6.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Buy as much as you want, man!!!", style = MaterialTheme.typography.h6.copy(
                    fontSize = 14.sp, color = Color.Black.copy(alpha = 0.3F)
                )
            )
        }
    }

    @Composable
    private fun ProductItemList(products: List<Product>, onFetchMore: () -> Unit) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(154.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            items(products.size, key = { index -> products[index].id }) { index ->
                val product = products[index]
                if (product.id == products.lastOrNull()?.id) onFetchMore()

                ProductItem(product)
            }
        }
    }

    @Composable
    private fun ProductScreenTopBar() {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp).fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Rounded.Menu, "", modifier = Modifier.size(27.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn, "", modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Dhaka, Bangladesh", style = MaterialTheme.typography.body1.copy(
                        fontSize = 14.sp
                    )
                )
            }
            Box(
                modifier = Modifier.background(
                    SallahColor.OrangeJaffa.copy(alpha = 0.1F), shape = CircleShape
                ).size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    "",
                    tint = SallahColor.PinkLipstick,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }


    @Composable
    private fun ProductItem(product: Product) {
        Card(
            shape = RoundedCornerShape(12.dp),
            backgroundColor = SallahColor.WhiteSoapstone,
            elevation = 0.05.dp,
            modifier = Modifier.height(190.dp).width(154.dp),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                KamelImage(
                    resource = asyncPainterResource(product.thumbnail),
                    contentDescription = product.title + " image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(104.dp).height(129.dp)
                        .clip(RoundedCornerShape(10.dp)),
                )
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        product.title,
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        modifier = Modifier.width(94.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        product.price, style = MaterialTheme.typography.body1.copy(
                            fontSize = 10.sp,
                        )
                    )
                }
            }
        }
    }
}

