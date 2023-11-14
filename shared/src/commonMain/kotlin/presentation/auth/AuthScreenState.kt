package presentation.auth

data class AuthScreenState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
)