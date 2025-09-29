package com.dynamite.proyectox.domain.model

import java.math.BigDecimal

data class Product(
    val id: String, // Podría ser un UUID
    val name: String,
    val description: String?,
    val price: BigDecimal, // Usamos BigDecimal para precisión monetaria
    val category: String, // Ejemplo: "Bebidas", "Platos Fuertes", "Postres"
    val imageUrl: String? // URL de la imagen del producto (opcional)
)