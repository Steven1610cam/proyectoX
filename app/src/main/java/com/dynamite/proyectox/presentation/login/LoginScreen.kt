package com.dynamite.proyectox.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dynamite.proyectox.common.Resource

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit, // PARÁMETRO AÑADIDO
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState = viewModel.loginState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is Resource.Loading
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (loginState) {
            is Resource.Loading -> {
                CircularProgressIndicator()
            }
            is Resource.Success -> {
                if (loginState.data == true) {
                    // Llamar al callback para navegar
                    LaunchedEffect(key1 = Unit) { // key1 = Unit para que se ejecute solo una vez cuando entre aquí
                        onLoginSuccess()
                    }
                }
            }
            is Resource.Error -> {
                Text("Error: ${loginState.message}", color = MaterialTheme.colorScheme.error)
            }
            else -> {
                // Estado inicial o no manejado (por ejemplo, antes del primer intento de login)
            }
        }
    }
}
