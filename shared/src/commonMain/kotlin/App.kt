import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import presentation.auth.AuthScreen

@Composable
fun App() {
    Navigator(screen = AuthScreen(), onBackPressed = { currentScreen ->
        println("Navigator: Pop screen ${currentScreen.key}")
        true
    })
}

expect fun getPlatformName(): String