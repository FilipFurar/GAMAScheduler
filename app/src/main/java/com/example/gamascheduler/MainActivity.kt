package com.example.gamascheduler

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.gamascheduler.data.model.ErrorMessage
import com.example.gamascheduler.ui.auth.login.LoginScreen
import com.example.gamascheduler.ui.theme.GAMASchedulerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GAMASchedulerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    LoginScreen(
        showErrorSnackbar = { errorMessage ->
            val message = getErrorMessage(context,errorMessage)
            scope.launch { snackbarHostState.showSnackbar(message) }
        }
    )
}

fun getErrorMessage(context: Context, error: ErrorMessage): String {
    return when (error) {
        is ErrorMessage.StringError -> error.message
        is ErrorMessage.IdError -> context.getString(error.message)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GAMASchedulerTheme {
        Greeting("Android")
    }
}