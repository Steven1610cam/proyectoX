package com.dynamite.proyectox.domain.usecase.product

import com.dynamite.proyectox.domain.model.Product
import com.dynamite.proyectox.domain.repository.ProductRepository

class GetProductsUseCase(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(category: String? = null): Result<List<Product>> {
        return try {
            val products = productRepository.getProducts(category)
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}