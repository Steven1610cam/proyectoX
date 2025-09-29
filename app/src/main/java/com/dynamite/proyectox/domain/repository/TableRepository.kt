package com.dynamite.proyectox.domain.repository

import com.dynamite.proyectox.domain.model.Table
import com.dynamite.proyectox.domain.model.TableStatus

interface TableRepository {
    suspend fun getTables(status: TableStatus? = null): List<Table>
    suspend fun getTableById(tableId: String): Table?
    suspend fun addTable(table: Table): Boolean // Admin o sistema
    suspend fun updateTableStatus(
        tableId: String,
        newStatus: TableStatus,
        orderId: String? = null
    ): Boolean
}