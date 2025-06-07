package com.example.gamascheduler.data.model

import androidx.annotation.StringRes
import androidx.compose.ui.res.stringResource
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

/**
 * firebase error template:
 * https://github.com/FirebaseExtended/make-it-so-android/blob/main/v2/app/src/main/java/com/google/firebase/example/makeitso/data/model/ErrorMessage.kt
 */
sealed class ErrorMessage {
    class StringError(val message: String) : ErrorMessage()
    class IdError(@StringRes val message: Int) : ErrorMessage()
}