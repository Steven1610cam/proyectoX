package com.dynamite.proyectox.presentation.cart

import com.dynamite.proyectox.domain.model.CartItem
import java.math.BigDecimal

data class CartScreenState(
    val tableNumber: Int = 0,
    val cartItems: List<CartItem> = emptyList(),
    val notes: String = "",
    val totalAmount: BigDecimal = BigDecimal.ZERO,
    val isLoading: Boolean = false
    // Puedes añadir más estados según sea necesario, ej: mensaje de error
)
