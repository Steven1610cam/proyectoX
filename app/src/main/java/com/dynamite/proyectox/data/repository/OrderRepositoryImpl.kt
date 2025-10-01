package com.dynamite.proyectox.data.repository

import com.dynamite.proyectox.domain.model.Order
import com.dynamite.proyectox.domain.model.OrderItem
import com.dynamite.proyectox.domain.model.OrderStatus
import com.dynamite.proyectox.domain.repository.OrderRepository
import java.math.BigDecimal
import java.util.Date

// TODO: Reemplazar con implementación de Room
class OrderRepositoryImpl : OrderRepository {

    private val orders = mutableListOf<Order>(
        // Datos de ejemplo iniciales
        Order(
            id = "order001",
            tableId = "table003", // Mesa que estaba ocupada
            waiterId = "waiter01", // <--- CORRECCIÓN AQUÍ
            items = listOf(
                OrderItem(
                    id = "item001_1",
                    productId = "prod001",
                    productName = "Hamburguesa Clásica",
                    quantity = 1,
                    unitPrice = BigDecimal("15.00"),
                    notes = "Extra queso"
                ),
                OrderItem(
                    id = "item001_2",
                    productId = "prod003",
                    productName = "Gaseosa Grande",
                    quantity = 1,
                    unitPrice = BigDecimal("5.00"),
                    notes = null
                )
            ),
            status = OrderStatus.PREPARING,
            orderTimestamp = Date(System.currentTimeMillis() - 1000 * 60 * 15), // Hace 15 mins
            lastUpdateTimestamp = Date(System.currentTimeMillis() - 1000 * 60 * 5) // Hace 5 mins
        ),
        Order(
            id = "order002",
            tableId = "table005", // Mesa que pidió la cuenta
            waiterId = "waiter01",
            items = listOf(
                OrderItem(
                    id = "item002_1",
                    productId = "prod004",
                    productName = "Pizza Personal",
                    quantity = 2,
                    unitPrice = BigDecimal("20.00"),
                    notes = "Sin champiñones en una"
                )
            ),
            status = OrderStatus.SERVED, // Ya se sirvió, esperando pago
            orderTimestamp = Date(System.currentTimeMillis() - 1000 * 60 * 45), // Hace 45 mins
            lastUpdateTimestamp = Date(System.currentTimeMillis() - 1000 * 60 * 20) // Hace 20 mins
        )
    )

    override suspend fun getOrders(
        status: OrderStatus?,
        tableId: String?,
        waiterId: String?
    ): List<Order> {
        var filteredOrders = orders.toList()
        if (status != null) {
            filteredOrders = filteredOrders.filter { it.status == status }
        }
        if (tableId != null) {
            filteredOrders = filteredOrders.filter { it.tableId == tableId }
        }
        if (waiterId != null) {
            filteredOrders = filteredOrders.filter { it.waiterId == waiterId }
        }
        return filteredOrders
    }

    override suspend fun getOrderById(orderId: String): Order? {
        return orders.find { it.id == orderId }
    }

    override suspend fun placeOrder(order: Order): Boolean {
        if (orders.any { it.id == order.id }) {
            return false // Pedido ya existe (por ID)
        }
        orders.add(order)
        return true
    }

    override suspend fun updateOrderStatus(orderId: String, newStatus: OrderStatus): Boolean {
        val index = orders.indexOfFirst { it.id == orderId }
        return if (index != -1) {
            val oldOrder = orders[index]
            orders[index] = oldOrder.copy(
                status = newStatus,
                lastUpdateTimestamp = Date()
            )
            true
        } else {
            false // Pedido no encontrado
        }
    }
}