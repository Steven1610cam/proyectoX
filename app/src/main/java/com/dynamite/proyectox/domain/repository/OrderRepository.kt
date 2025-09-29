package com.dynamite.proyectox.domain.repository

import com.dynamite.proyectox.domain.model.Order
import com.dynamite.proyectox.domain.model.OrderStatus

interface OrderRepository {
    suspend fun getOrders(
        status: OrderStatus? = null,
        tableId: String? = null,
        waiterId: String? = null
    ): List<Order>

    suspend fun getOrderById(orderId: String): Order?
    suspend fun placeOrder(order: Order): Boolean
    suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Boolean
    // Podríamos añadir más funciones como getOrdersForTable, getOrdersForWaiter, etc.
}