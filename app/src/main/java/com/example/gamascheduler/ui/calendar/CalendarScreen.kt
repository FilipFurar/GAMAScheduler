package com.example.gamascheduler.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gamascheduler.R
import com.example.gamascheduler.data.model.Event
import com.example.gamascheduler.ui.shared.BasicButton
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CalendarScreen(
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateToAddEvent: () -> Unit
) {
    val userEmail by calendarViewModel.userEmail.collectAsState()
    val events by calendarViewModel.events.collectAsState()
    val currentUserId by calendarViewModel.currentUserId.collectAsState()

    val userName = userEmail ?: currentUserId ?: stringResource(R.string.null_string)

    Scaffold(
        topBar = {
            CalendarTopBar(
                userName = userName,
                onLogout = {
                    calendarViewModel.logOut(onSuccess = navigateToLogin)
                }
            )
        },
        floatingActionButton = {
            AddEventFab(onClick = navigateToAddEvent)
        }
    ) { paddingValues ->
        EventList(
            events = events,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarTopBar(
    userName: String,
    onLogout: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.welcome, userName),
                fontSize = 20.sp
            )
        },
        actions = {
            IconButton(onClick = onLogout) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = stringResource(R.string.logout))
            }
        }
    )
}

@Composable
fun AddEventFab(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_event)) },
        text = { Text(stringResource(R.string.add_event)) },
        modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
    )
}

@Composable
fun EventList(events: List<Event>, modifier: Modifier = Modifier) {
    if (events.isEmpty()) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(stringResource(R.string.no_events), style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
            modifier = modifier
        ) {
            items(events) { event ->
                EventItem(event)
            }
        }
    }
}

@Composable
fun EventItem(event: Event) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = event.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = event.happeningOn.toReadable(),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun Timestamp?.toReadable(): String {
    return this?.toDate()?.let {
        val format = SimpleDateFormat(stringResource(R.string.time_format), Locale.getDefault())
        format.format(it)
    } ?: stringResource(R.string.null_string)
}
