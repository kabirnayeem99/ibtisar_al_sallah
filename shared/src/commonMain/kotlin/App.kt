import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import presentation.auth.AuthScreen
import presentation.products.ProductsScreen

@Composable
fun App() {
    Navigator(screen = ProductsScreen(), onBackPressed = { currentScreen ->
        println("Navigator: Pop screen ${currentScreen.key}")
        true
    })
}

expect fun getPlatformName(): String