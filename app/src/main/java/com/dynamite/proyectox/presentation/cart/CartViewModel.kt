package com.dynamite.proyectox.presentation.cart

import android.util.Log // <-- AÑADIR IMPORT
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamite.proyectox.ARG_TABLE_NUMBER
import com.dynamite.proyectox.domain.model.CartItem
import com.dynamite.proyectox.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

private const val TAG = "CartViewModel_DEBUG" // Tag para filtrar logs

@HiltViewModel
class CartViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartScreenState())
    val uiState: StateFlow<CartScreenState> = _uiState.asStateFlow()

    private val _cartItems = mutableListOf<CartItem>()

    init {
        val tableNumber = savedStateHandle.get<Int>(ARG_TABLE_NUMBER) ?: 0
        Log.d(TAG, "ViewModel instance $this CREATED. TableNumber from SavedStateHandle: $tableNumber")
        _uiState.update { it.copy(tableNumber = tableNumber) }
        updateCartState() // Actualiza el estado inicial (carrito vacío)
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch {
            Log.d(TAG, "addProductToCart: Intentando añadir ${product.name} (ID: ${product.id})")
            val existingItemIndex = _cartItems.indexOfFirst { it.product.id == product.id }

            if (existingItemIndex != -1) {
                val existingItem = _cartItems[existingItemIndex]
                Log.d(TAG, "addProductToCart: ${product.name} ya existe, cantidad actual ${existingItem.quantity}. Incrementando.")
                _cartItems[existingItemIndex] = existingItem.copy(quantity = existingItem.quantity + 1)
            } else {
                Log.d(TAG, "addProductToCart: ${product.name} es nuevo. Añadiendo con cantidad 1.")
                _cartItems.add(CartItem(product = product, quantity = 1))
            }
            Log.d(TAG, "addProductToCart: _cartItems después de añadir/actualizar: ${_cartItems.map { it.product.name + " qty:" + it.quantity }}")
            updateCartState()
        }
    }

    fun incrementQuantity(productId: String) {
        viewModelScope.launch {
            Log.d(TAG, "incrementQuantity: Intentando incrementar producto ID $productId")
            val itemIndex = _cartItems.indexOfFirst { it.product.id == productId }
            if (itemIndex != -1) {
                val item = _cartItems[itemIndex]
                Log.d(TAG, "incrementQuantity: Producto ${item.product.name} encontrado, cantidad actual ${item.quantity}. Incrementando.")
                _cartItems[itemIndex] = item.copy(quantity = item.quantity + 1)
            } else {
                Log.w(TAG, "incrementQuantity: Producto ID $productId NO encontrado para incrementar.")
            }
            Log.d(TAG, "incrementQuantity: _cartItems después de incrementar: ${_cartItems.map { it.product.name + " qty:" + it.quantity }}")
            updateCartState()
        }
    }

    fun decrementQuantity(productId: String) {
        viewModelScope.launch {
            Log.d(TAG, "decrementQuantity: Intentando decrementar producto ID $productId")
            val itemIndex = _cartItems.indexOfFirst { it.product.id == productId }
            if (itemIndex != -1) {
                val item = _cartItems[itemIndex]
                Log.d(TAG, "decrementQuantity: Producto ${item.product.name} encontrado, cantidad actual ${item.quantity}. Decrementando.")
                if (item.quantity > 1) {
                    _cartItems[itemIndex] = item.copy(quantity = item.quantity - 1)
                } else {
                    Log.d(TAG, "decrementQuantity: Cantidad es 1, eliminando producto ${item.product.name}.")
                    _cartItems.removeAt(itemIndex)
                }
            } else {
                Log.w(TAG, "decrementQuantity: Producto ID $productId NO encontrado para decrementar.")
            }
            Log.d(TAG, "decrementQuantity: _cartItems después de decrementar/eliminar: ${_cartItems.map { it.product.name + " qty:" + it.quantity }}")
            updateCartState()
        }
    }

    fun removeItem(productId: String) { // No la estamos usando directamente, pero por si acaso
        viewModelScope.launch {
            Log.d(TAG, "removeItem: Intentando eliminar producto ID $productId")
            val removed = _cartItems.removeAll { it.product.id == productId }
            Log.d(TAG, "removeItem: ¿Eliminado? $removed. _cartItems: ${_cartItems.map { it.product.name + " qty:" + it.quantity }}")
            updateCartState()
        }
    }

    fun updateNotes(notes: String) {
        Log.d(TAG, "updateNotes: Nuevas notas: $notes")
        _uiState.update { it.copy(notes = notes) }
    }

    private fun updateCartState() {
        val currentItems = _cartItems.toList()
        val total = currentItems.fold(BigDecimal.ZERO) { acc, cartItem ->
            acc + cartItem.subtotal
        }
        Log.d(TAG, "updateCartState: Actualizando UI State. currentItems: ${currentItems.map { it.product.name + " qty:" + it.quantity }}, total: $total")
        _uiState.update {
            it.copy(
                cartItems = currentItems,
                totalAmount = total
            )
        }
        Log.d(TAG, "updateCartState: UI State POST-actualización: ${uiState.value}")
    }

    fun clearCart() {
        viewModelScope.launch {
            Log.d(TAG, "clearCart: Limpiando carrito.")
            _cartItems.clear()
            updateCartState()
            updateNotes("")
        }
    }
    // TODO: Función para "Enviar Pedido a Cocina"
    // fun submitOrder() { ... }
}
