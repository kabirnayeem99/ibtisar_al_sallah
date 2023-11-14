package core

sealed class UiEvent {
    data class Warning(val message: String) : UiEvent()
    data class Error(val message: String) : UiEvent()
}
