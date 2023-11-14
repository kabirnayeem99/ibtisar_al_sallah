package presentation.auth

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthScreenModel : StateScreenModel<AuthScreenState>(AuthScreenState()) {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEmailChanged(email: String) {
        screenModelScope.launch(Dispatchers.IO) {
            if (email.contains(" ")) showUserMessage("Email can't contain spaces.")
            mutableState.update { it.copy(email = email.trim()) }
        }
    }

    fun onPasswordChanged(password: String) {
        screenModelScope.launch(Dispatchers.IO) {
            if (password.contains(" ")) showUserMessage("Password can't contain spaces.")
            mutableState.update { it.copy(password = password.trim()) }
        }
    }

    private fun showUserMessage(message: String, isError: Boolean = false) {
        screenModelScope.launch {
            if (message.isBlank()) return@launch
            if (isError) _uiEvent.emit(UiEvent.Error(message))
            else _uiEvent.emit(UiEvent.Warning(message))
        }
    }

    private fun toggleLoading(loading: Boolean) {
        mutableState.update { it.copy(isLoading = loading) }
    }

    private fun validateEmailPassword(email: String, password: String): String? {
        if (email.isBlank()) return "Email cannot be empty"
        if (password.isBlank()) return "Password cannot be empty"

        val isValidEmail = isValidEmail(email)
        if (!isValidEmail) return "Invalid email"

        val passwordValidation = validatePassword(password)
        if (passwordValidation != null) return passwordValidation

        return null
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$")
        return emailRegex.matches(email)
    }


    private fun validatePassword(password: String): String? {
        if (password.length < 8) return "Password must be at least 8 characters"

        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) return "Password should contain digits"

        val hasUppercase = password.any { it.isUpperCase() }
        if (!hasUppercase) return "Password should contain uppercase characters"

        val hasLowercase = password.any { it.isLowerCase() }
        if (!hasLowercase) return "Password should contain lowercase characters"

        val hasSpecialChar = password.any { it in "!@#$%^&*()-_=+[{]};:'\",<.>/?|" }
        if (!hasSpecialChar) return "Password should contain special characters, like '@#$%^&*()'"

        return null
    }


    private var onLoginClickedJob: Job? = null

    fun onLoginClicked() {
        onLoginClickedJob?.cancel()
        onLoginClickedJob = screenModelScope.launch(Dispatchers.IO) {
            toggleLoading(true)
            val validation = validateEmailPassword(state.value.email, state.value.password)
            delay(2000L)
            if (validation != null) {
                showUserMessage(validation)
                toggleLoading(false)
                return@launch
            }
            delay(3000L)
            toggleLoading(false)
            showUserMessage("You are logged in.")
        }
    }

}