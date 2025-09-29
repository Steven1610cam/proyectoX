package com.dynamite.proyectox.presentation.order

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamite.proyectox.ARG_TABLE_NUMBER // Importa la constante de MainActivity
import com.dynamite.proyectox.common.Resource
import com.dynamite.proyectox.domain.model.Product
import com.dynamite.proyectox.domain.usecase.product.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _tableNumber = mutableStateOf<Int?>(null)
    val tableNumber: State<Int?> = _tableNumber

    private val _productsState = mutableStateOf<Resource<List<Product>>>(Resource.Loading())
    val productsState: State<Resource<List<Product>>> = _productsState

    // Futuros estados para la UI de OrderScreen
    // private val _selectedCategory = mutableStateOf<String?>(null)
    // val selectedCategory: State<String?> = _selectedCategory

    // private val _searchQuery = mutableStateOf("")
    // val searchQuery: State<String> = _searchQuery

    init {
        savedStateHandle.get<Int>(ARG_TABLE_NUMBER)?.let { number ->
            _tableNumber.value = number
        }
        loadProducts() // Cargar todos los productos inicialmente
    }

    fun loadProducts(category: String? = null) {
        viewModelScope.launch {
            _productsState.value = Resource.Loading()
            try {
                val result = getProductsUseCase(category = category) // Pasar la categoría si existe
                if (result.isSuccess) {
                    _productsState.value = Resource.Success(result.getOrDefault(emptyList()))
                } else {
                    _productsState.value = Resource.Error(
                        result.exceptionOrNull()?.message ?: "Error desconocido al cargar productos"
                    )
                }
            } catch (e: Exception) {
                _productsState.value = Resource.Error(
                    e.message ?: "Error inesperado al cargar productos"
                )
            }
        }
    }

    // Funciones futuras para manejar la selección de categoría, búsqueda, etc.
    // fun onCategorySelected(category: String) { ... }
    // fun onSearchQueryChanged(query: String) { ... }
}
