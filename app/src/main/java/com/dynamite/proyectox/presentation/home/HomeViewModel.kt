package com.dynamite.proyectox.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamite.proyectox.common.Resource
import com.dynamite.proyectox.domain.model.Table // Import from domain.model
import com.dynamite.proyectox.domain.usecase.table.GetTablesUseCase // Import from domain.usecase.table
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch // Import for viewModelScope.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTablesUseCase: GetTablesUseCase
) : ViewModel() {

    private val _tablesState = mutableStateOf<Resource<List<Table>>>(Resource.Loading())
    val tablesState: State<Resource<List<Table>>> = _tablesState

    init {
        loadTables()
    }

    private fun loadTables() {
        viewModelScope.launch {
            _tablesState.value = Resource.Loading() // Set loading state
            try {
                // Llama al caso de uso. El parámetro status es opcional,
                // si lo omites, debería devolver todas las mesas según tu implementación.
                val result = getTablesUseCase() // Llama sin argumentos por ahora para obtener todas las mesas
                if (result.isSuccess) {
                    _tablesState.value = Resource.Success(result.getOrDefault(emptyList()))
                } else {
                    _tablesState.value = Resource.Error(
                        result.exceptionOrNull()?.message ?: "Error desconocido al cargar las mesas"
                    )
                }
            } catch (e: Exception) {
                // Captura cualquier otra excepción inesperada
                _tablesState.value = Resource.Error(e.message ?: "Error inesperado al cargar las mesas")
            }
        }
    }
}
