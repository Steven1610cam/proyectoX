package com.dynamite.proyectox.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dynamite.proyectox.common.Resource
import com.dynamite.proyectox.domain.model.Product
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val tableNumber = viewModel.tableNumber.value
    val productsState = viewModel.productsState.value

    // Estados para la UI (eventualmente se moverán o se conectarán al ViewModel)
    var searchQuery by remember { mutableStateOf("") }
    val categories = listOf("BURGERS", "BEBIDAS", "ENTRADAS", "SOPAS", "POSTRES", "OTROS") // Ejemplo
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()) } // Selecciona la primera por defecto

    // Simular el filtrado por categoría o búsqueda (lógica real en ViewModel más adelante)
    val filteredProducts = if (productsState is Resource.Success) {
        productsState.data?.filter {
            product ->
            (selectedCategory == null || product.category.equals(selectedCategory, ignoreCase = true)) &&
                    (searchQuery.isBlank() || product.name.contains(searchQuery, ignoreCase = true))
        } ?: emptyList()
    } else {
        emptyList()
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Menú")
                        if (tableNumber != null) {
                            Text(
                                "Mesa $tableNumber",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Navegar a la pantalla del carrito */ }) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Carrito"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Barra de Búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar producto") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                shape = RoundedCornerShape(8.dp), // Bordes redondeados
                singleLine = true
            )

            // Selector de Categorías
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    Button(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(20.dp), // Más redondeado para chips
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedCategory == category) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (selectedCategory == category) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text(category)
                    }
                }
            }

            // Lista de Productos o Estados
            Box(modifier = Modifier.weight(1f)) { // Para que la lista ocupe el espacio restante
                when (productsState) {
                    is Resource.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is Resource.Error -> {
                        Text(
                            text = productsState.message ?: "Error al cargar productos",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                    is Resource.Success -> {
                        if (filteredProducts.isEmpty()) {
                            Text(
                                text = if (productsState.data.isNullOrEmpty()) "No hay productos disponibles." else "No hay productos que coincidan.",
                                modifier = Modifier.align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            ) {
                                items(filteredProducts) { product ->
                                    ProductListItem(product = product, onAddClick = {
                                        // TODO: Implementar lógica para añadir al carrito
                                        println("Añadir ${product.name} al carrito")
                                    })
                                    Divider() // Divisor entre ítems
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductListItem(
    product: Product,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp), // Un poco más de padding vertical
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Para separar el botón "Add"
    ) {
        Column(modifier = Modifier.weight(1f)) { // Columna para Nombre y Precio
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium, // Un poco más grande
                fontWeight = FontWeight.SemiBold // Un poco más de peso
            )
            Text(
                text = "$${product.price}", // Formato de moneda
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Color sutil para el precio
            )
        }
        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFA726) // Naranja similar al de la imagen
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp) // Ajustar padding del botón
        ) {
            Text("Add +", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

// Preview para desarrollo rápido (Opcional pero recomendado)
@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    MaterialTheme { // Necesitas un tema para los previews
        val navController = rememberNavController()
        // Simular un estado de éxito con algunos productos para el preview
        val previewViewModel = OrderViewModel(
            getProductsUseCase = com.dynamite.proyectox.domain.usecase.product.GetProductsUseCase(
                // Repositorio de producto simulado para el preview
                productRepository = object : com.dynamite.proyectox.domain.repository.ProductRepository {
                    override suspend fun getProducts(category: String?): List<Product> {
                        return listOf(
                            Product("1", "Hamburguesa Clásica", "Deliciosa hamburguesa", BigDecimal("12.00"), "BURGERS", null),
                            Product("2", "Hamburguesa BBQ", "Con salsa BBQ", BigDecimal("15.00"), "BURGERS", null),
                            Product("3", "Coca-Cola", "Bebida refrescante", BigDecimal("3.00"), "BEBIDAS", null)
                        )
                    }
                    override suspend fun getProductById(productId: String): Product? = null

                    // --- MÉTODOS AÑADIDOS PARA LA PREVIEW ---
                    override suspend fun addProduct(product: Product): Boolean {
                        println("Preview: addProduct llamado con $product")
                        return true // Simular éxito
                    }

                    override suspend fun updateProduct(product: Product): Boolean {
                        println("Preview: updateProduct llamado con $product")
                        return true // Simular éxito
                    }

                    override suspend fun deleteProduct(productId: String): Boolean {
                        println("Preview: deleteProduct llamado con id $productId")
                        return true // Simular éxito
                    }
                    // --- FIN DE MÉTODOS AÑADIDOS ---
                }
            ),
            savedStateHandle = androidx.lifecycle.SavedStateHandle(mapOf(com.dynamite.proyectox.ARG_TABLE_NUMBER to 1))
        )
        OrderScreen(navController = navController, viewModel = previewViewModel)
    }
}
