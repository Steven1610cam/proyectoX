package com.dynamite.proyectox.domain.model

import java.math.BigDecimal

data class OrderItem(
    val id: String, // Podría ser un UUID
    val productId: String, // Referencia al Product
    val productName: String, // Denormalizado para fácil acceso
    val quantity: Int,
    val unitPrice: BigDecimal, // Precio del producto al momento del pedido
    val notes: String? // Notas especiales para este artículo (ej: "sin cebolla")
) {
    val totalPrice: BigDecimal
        get() = unitPrice * BigDecimal(quantity)
}