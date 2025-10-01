package com.dynamite.proyectox

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dynamite.proyectox.presentation.cart.CartScreen
import com.dynamite.proyectox.presentation.cart.CartViewModel
import com.dynamite.proyectox.presentation.home.HomeScreen
import com.dynamite.proyectox.presentation.login.LoginScreen
import com.dynamite.proyectox.presentation.order.OrderScreen
import com.dynamite.proyectox.presentation.order.OrderViewModel
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
            return "order_screen/$tableNumber"
        }
    }
    object CartScreen : Screen("cart_screen/{$ARG_TABLE_NUMBER}") {
        fun withArgs(tableNumber: Int): String {
            return "cart_screen/$tableNumber"
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
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.OrderScreen.route,
            arguments = listOf(navArgument(ARG_TABLE_NUMBER) { type = NavType.IntType })
        ) { backStackEntry ->
            val orderViewModel: OrderViewModel = hiltViewModel()
            val cartViewModel: CartViewModel = hiltViewModel()

            val orderUiState by orderViewModel.uiState.collectAsStateWithLifecycle()
            val cartUiState by cartViewModel.uiState.collectAsStateWithLifecycle()

            val tableNumber = backStackEntry.arguments?.getInt(ARG_TABLE_NUMBER) ?: 0

            OrderScreen(
                navController = navController,
                tableNumber = tableNumber, // AÑADIR ESTE PARÁMETRO
                state = orderUiState,
                cartItemCount = cartUiState.cartItems.size,
                onSearchQueryChanged = orderViewModel::onSearchQueryChanged,
                onCategorySelected = orderViewModel::onCategorySelected,
                onAddProductClicked = { product ->
                    cartViewModel.addProductToCart(product)
                },
                onNavigateToCart = {
                    if (tableNumber != 0) {
                        navController.navigate(Screen.CartScreen.withArgs(tableNumber))
                    } else {
                        Log.e("Navigation", "Invalid tableNumber to navigate to CartScreen")
                    }
                }
            )
        }
        composable(
            route = Screen.CartScreen.route,
            arguments = listOf(navArgument(ARG_TABLE_NUMBER) { type = NavType.IntType })
        ) { backStackEntry ->
            val viewModel: CartViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val tableNumber = backStackEntry.arguments?.getInt(ARG_TABLE_NUMBER) ?: 0

            CartScreen(
                navController = navController,
                tableNumber = tableNumber,
                state = uiState,
                onIncrementItem = viewModel::incrementQuantity,
                onDecrementItem = viewModel::decrementQuantity,
                onRemoveItem = viewModel::removeItem,
                onNotesChanged = viewModel::updateNotes,
                onSubmitOrder = {
                    Log.d("Cart", "Submit order clicked for table $tableNumber")
                    // Esto se manejará en el ViewModel
                }
            )
        }
    }
}