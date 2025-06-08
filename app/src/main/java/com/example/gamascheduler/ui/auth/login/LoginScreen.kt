package com.example.gamascheduler.ui.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gamascheduler.R
import com.example.gamascheduler.data.model.ErrorMessage
import com.example.gamascheduler.ui.shared.BasicButton

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    showErrorSnackbar: (ErrorMessage) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    Column(
        verticalArrangement = Arrangement.spacedBy(mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(mediumPadding)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
        )

        OutlinedTextField(
            value = email,
            singleLine = true,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            isError = false,
            keyboardActions = KeyboardActions(
                onDone = {}
            )
        )

        OutlinedTextField(
            value = password,
            singleLine = true,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            isError = false,
            keyboardActions = KeyboardActions(
                onDone = {
                    loginViewModel.logIn(email, password, showErrorSnackbar)
                }
            )
        )

        BasicButton(
            label = R.string.login,
            onButtonClick = {
                loginViewModel.logIn(email, password, showErrorSnackbar)
                println("logging in with ${email} and ${password}")
            }
        )
    }
}