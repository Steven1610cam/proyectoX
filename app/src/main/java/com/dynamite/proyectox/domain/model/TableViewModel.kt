package com.dynamite.proyectox.presentation.tables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamite.proyectox.domain.model.Table
import com.dynamite.proyectox.domain.repository.TableRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Si estás usando Hilt, descomenta la siguiente línea y la inyección en el constructor:
// import dagger.hilt.android.lifecycle.HiltViewModel
// import javax.inject.Inject

// @HiltViewModel
class TableViewModel /* @Inject constructor */(
    private val tableRepository: TableRepository
) : ViewModel() {

    private val _tables = MutableStateFlow<List<Table>>(emptyList())
    val tables: StateFlow<List<Table>> = _tables

    // Podrías añadir un StateFlow para el estado de carga (Loading, Success, Error)
    // private val _uiState = MutableStateFlow<TablesUiState>(TablesUiState.Loading)
    // val uiState: StateFlow<TablesUiState> = _uiState

    init {
        loadTables()
    }

    fun loadTables() {
        viewModelScope.launch {
            // Aquí podrías manejar el estado de carga y errores
            // _uiState.value = TablesUiState.Loading
            try {
                _tables.value = tableRepository.getTables()
                // _uiState.value = TablesUiState.Success(tableRepository.getTables())
            } catch (e: Exception) {
                // _uiState.value = TablesUiState.Error(e.message ?: "Unknown error")
                // Maneja el error, por ejemplo, mostrando un mensaje al usuario
                _tables.value = emptyList() // O mantener la lista anterior
            }
        }
    }
}

// Opcional: Define estados de UI si quieres manejar la carga y errores de forma más explícita
// sealed interface TablesUiState {
//     object Loading : TablesUiState
//     data class Success(val tables: List<Table>) : TablesUiState
//     data class Error(val message: String) : TablesUiState
// }