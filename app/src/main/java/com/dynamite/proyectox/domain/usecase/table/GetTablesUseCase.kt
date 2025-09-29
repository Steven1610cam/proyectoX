package com.dynamite.proyectox.domain.usecase.table

import com.dynamite.proyectox.domain.model.Table
import com.dynamite.proyectox.domain.model.TableStatus
import com.dynamite.proyectox.domain.repository.TableRepository

class GetTablesUseCase(
    private val tableRepository: TableRepository
) {
    suspend operator fun invoke(status: TableStatus? = null): Result<List<Table>> {
        return try {
            val tables = tableRepository.getTables(status)
            Result.success(tables)
        } catch (e: Exception) {
            // Aquí podrías loggear el error o mapearlo a un error de dominio más específico
            Result.failure(e)
        }
    }
}
