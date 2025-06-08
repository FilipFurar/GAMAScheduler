package com.example.gamascheduler.ui.auth.login

import kotlinx.coroutines.flow.MutableStateFlow
import com.example.gamascheduler.MainViewModel
import com.example.gamascheduler.data.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.gamascheduler.data.model.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class LoginUIState(
    val currentUsername: String = "",
    val currentPassword: String = ""
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : MainViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    init {
        println("LoginViewModel initialized")
    }

    fun logIn(
        email: String,
        password: String,
        showErrorSnackbar: (ErrorMessage) -> Unit
    ) {
        launchCatching(showErrorSnackbar) {
            authRepo.signIn(email, password)
            println("tried logging in")
        }
    }
}
