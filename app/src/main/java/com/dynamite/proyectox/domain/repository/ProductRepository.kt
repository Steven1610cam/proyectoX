package com.dynamite.proyectox.domain.repository

import com.dynamite.proyectox.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(category: String? = null): List<Product>
    suspend fun getProductById(productId: String): Product?

    // Funciones de admin
    suspend fun addProduct(product: Product): Boolean
    suspend fun updateProduct(product: Product): Boolean
    suspend fun deleteProduct(productId: String): Boolean
}