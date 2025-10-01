package com.dynamite.proyectox.presentation.order

// Imports para StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow // Para exponer como StateFlow
import kotlinx.coroutines.flow.update // Para actualizar StateFlow de forma segura

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamite.proyectox.ARG_TABLE_NUMBER
import com.dynamite.proyectox.common.Resource
import com.dynamite.proyectox.domain.model.Product
import com.dynamite.proyectox.domain.usecase.product.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Cambiado a MutableStateFlow
    private val _uiState = MutableStateFlow(OrderScreenState())
    // Expuesto como StateFlow (inmutable desde el exterior)
    val uiState: StateFlow<OrderScreenState> = _uiState.asStateFlow()

    init {
        val initialTableNumber = savedStateHandle.get<Int>(ARG_TABLE_NUMBER)
        // Usar .update para modificar el StateFlow de forma segura
        _uiState.update { currentState ->
            currentState.copy(
                tableNumber = initialTableNumber,
                selectedCategory = currentState.categories.firstOrNull()
            )
        }
        loadProducts(category = _uiState.value.selectedCategory)
    }

    fun loadProducts(category: String? = _uiState.value.selectedCategory) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, productsResource = Resource.Loading()) }
            try {
                val currentCategory = category ?: _uiState.value.selectedCategory
                val result = getProductsUseCase(category = currentCategory)

                if (result.isSuccess) {
                    _uiState.update {
                        it.copy(
                            productsResource = Resource.Success(result.getOrDefault(emptyList())),
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            productsResource = Resource.Error(
                                result.exceptionOrNull()?.message ?: "Error al cargar productos"
                            ),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        productsResource = Resource.Error(
                            e.message ?: "Error inesperado al cargar productos"
                        ),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadProducts(category = category)
    }

    // TODO: Función para añadir producto al carrito (actualizará algún estado del pedido)
    // fun onAddProductToCart(product: Product) { ... }
}
