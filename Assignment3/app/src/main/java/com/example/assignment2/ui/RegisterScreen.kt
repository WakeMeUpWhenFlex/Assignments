package com.example.assignment2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment2.network.ApiService
import com.example.assignment2.viewmodel.RegisterViewModel
import com.example.assignment2.viewmodel.RegisterState
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    apiService: ApiService,
    onRegisterSuccess: (String, String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val registerState by viewModel.registerState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.register(apiService, name, email, password)
                }
            }
        ) {
            Text("Create Account")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onNavigateToLogin) {
            Text("Log In")
        }

        when (registerState) {
            is RegisterState.Loading -> CircularProgressIndicator()
            is RegisterState.Error -> Text((registerState as RegisterState.Error).message, color = MaterialTheme.colors.error)
            is RegisterState.Success -> {
                val user = (registerState as RegisterState.Success).user
                onRegisterSuccess(user.id, user.token)
            }
            else -> {}
        }
    }
}
