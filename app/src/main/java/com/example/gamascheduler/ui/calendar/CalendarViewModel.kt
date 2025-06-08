package com.example.gamascheduler.ui.calendar

import com.example.gamascheduler.MainViewModel
import com.example.gamascheduler.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : MainViewModel() {
    private val _userEmail = MutableStateFlow<String?>(authRepository.currentUserEmail)
    val userEmail: StateFlow<String?> = _userEmail

    fun loadCurrentUser() {
        _userEmail.value = authRepository.currentUserEmail
    }

    fun logOut(
        onSuccess: () -> Unit
    ) {
        launchCatching {
            authRepository.signOut()
            onSuccess()
        }
    }

    fun deleteAccount() {
        launchCatching {
            authRepository.deleteAccount()
        }
    }
}
