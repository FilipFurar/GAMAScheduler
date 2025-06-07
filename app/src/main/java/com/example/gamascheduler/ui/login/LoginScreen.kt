package com.example.gamascheduler.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.gamascheduler.R

@Composable
fun LoginScreen() {

    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    Column (
        verticalArrangement = Arrangement.spacedBy(mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(mediumPadding)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
        )
        OutlinedTextField(
            value = "",
            singleLine = true,
            onValueChange = {},
            label = { Text(stringResource(R.string.username)) },
            isError = false,
            keyboardActions = KeyboardActions(
                onDone = {}
            )
        )
        OutlinedTextField(
            value = "",
            singleLine = true,
            onValueChange = {},
            label = { Text(stringResource(R.string.password)) },
            isError = false,
            keyboardActions = KeyboardActions(
                onDone = {}
            )
        )
    }
}