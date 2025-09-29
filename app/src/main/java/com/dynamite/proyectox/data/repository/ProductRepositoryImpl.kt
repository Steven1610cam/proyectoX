package com.dynamite.proyectox.data.repository

import com.dynamite.proyectox.domain.model.Product
import com.dynamite.proyectox.domain.repository.ProductRepository
import java.math.BigDecimal

// TODO: Reemplazar con implementación de Room
class ProductRepositoryImpl : ProductRepository {

    private val products = mutableListOf<Product>(
        // Datos de ejemplo iniciales
        Product(
            id = "prod001",
            name = "Hamburguesa Clásica",
            description = "Carne de res, lechuga, tomate, queso y salsa especial.",
            price = BigDecimal("15.00"),
            category = "Hamburguesas",
            imageUrl = null // Podrías añadir URLs de imágenes de ejemplo más tarde
        ),
        Product(
            id = "prod002",
            name = "Papas Fritas Medianas",
            description = "Porción mediana de papas fritas crujientes.",
            price = BigDecimal("7.50"),
            category = "Acompañamientos",
            imageUrl = null
        ),
        Product(
            id = "prod003",
            name = "Gaseosa Grande",
            description = "Vaso grande de tu gaseosa preferida.",
            price = BigDecimal("5.00"),
            category = "Bebidas",
            imageUrl = null
        ),
        Product(
            id = "prod004",
            name = "Pizza Personal",
            description = "Pizza individual con tus ingredientes favoritos.",
            price = BigDecimal("20.00"),
            category = "Pizzas",
            imageUrl = null
        )
    )

    override suspend fun getProducts(category: String?): List<Product> {
        return if (category == null) {
            products.toList()
        } else {
            products.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    override suspend fun getProductById(productId: String): Product? {
        return products.find { it.id == productId }
    }

    override suspend fun addProduct(product: Product): Boolean {
        if (products.any {
                it.id == product.id || it.name.equals(
                    product.name,
                    ignoreCase = true
                )
            }) {
            return false // Producto ya existe (por ID o nombre)
        }
        products.add(product)
        return true
    }

    override suspend fun updateProduct(product: Product): Boolean {
        val index = products.indexOfFirst { it.id == product.id }
        return if (index != -1) {
            products[index] = product
            true
        } else {
            false // Producto no encontrado
        }
    }

    override suspend fun deleteProduct(productId: String): Boolean {
        return products.removeIf { it.id == productId }
    }
}