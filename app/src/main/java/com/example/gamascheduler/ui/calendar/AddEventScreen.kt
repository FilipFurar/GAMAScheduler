package com.example.gamascheduler.ui.calendar

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.ui.draw.scale
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gamascheduler.ui.calendar.CalendarViewModel
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.DatePicker
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.ui.window.Dialog
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.gamascheduler.R
import com.example.gamascheduler.data.model.Event

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEventScreen(
    viewModel: CalendarViewModel,
    onEventCreated: () -> Unit
) {
    val uiState = rememberAddEventState()

    Scaffold(
        topBar = { AddEventTopBar() },
        floatingActionButton = {
            CreateEventButton(
                uiState = uiState,
                onCreateClick = {
                    viewModel.addEvent(it)
                    onEventCreated()
                }
            )
        }
    ) { innerPadding ->
        AddEventContent(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState
        )
    }
}

@Composable
private fun rememberAddEventState() = remember {
    mutableStateOf(AddEventUiState())
}

data class AddEventUiState(
    val title: String = "",
    val happeningOn: LocalDateTime? = null,
    val showDatePicker: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEventTopBar() {
    TopAppBar(
        title = { Text(stringResource(R.string.add)) },
        navigationIcon = {
            IconButton(onClick = { /* Handle back */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AddEventContent(
    modifier: Modifier = Modifier,
    uiState: MutableState<AddEventUiState>
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .verticalScroll(rememberScrollState())
    ) {
        EventTitleInput(
            title = uiState.value.title,
            onTitleChange = { uiState.value = uiState.value.copy(title = it) }
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_big)))

        DateTimeSelection(
            happeningOn = uiState.value.happeningOn,
            showDatePicker = uiState.value.showDatePicker,
            onDateTimeSelected = { dateTime ->
                uiState.value = uiState.value.copy(
                    happeningOn = dateTime,
                    showDatePicker = false
                )
            },
            timeFormat = stringResource(R.string.time_format),
            onShowDatePicker = { show ->
                uiState.value = uiState.value.copy(showDatePicker = show)
            }
        )
    }
}

@Composable
private fun EventTitleInput(
    title: String,
    onTitleChange: (String) -> Unit
) {
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        label = { Text(stringResource(R.string.title)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = title.isBlank(),
        supportingText = {
            if (title.isBlank()) {
                Text(stringResource(R.string.required_field))
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DateTimeSelection(
    happeningOn: LocalDateTime?,
    showDatePicker: Boolean,
    onDateTimeSelected: (LocalDateTime) -> Unit,
    onShowDatePicker: (Boolean) -> Unit,
    timeFormat: String
) {
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern(timeFormat)
    }

    // Date selection button
    OutlinedButton(
        onClick = { onShowDatePicker(true) },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (happeningOn == null) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    ) {
        Text(
            text = happeningOn?.format(dateFormatter) ?: stringResource(R.string.pick_date),
            style = MaterialTheme.typography.bodyLarge
        )
    }

    if (happeningOn == null) {
        Text(
            text = stringResource(R.string.required_field),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_very_small), top = dimensionResource(R.dimen.padding_very_small))
        )
    }

    // Date/Time picker dialog
    if (showDatePicker) {
        DateTimePickerDialog(
            onDismiss = { onShowDatePicker(false) },
            onConfirm = { dateTime ->
                onDateTimeSelected(dateTime)
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateTimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (LocalDateTime) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = dimensionResource(R.dimen.padding_small),
            modifier = Modifier
                .wrapContentHeight()
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Column(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = stringResource(R.string.pick_date),
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier
                            .scale(0.85f)
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier
                            .scale(0.85f)
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val localDate = Instant.ofEpochMilli(selectedDateMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            val localTime = LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            onConfirm(LocalDateTime.of(localDate, localTime))
                        }
                    }) {
                        Text(stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun CreateEventButton(
    uiState: MutableState<AddEventUiState>,
    onCreateClick: (Event) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val required = stringResource(R.string.required_field)
    ExtendedFloatingActionButton(
        onClick = {
            if (uiState.value.title.isBlank()) {
                Toast.makeText(context, required, Toast.LENGTH_SHORT).show()
                return@ExtendedFloatingActionButton
            }
            if (uiState.value.happeningOn == null) {
                Toast.makeText(context, required, Toast.LENGTH_SHORT).show()
                return@ExtendedFloatingActionButton
            }

            scope.launch {
                onCreateClick(
                    Event(
                        title = uiState.value.title,
                        type = 1,
                        happeningOn = uiState.value.happeningOn!!.toTimestamp(),
                        createdOn = Timestamp.now(),
                        assignedTo = emptyList()
                    )
                )
            }
        },
        icon = { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_event)) },
        text = { Text(stringResource(R.string.add_event)) }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toTimestamp(): Timestamp {
    return Timestamp(Date.from(this.atZone(ZoneId.systemDefault()).toInstant()))
}