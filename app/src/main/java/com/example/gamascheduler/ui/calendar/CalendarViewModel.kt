package com.example.gamascheduler.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamascheduler.data.model.Event
import com.example.gamascheduler.data.repository.AuthRepository
import com.example.gamascheduler.data.repository.EventRepository
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.Event.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _userEmail = MutableStateFlow(authRepository.currentUserEmail)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    private val _currentUserId = MutableStateFlow(authRepository.currentUser?.uid)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    private val _addEventError = MutableStateFlow<String?>(null)
    val addEventError: StateFlow<String?> = _addEventError.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadEvents()
        loadCurrentUser()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            eventRepository.getEvents().collectLatest { events ->
                _events.value = events
            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            _addEventError.value = null

            try {
                eventRepository.createEvent(event)
                loadEvents()
            } catch (e: Exception) {
                _addEventError.value = e.localizedMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCurrentUser() {
        _userEmail.value = authRepository.currentUserEmail
        _currentUserId.value = authRepository.currentUser?.uid
    }

    fun logOut(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.signOut()
            onSuccess()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteAccount()
        }
    }

    fun toggleUserAssignment(event: Event) {
        val userEmail = _userEmail.value ?: return
        val updatedAssignedTo = if (userEmail in event.assignedTo) {
            event.assignedTo - userEmail
        } else {
            event.assignedTo + userEmail
        }

        val updatedEvent = event.copy(assignedTo = updatedAssignedTo)

        viewModelScope.launch {
            try {
                eventRepository.updateEvent(updatedEvent)
                loadEvents()
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }
}