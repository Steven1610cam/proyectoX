package com.dynamite.proyectox.data.repository

import com.dynamite.proyectox.domain.model.Product
import com.dynamite.proyectox.domain.repository.ProductRepository
import java.math.BigDecimal

// TODO: Reemplazar con implementación de Room
class ProductRepositoryImpl : ProductRepository {

    private val products = mutableListOf<Product>(
        // BURGERS
        Product(
            id = "burg001",
            name = "Hamburguesa Clásica",
            description = "Carne de res, lechuga, tomate, queso y salsa especial.",
            price = BigDecimal("15.00"),
            category = "BURGERS",
            imageUrl = null
        ),
        Product(
            id = "burg002",
            name = "Hamburguesa Doble Queso",
            description = "Doble carne, doble queso cheddar, pepinillos y mostaza.",
            price = BigDecimal("22.50"),
            category = "BURGERS",
            imageUrl = null
        ),
        Product(
            id = "burg003",
            name = "Hamburguesa BBQ Extrema",
            description = "Carne de res, aros de cebolla, bacon crujiente, queso suizo y salsa BBQ.",
            price = BigDecimal("25.00"),
            category = "BURGERS",
            imageUrl = null
        ),
        Product(
            id = "burg004",
            name = "Hamburguesa de Pollo Crispy",
            description = "Pechuga de pollo empanizada, lechuga, tomate y mayonesa de ajo.",
            price = BigDecimal("18.00"),
            category = "BURGERS",
            imageUrl = null
        ),

        // BEBIDAS
        Product(
            id = "beb001",
            name = "Gaseosa Grande",
            description = "Vaso grande de tu gaseosa preferida (Coca-Cola, Pepsi, Sprite).",
            price = BigDecimal("7.00"),
            category = "BEBIDAS",
            imageUrl = null
        ),
        Product(
            id = "beb002",
            name = "Jugo Natural de Naranja",
            description = "Jugo recién exprimido, lleno de vitaminas.",
            price = BigDecimal("9.00"),
            category = "BEBIDAS",
            imageUrl = null
        ),
        Product(
            id = "beb003",
            name = "Agua Embotellada",
            description = "Agua pura y refrescante.",
            price = BigDecimal("5.00"),
            category = "BEBIDAS",
            imageUrl = null
        ),
        Product(
            id = "beb004",
            name = "Cerveza Nacional",
            description = "Botella de cerveza local.",
            price = BigDecimal("10.00"),
            category = "BEBIDAS",
            imageUrl = null
        ),
        Product(
            id = "beb005",
            name = "Limonada con Hierbabuena",
            description = "Refrescante limonada con un toque de hierbabuena.",
            price = BigDecimal("8.50"),
            category = "BEBIDAS",
            imageUrl = null
        ),

        // ENTRADAS
        Product(
            id = "ent001",
            name = "Papas Fritas Clásicas",
            description = "Porción generosa de papas fritas crujientes.",
            price = BigDecimal("10.00"),
            category = "ENTRADAS",
            imageUrl = null
        ),
        Product(
            id = "ent002",
            name = "Aros de Cebolla",
            description = "Aros de cebolla rebozados, servidos con salsa golf.",
            price = BigDecimal("12.00"),
            category = "ENTRADAS",
            imageUrl = null
        ),
        Product(
            id = "ent003",
            name = "Nachos con Queso y Jalapeños",
            description = "Totopos de maíz bañados en queso cheddar fundido y jalapeños.",
            price = BigDecimal("18.00"),
            category = "ENTRADAS",
            imageUrl = null
        ),
        Product(
            id = "ent004",
            name = "Palitos de Mozzarella",
            description = "Dedos de queso mozzarella empanizados, servidos con salsa marinara.",
            price = BigDecimal("16.00"),
            category = "ENTRADAS",
            imageUrl = null
        ),

        // SOPAS
        Product(
            id = "sop001",
            name = "Sopa de Pollo Casera",
            description = "Caldo reconfortante con pollo desmenuzado y verduras.",
            price = BigDecimal("14.00"),
            category = "SOPAS",
            imageUrl = null
        ),
        Product(
            id = "sop002",
            name = "Crema de Tomate",
            description = "Suave crema de tomate con crutones al ajo.",
            price = BigDecimal("12.00"),
            category = "SOPAS",
            imageUrl = null
        ),

        // POSTRES
        Product(
            id = "pos001",
            name = "Torta de Chocolate Húmeda",
            description = "Porción generosa de torta de chocolate con fudge.",
            price = BigDecimal("15.00"),
            category = "POSTRES",
            imageUrl = null
        ),
        Product(
            id = "pos002",
            name = "Cheesecake de Fresa",
            description = "Clásico cheesecake cremoso con salsa de fresas.",
            price = BigDecimal("16.00"),
            category = "POSTRES",
            imageUrl = null
        ),
        Product(
            id = "pos003",
            name = "Helado Artesanal (2 bolas)",
            description = "Elige dos sabores de nuestro helado artesanal.",
            price = BigDecimal("10.00"),
            category = "POSTRES",
            imageUrl = null
        ),

        // OTROS (Ejemplos variados)
        Product(
            id = "otr001",
            name = "Ensalada César con Pollo",
            description = "Lechuga romana, pollo a la parrilla, crutones, queso parmesano y aderezo César.",
            price = BigDecimal("20.00"),
            category = "OTROS", // O podría ser "ENSALADAS" si creas esa categoría
            imageUrl = null
        ),
        Product(
            id = "otr002",
            name = "Sándwich Club",
            description = "Triple capa de pavo, jamón, bacon, lechuga, tomate y mayonesa.",
            price = BigDecimal("23.00"),
            category = "OTROS", // O podría ser "SANDWICHES"
            imageUrl = null
        )
    )

    override suspend fun getProducts(category: String?): List<Product> {
        return if (category == null || category.equals("TODOS", ignoreCase = true) || category.isBlank()) { // Considerar "TODOS" o vacío como todas las categorías
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
