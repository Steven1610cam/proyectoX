package com.dynamite.proyectox.presentation.order

import com.dynamite.proyectox.common.Resource
import com.dynamite.proyectox.domain.model.Product

data class OrderScreenState(
    val tableNumber: Int? = null,
    val productsResource: Resource<List<Product>> = Resource.Loading(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val categories: List<String> = listOf("BURGERS", "BEBIDAS", "ENTRADAS", "SOPAS", "POSTRES", "OTROS"), // Ejemplo, podría venir de otro sitio
    val isLoading: Boolean = false // Para manejar el estado de carga general
)
