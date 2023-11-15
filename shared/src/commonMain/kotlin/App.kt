import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import presentation.products.HomeScreen

@Composable
fun App() {
    Navigator(screen = HomeScreen(), onBackPressed = { currentScreen ->
        println("Navigator: Pop screen ${currentScreen.key}")
        true
    })
}

expect fun getPlatformName(): String