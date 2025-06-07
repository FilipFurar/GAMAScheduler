package com.example.gamascheduler.ui.shared

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamascheduler.R


@Composable
fun BasicButton(@StringRes label: Int, onButtonClick: () -> Unit) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = mediumPadding),
        onClick = onButtonClick
    ) {
        Text(
            text = stringResource(label),
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}