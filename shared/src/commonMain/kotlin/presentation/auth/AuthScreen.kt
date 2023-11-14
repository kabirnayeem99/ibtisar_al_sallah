package presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.UiEvent
import presentation.products.ProductsScreen

class AuthScreen : Screen {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val snackbarHostState = remember { SnackbarHostState() }
        val screenModel = getScreenModel<AuthViewModel>()
        val uiState = screenModel.state.collectAsState()
        val isLoading = uiState.value.isLoading
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(true) {
            screenModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.Warning -> snackbarHostState.showSnackbar(event.message)
                    is UiEvent.Error -> snackbarHostState.showSnackbar(event.message)
                }
            }
        }

        LaunchedEffect(true) {
            screenModel.loggedIn.collect { loggedId ->
                if (loggedId) navigator.push(ProductsScreen())
            }
        }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) {
            Box(modifier = Modifier.wrapContentHeight()) {
                LazyColumn(
                    modifier = Modifier.padding(40.dp).align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = uiState.value.email,
                            onValueChange = {
                                screenModel.onUsernameChanged(it)
                            },
                            label = { Text("Username") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                autoCorrect = true,
                                imeAction = ImeAction.Next
                            ),
                            enabled = !isLoading,
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = uiState.value.password,
                            label = { Text("Password") },
                            onValueChange = {
                                screenModel.onPasswordChanged(it)
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                            ),
                            enabled = !isLoading,
                        )
                    }
                    item {
                        AnimatedVisibility(visible = !isLoading) {
                            Button(onClick = {
                                keyboardController?.hide()
                                screenModel.onLoginClicked()
                            }) {
                                Text("Log In")
                            }
                        }
                        AnimatedVisibility(visible = isLoading) {
                            CircularProgressIndicator(
                                strokeWidth = 4.dp, modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}