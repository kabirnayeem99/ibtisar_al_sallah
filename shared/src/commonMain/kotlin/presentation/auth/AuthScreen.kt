package presentation.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import core.UiEvent
import presentation.common.SallahColor
import presentation.common.uiitems.LogoImage
import presentation.common.uiitems.PrimaryActionButton
import presentation.common.uiitems.ProgressIndicator
import presentation.products.ProductsScreen

class AuthScreen : Screen {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val snackbarHostState = remember { SnackbarHostState() }
        val viewModel = getScreenModel<AuthViewModel>()
        val uiState = viewModel.state.collectAsState()
        val isLoading = uiState.value.isLoading
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(true) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is UiEvent.Warning -> snackbarHostState.showSnackbar(event.message)
                    is UiEvent.Error -> snackbarHostState.showSnackbar(event.message)
                }
            }
        }

        LaunchedEffect(true) {
            viewModel.loggedIn.collect { loggedId ->
                if (loggedId) navigator.push(ProductsScreen())
            }
        }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            backgroundColor = SallahColor.WhiteWhisper,
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(top = 30.dp)) {
                LazyColumn(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        LogoImage(modifier = Modifier.size(80.dp).padding(bottom = 16.dp))
                    }
                    item {
                        Text(
                            "Log In", style = MaterialTheme.typography.h6.copy(
                                color = Color.Black,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                            ), modifier = Modifier.padding(bottom = 30.dp)
                        )
                    }
                    item {
                        LogInTextField(
                            value = uiState.value.email,
                            enabled = !isLoading,
                            onValueChanged = {
                                viewModel.onUsernameChanged(it)
                            },
                            icon = Icons.Rounded.Person,
                        )
                    }
                    item {
                        LogInTextField(
                            value = uiState.value.password,
                            enabled = !isLoading,
                            onValueChanged = {
                                viewModel.onPasswordChanged(it)
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password,
                            icon = Icons.Rounded.Lock,
                        )

                    }
                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                        AnimatedVisibility(visible = !isLoading) {
                            PrimaryActionButton(
                                label = "Log In",
                                onClick = {
                                    keyboardController?.hide()
                                    viewModel.onLoginClicked()
                                },
                            )
                        }
                        ProgressIndicator(isLoading)
                    }
                }
                Text(
                    "For now you can only log in through this account.",
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 14.sp, color = SallahColor.GreyWoodBark
                    ),
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp)
                )
            }
        }
    }


    @Composable
    private fun LogInTextField(
        value: String,
        enabled: Boolean,
        onValueChanged: (String) -> Unit,
        keyboardType: KeyboardType = KeyboardType.Text,
        imeAction: ImeAction = ImeAction.Next,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        icon: ImageVector,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(60.dp)
                .background(Color.White, shape = RoundedCornerShape(10.dp)).padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.height(45.dp).width(48.dp).background(
                        SallahColor.OrangeJaffa.copy(alpha = 0.1F),
                        shape = RoundedCornerShape(10.dp)
                    )
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp).align(Alignment.Center),
                        tint = SallahColor.PinkLipstick,
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                BasicTextField(
                    value,
                    onValueChange = { onValueChanged(it) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType,
                        autoCorrect = true,
                        imeAction = imeAction,
                    ),
                    textStyle = MaterialTheme.typography.body1.copy(
                        color = SallahColor.GreyWoodBark,
                        fontSize = 14.sp,
                    ),
                    visualTransformation = visualTransformation,
                    enabled = enabled,
                )
            }
        }
    }


}