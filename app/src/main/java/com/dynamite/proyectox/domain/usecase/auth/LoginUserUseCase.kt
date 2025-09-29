package com.dynamite.proyectox.domain.usecase.auth

import com.dynamite.proyectox.domain.model.User
import com.dynamite.proyectox.domain.repository.UserRepository

class LoginUserUseCase(
    private val userRepository: UserRepository
) {
    // Aquí podríamos añadir lógica de validación de formato de username/password
    // antes de pasarlo al repositorio.
    // También podríamos manejar la lógica de hashing aquí si no se hace en la capa de datos.
    suspend operator fun invoke(username: String, passwordAttempt: String): Result<User> {
        // En un escenario real, el passwordAttempt debería ser hasheado
        // aquí o en la capa de datos antes de compararlo o enviarlo.
        // Por ahora, asumimos que el repositorio maneja la lógica de
        // comparación con el passwordHash almacenado.
        val user = userRepository.login(username, passwordAttempt /* Idealmente hasheado */)
        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("Login failed: Invalid username or password"))
        }
    }
}


//Estos son algunos ejemplos iniciales de casos de uso. Podríamos seguir creando más, como:
//•PlaceOrderUseCase
//•GetOrdersUseCase
//•AddProductUseCase (para el admin)
//•UpdateOrderStatusUseCase
