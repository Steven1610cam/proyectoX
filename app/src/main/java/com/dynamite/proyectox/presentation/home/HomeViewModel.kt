package com.dynamite.proyectox.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.dynamite.proyectox.common.Resource // Sigue siendo útil si en el futuro hay operaciones asíncronas
import com.dynamite.proyectox.domain.model.Table
import com.dynamite.proyectox.domain.model.TableStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID // Para generar IDs únicos para las mesas
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() { // Eliminado GetTablesUseCase

    private val _tablesState = mutableStateOf<Resource<List<Table>>>(Resource.Success(emptyList()))
    val tablesState: State<Resource<List<Table>>> = _tablesState

    private var nextTableNumber = 1

    init {
        // Iniciar con una mesa por defecto
        val initialTable = Table(
            id = UUID.randomUUID().toString(),
            number = nextTableNumber++,
            status = TableStatus.FREE,
            currentOrderId = null
        )
        _tablesState.value = Resource.Success(listOf(initialTable))
    }

    fun addNewTable() {
        val currentTables = (_tablesState.value as? Resource.Success)?.data ?: emptyList()
        val newTable = Table(
            id = UUID.randomUUID().toString(),
            number = nextTableNumber++,
            status = TableStatus.FREE,
            currentOrderId = null
        )
        _tablesState.value = Resource.Success(currentTables + newTable)
    }
}
