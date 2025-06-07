package com.example.gamascheduler.data.repository

import com.example.gamascheduler.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Kod prevzaty a upraveny:
 * https://github.com/FirebaseExtended/make-it-so-android/blob/main/v2/app/src/main/java/com/google/firebase/example/makeitso/data/repository/AuthRepository.kt
 */
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    val currentUser: FirebaseUser? = authRemoteDataSource.currentUser
    val currentUserIdFlow: Flow<String?> = authRemoteDataSource.currentUserIdFlow

    suspend fun signIn(email: String, password: String) {
        authRemoteDataSource.signIn(email, password)
        println("logged in")
    }

    suspend fun signUp(email: String, password: String) {
        authRemoteDataSource.linkAccount(email, password)
        println("signed up")
    }

    fun signOut() {
        authRemoteDataSource.signOut()
    }

    suspend fun deleteAccount() {
        authRemoteDataSource.deleteAccount()
    }
}