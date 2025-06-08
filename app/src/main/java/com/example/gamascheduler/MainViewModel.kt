package com.example.gamascheduler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.example.gamascheduler.data.model.ErrorMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import com.google.firebase.crashlytics.ktx.crashlytics
import kotlinx.coroutines.launch

open class MainViewModel : ViewModel() {
    /**
     * from firebase docs:
     * https://github.com/FirebaseExtended/make-it-so-android/blob/main/v2/app/src/main/java/com/google/firebase/example/makeitso/MainViewModel.kt
     */
    fun launchCatching(
        showErrorSnackbar: (com.example.gamascheduler.data.model.ErrorMessage) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Firebase.crashlytics.recordException(throwable)
                println("in MainViewModel/launchCatching")
                val error = if (throwable.message.isNullOrBlank()) {
                    ErrorMessage.IdError(R.string.generic_error)
                } else {
                    ErrorMessage.StringError(throwable.message!!)
                }
                showErrorSnackbar(error)
            },
            block = block
        )
}