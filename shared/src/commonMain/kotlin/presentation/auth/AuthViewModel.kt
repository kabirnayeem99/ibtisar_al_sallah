package presentation.auth

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import core.UiEvent
import domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) :
    StateScreenModel<AuthScreenState>(AuthScreenState()) {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _loggedIn = MutableSharedFlow<Boolean>()
    val loggedIn = _loggedIn.asSharedFlow()


    fun onUsernameChanged(username: String) {
        screenModelScope.launch(Dispatchers.IO) {
            if (username.contains(" ")) showUserMessage("Username can't contain spaces.")
            mutableState.update { it.copy(email = username.trim()) }
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
        if (email.isBlank()) return "Username cannot be empty"
        if (password.isBlank()) return "Password cannot be empty"


        val passwordValidation = validatePassword(password)
        if (passwordValidation != null) return passwordValidation

        return null
    }


    private fun validatePassword(password: String): String? {
        if (password.length < 6) return "Password must be at least 6 characters"

        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) return "Password should contain digits"

        val hasUppercase = password.any { it.isUpperCase() }
        if (!hasUppercase) return "Password should contain uppercase characters"

        val hasLowercase = password.any { it.isLowerCase() }
        if (!hasLowercase) return "Password should contain lowercase characters"

        return null
    }


    private var onLoginClickedJob: Job? = null

    fun onLoginClicked() {
        onLoginClickedJob?.cancel()
        onLoginClickedJob = screenModelScope.launch(Dispatchers.IO) {
            try {

                val email = state.value.email
                val password = state.value.password
                val validation = validateEmailPassword(email, password)
                if (validation != null) throw Exception(validation)

                toggleLoading(true)
                val result = authRepository.login(username = email, password = password)

                if (result.isSuccess) {
                    showUserMessage("You are logged in.")
                    toggleLoading(false)
                    _loggedIn.emit(true)
                }
                if (result.isFailure) throw result.exceptionOrNull() ?: Exception("Failed to login")

            } catch (e: Exception) {
                toggleLoading(false)
                showUserMessage(e.message ?: e.toString())
            }
        }
    }

}