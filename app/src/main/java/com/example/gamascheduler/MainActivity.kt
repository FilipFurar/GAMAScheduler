package com.example.gamascheduler

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.gamascheduler.data.model.ErrorMessage
import com.example.gamascheduler.ui.auth.login.LoginScreen
import com.example.gamascheduler.ui.theme.GAMASchedulerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

enum class SchedulerScreen() {
    Login,
    Home,
    Signup,
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }

            GAMASchedulerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    // TODO
                    val pad = innerPadding

                    LoginScreen(
                        showErrorSnackbar = { errorMessage ->
                            val message = getErrorMessage(errorMessage)
                            scope.launch { snackbarHostState.showSnackbar(message) }
                        }
                    )
                }
            }
        }
    }
    private fun getErrorMessage(error: ErrorMessage): String {
        return when (error) {
            is ErrorMessage.StringError -> error.message
            is ErrorMessage.IdError -> this@MainActivity.getString(error.message)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

}

/*fun getErrorMessage(context: Context, error: ErrorMessage): String {
    return when (error) {
        is ErrorMessage.StringError -> error.message
        is ErrorMessage.IdError -> context.getString(error.message)
    }
}*/

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GAMASchedulerTheme {
        Greeting("Android")
    }
}