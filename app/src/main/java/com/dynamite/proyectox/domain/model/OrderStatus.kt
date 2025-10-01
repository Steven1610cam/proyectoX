package com.dynamite.proyectox.domain.model

enum class OrderStatus {
    PENDING,        // Pedido recién tomado, pendiente de enviar a cocina
    PREPARING,      // Pedido en preparación en cocina
    READY_TO_SERVE, // Pedido listo para ser llevado a la mesa
    SERVED,         // Pedido entregado al cliente
    PAID,           // Pedido pagado
    CANCELLED       // Pedido cancelado
}
