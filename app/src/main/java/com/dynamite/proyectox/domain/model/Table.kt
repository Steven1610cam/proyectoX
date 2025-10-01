package com.dynamite.proyectox.domain.model

enum class TableStatus {
    FREE,
    OCCUPIED,
    BILL_REQUESTED
}

data class Table(
    val id: String, // Podría ser un UUID o un número de mesa
    val number: Int, // Número visible de la mesa, ej: 1, 2, T-1
    val status: TableStatus,
    val currentOrderId: String? // ID del pedido activo en esta mesa (si lo hay)
)
