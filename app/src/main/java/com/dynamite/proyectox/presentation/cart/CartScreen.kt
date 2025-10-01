package com.dynamite.proyectox.presentation.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Print // Icono para enviar pedido
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dynamite.proyectox.domain.model.CartItem
import com.dynamite.proyectox.domain.model.Product
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

// Helper para formatear moneda (puedes moverlo a un archivo de utils)
fun formatCurrency(amount: BigDecimal): String {
    // Usar un Locale que use el punto como separador decimal si así lo deseas, o ajusta según tu preferencia.
    // Locale.US o un Locale específico como "en_US" usa "." para decimales.
    // Locale("es", "ES") usualmente usa ",". Para $12.00, "en_US" o similar es más apropiado.
    val formatter = NumberFormat.getCurrencyInstance(Locale.US) // Ejemplo con US para el formato $12.00
    // Si quieres forzar que no haya símbolos de moneda o ajustarlo:
    // val customFormatter = NumberFormat.getNumberInstance(Locale.US)
    // customFormatter.minimumFractionDigits = 2
    // customFormatter.maximumFractionDigits = 2
    // return "$" + customFormatter.format(amount) // Para añadir el $ manualmente si es necesario.
    return formatter.format(amount)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    tableNumber: Int, // Recibido para consistencia, pero el estado lo tiene
    state: CartScreenState,
    onIncrementItem: (String) -> Unit,
    onDecrementItem: (String) -> Unit,
    onRemoveItem: (String) -> Unit, // Puede que no se use si decremento a 0 elimina
    onNotesChanged: (String) -> Unit,
    onSubmitOrder: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Pedido")
                        // Usar tableNumber del estado, ya que es la fuente de verdad del ViewModel
                        if (state.tableNumber != 0) {
                            Text(
                                "Mesa ${state.tableNumber}",
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
                }
            )
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) { // Añadir elevación para separar visualmente
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(
                            formatCurrency(state.totalAmount),
                            style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFFE65100)), // Naranja más oscuro para el total
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = state.notes,
                        onValueChange = onNotesChanged,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("ambas Hamburguesa sin salsa") },
                        label = { Text("Notas para la cocina") },
                        maxLines = 3,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onSubmitOrder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp), // Botón más alto
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)), // Naranja
                        shape = RoundedCornerShape(12.dp) // Bordes más redondeados
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Print,
                            contentDescription = "Enviar Pedido"
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Enviar Pedido a Cocina", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding) // Usar el innerPadding del Scaffold
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Padding para el texto centrado
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tu carrito está vacío.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                Text(
                    "Productos seleccionados",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                )
                LazyColumn(
                    modifier = Modifier.weight(1f), // Para que ocupe el espacio disponible
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp) // Menos espacio entre ítems
                ) {
                    items(state.cartItems, key = { it.product.id }) { cartItem ->
                        CartListItem(
                            cartItem = cartItem,
                            onIncrement = { onIncrementItem(cartItem.product.id) },
                            onDecrement = { onDecrementItem(cartItem.product.id) }
                        )
                        Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)) // Divisor más sutil
                    }
                }
            }
        }
    }
}

@Composable
fun CartListItem(
    cartItem: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp), // Un poco más de padding vertical
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(cartItem.product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Text(
                text = formatCurrency(cartItem.product.price), // Precio unitario
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFE65100) // Naranja oscuro para el precio
            )
        }
        Spacer(Modifier.width(16.dp)) // Espacio antes de los controles
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp) // Espacio entre botones y texto
        ) {
            IconButton(onClick = onDecrement, modifier = Modifier.size(40.dp)) { // Botones un poco más grandes
                Icon(Icons.Filled.RemoveCircleOutline, contentDescription = "Decrementar cantidad", tint = MaterialTheme.colorScheme.primary)
            }
            Text(
                text = cartItem.quantity.toString(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 8.dp) // Más padding para el número
            )
            IconButton(onClick = onIncrement, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Filled.AddCircleOutline, contentDescription = "Incrementar cantidad", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun CartScreenPreview() {
    val sampleProducts = listOf(
        Product("burg001", "Hamburguesa Clásica", "Carne de res...", BigDecimal("12.00"), "BURGERS", null),
        Product("burg002", "Hamburguesa BBQ", "Doble carne...", BigDecimal("15.00"), "BURGERS", null),
        Product("beb001", "Coca Cola", "Bebida refrescante", BigDecimal("3.00"), "BEBIDAS", null)
    )
    val sampleCartItems = listOf(
        CartItem(sampleProducts[0], 2),
        CartItem(sampleProducts[1], 1),
        CartItem(sampleProducts[2], 3)
    )

    // Es importante usar tu tema de aplicación aquí (ProyectoXTheme) si tiene colores específicos
    MaterialTheme {
        CartScreen(
            navController = rememberNavController(),
            tableNumber = 1, // Este tableNumber es para la preview, el real vendrá del estado.
            state = CartScreenState(
                tableNumber = 1,
                cartItems = sampleCartItems,
                notes = "ambas Hamburguesa sin salsa",
                totalAmount = sampleCartItems.sumOf { it.subtotal } // Calcula el total para la preview
            ),
            onIncrementItem = { println("Preview: Increment $it") },
            onDecrementItem = { println("Preview: Decrement $it") },
            onRemoveItem = { println("Preview: Remove $it") },
            onNotesChanged = { println("Preview: Notes changed to $it") },
            onSubmitOrder = { println("Preview: Submit order") }
        )
    }
}
