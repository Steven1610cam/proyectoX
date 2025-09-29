package com.dynamite.proyectox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
// import androidx.navigation.NavHostController // No es necesario aquí directamente
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dynamite.proyectox.presentation.home.HomeScreen
import com.dynamite.proyectox.presentation.login.LoginScreen
import com.dynamite.proyectox.presentation.order.OrderScreen // IMPORT AÑADIDO
import com.dynamite.proyectox.ui.theme.ProyectoXTheme
import dagger.hilt.android.AndroidEntryPoint

// Argument Keys
const val ARG_TABLE_NUMBER = "tableNumber"

// Definición de rutas
sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object HomeScreen : Screen("home_screen")
    object OrderScreen : Screen("order_screen/{$ARG_TABLE_NUMBER}") {
        fun withArgs(tableNumber: Int): String {
            return route.replace("{$ARG_TABLE_NUMBER}", tableNumber.toString())
        }
    }
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
                onLoginSuccess = {
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController) // Pasamos navController
        }
        composable(
            route = Screen.OrderScreen.route,
            arguments = listOf(navArgument(ARG_TABLE_NUMBER) { type = NavType.IntType })
        ) { /* backStackEntry -> */ // backStackEntry ya no se usa directamente aquí
            // val tableNumber = backStackEntry.arguments?.getInt(ARG_TABLE_NUMBER) ?: 0 // No es necesario pasarla
            OrderScreen(navController = navController) // CORREGIDO
        }
    }
}
