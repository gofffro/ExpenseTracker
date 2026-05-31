package com.example.expensetracker.feature.auth.presentation

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.core.auth.AuthResult
import com.example.expensetracker.feature.auth.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity
    val snackbarHostState = remember { SnackbarHostState() }

    if (viewModel.currentUser != null) {
        LaunchedEffect(Unit) {
            onLoginSuccess()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loginResult.collect { result ->
            when (result) {
                is AuthResult.Success -> onLoginSuccess()
                is AuthResult.Error -> snackbarHostState.showSnackbar(result.message)
                AuthResult.Cancelled -> snackbarHostState.showSnackbar("Login cancelled")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to Expense Tracker",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = { viewModel.loginWithVk(activity) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(8.dp)
            ) {
                Text("Login with VK")
            }

            Button(
                onClick = { viewModel.loginWithYandex(activity) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(8.dp)
            ) {
                Text("Login with Yandex")
            }

            OutlinedButton(
                onClick = { viewModel.loginAsGuest() },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(8.dp)
            ) {
                Text("Login as Guest")
                }

                Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { viewModel.triggerTestCrash() }
            ) {
                Text("Trigger Test Crash (Debug Only)")
            }
        }
    }
}
