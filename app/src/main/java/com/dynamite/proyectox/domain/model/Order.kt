package com.dynamite.proyectox.domain.model

import java.math.BigDecimal
import java.util.Date // O puedes usar kotlinx-datetime para manejo de fechas más moderno

data class Order(
    val id: String, // Podría ser un UUID
    val tableId: String, // Referencia a la Table
    val waiterId: String, // Referencia al User (mesero)
    val items: List<OrderItem>,
    val status: OrderStatus,
    val orderTimestamp: Date, // Momento en que se tomó el pedido
    val lastUpdateTimestamp: Date // Momento de la última actualización del estado
) {
    val totalAmount: BigDecimal
        get() = items.sumOf { it.totalPrice }
}