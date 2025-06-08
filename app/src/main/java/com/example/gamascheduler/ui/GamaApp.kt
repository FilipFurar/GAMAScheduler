package com.example.gamascheduler.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gamascheduler.data.model.ErrorMessage
import com.example.gamascheduler.ui.auth.login.LoginScreen
import com.example.gamascheduler.ui.auth.login.AuthViewModel
import com.example.gamascheduler.ui.calendar.AddEventScreen
import com.example.gamascheduler.ui.calendar.CalendarScreen
import com.example.gamascheduler.ui.calendar.CalendarViewModel
import kotlinx.coroutines.launch

enum class SchedulerScreen {
    Login,
    Calendar,
    Signup,
    AddEvent,
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GamaApp(
    viewModel: AuthViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    getErrorMessage: (ErrorMessage) -> String
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = SchedulerScreen.valueOf(
        backStackEntry?.destination?.route ?: SchedulerScreen.Login.name
    )
    println("current screen: ${backStackEntry?.destination?.route}")

    println("in GamaApp")

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        val loginUiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = SchedulerScreen.Login.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = SchedulerScreen.Login.name) {
                LoginScreen(
                    showErrorSnackbar = { errorMessage ->
                        val message = getErrorMessage(errorMessage)
                        scope.launch { snackbarHostState.showSnackbar(message) }
                    },
                    navigateToCalendar = {
                        navController.navigate(SchedulerScreen.Calendar.name) {
                            popUpTo(SchedulerScreen.Login.name) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(route = SchedulerScreen.Calendar.name) {
                CalendarScreen(
                    navigateToLogin = {
                        navController.popBackStack(
                            route = SchedulerScreen.Login.name,
                            inclusive = true
                        )
                        navController.navigate(SchedulerScreen.Login.name)

                    },
                    navigateToAddEvent = {
                        navController.navigate(SchedulerScreen.AddEvent.name)
                    }
                )
            }
            composable(route = SchedulerScreen.AddEvent.name) {
                val calendarViewModel: CalendarViewModel = hiltViewModel()

                AddEventScreen(
                    viewModel = calendarViewModel,
                    onEventCreated = {
                        navController.popBackStack() // Go back to Calendar after event creation
                    }
                )
            }
        }
    }
}
