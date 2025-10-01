package com.dynamite.proyectox.data.repository

import com.dynamite.proyectox.domain.model.Table
import com.dynamite.proyectox.domain.model.TableStatus
import com.dynamite.proyectox.domain.repository.TableRepository

// TODO: Reemplazar con implementación de Room
class TableRepositoryImpl : TableRepository {

    private val tables = mutableListOf<Table>(
        // Datos de ejemplo iniciales
        Table(id = "table001", number = 1, status = TableStatus.FREE, currentOrderId = null),
        Table(id = "table002", number = 2, status = TableStatus.FREE, currentOrderId = null),
        Table(
            id = "table003",
            number = 3,
            status = TableStatus.OCCUPIED,
            currentOrderId = "order001"
        ), // Mesa ocupada con un pedido
        Table(id = "table004", number = 4, status = TableStatus.FREE, currentOrderId = null),
        Table(
            id = "table005",
            number = 5,
            status = TableStatus.BILL_REQUESTED,
            currentOrderId = "order002"
        ) // Mesa que pidió la cuenta
    )

    override suspend fun getTables(status: TableStatus?): List<Table> {
        return if (status == null) {
            tables.toList()
        } else {
            tables.filter { it.status == status }
        }
    }

    override suspend fun getTableById(tableId: String): Table? {
        return tables.find { it.id == tableId }
    }

    override suspend fun addTable(table: Table): Boolean {
        if (tables.any { it.id == table.id || it.number == table.number }) {
            return false // Mesa ya existe (por ID o número)
        }
        tables.add(table)
        return true
    }

    override suspend fun updateTableStatus(
        tableId: String,
        newStatus: TableStatus,
        orderId: String?
    ): Boolean {
        val index = tables.indexOfFirst { it.id == tableId }
        return if (index != -1) {
            val oldTable = tables[index]
            tables[index] = oldTable.copy(
                status = newStatus,
                // Solo actualiza currentOrderId si el nuevo estado es OCCUPIED o si se está limpiando.
                // Si se pasa un orderId, se usa; si no, y el estado cambia a FREE, se limpia.
                currentOrderId = when (newStatus) {
                    TableStatus.OCCUPIED -> orderId
                        ?: oldTable.currentOrderId // Mantiene el actual si no se provee uno nuevo
                    TableStatus.FREE -> null
                    TableStatus.BILL_REQUESTED -> oldTable.currentOrderId // Mantiene el orderId cuando se pide la cuenta
                    else -> oldTable.currentOrderId
                }
            )
            true
        } else {
            false // Mesa no encontrada
        }
    }
}