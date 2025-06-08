package com.example.gamascheduler.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gamascheduler.R
import com.example.gamascheduler.ui.auth.AuthViewModel
import com.example.gamascheduler.ui.shared.BasicButton

@Composable
fun CalendarScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
                   onSignOut: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.welcome, email ?: stringResource(R.string.email))
        )
        BasicButton(
            label = R.string.logout,
            onButtonClick = {
                authViewModel.logOut(onSignOut)
            }
        )
    }
}