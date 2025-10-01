package com.dynamite.proyectox.domain.model

data class User(
    val id: String, // Podría ser un UUID o un ID numérico
    val username: String,
    // Consideraremos el hashing de contraseñas más adelante en la capa de datos
    val passwordHash: String,
    val role: UserRole
)