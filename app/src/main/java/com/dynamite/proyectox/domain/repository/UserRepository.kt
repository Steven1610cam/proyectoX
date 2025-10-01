package com.dynamite.proyectox.domain.repository

import com.dynamite.proyectox.domain.model.User
import com.dynamite.proyectox.domain.model.UserRole

interface UserRepository {
    // Podríamos usar un Result<User, ErrorType> para un mejor manejo de errores
    suspend fun login(username: String, passwordHash: String): User?
    suspend fun getUserById(userId: String): User?
    // Funciones de admin
    suspend fun createUser(user: User): Boolean
    suspend fun getUsers(role: UserRole? = null): List<User>
}