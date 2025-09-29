package com.dynamite.proyectox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text // Import para el Text de ejemplo en HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dynamite.proyectox.presentation.login.LoginScreen // Asegúrate que esta ruta sea correcta
import com.dynamite.proyectox.ui.theme.ProyectoXTheme
import dagger.hilt.android.AndroidEntryPoint

// Definición de rutas
sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    // Podrías añadir más pantallas aquí, ej:
    // object HomeScreen : Screen("home_screen")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoXTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(
                // Aquí podrías pasar un callback para navegar tras un login exitoso
                // Ejemplo: onLoginSuccess = { navController.navigate(Screen.HomeScreen.route) { popUpTo(Screen.LoginScreen.route) { inclusive = true } } }
            )
        }
        // composable(route = Screen.HomeScreen.route) {
        //     // Aquí iría tu HomeScreen
        //     Text("¡Bienvenido a la pantalla principal!")
        // }
    }
}
