package com.dynamite.proyectox.data.repository

import com.dynamite.proyectox.domain.model.User
import com.dynamite.proyectox.domain.model.UserRole
import com.dynamite.proyectox.domain.repository.UserRepository

// TODO: Reemplazar con implementación de Room y SharedPreferences para sesión
class UserRepositoryImpl : UserRepository {

    private val users = mutableListOf<User>(
        // Datos de ejemplo iniciales
        User("admin01", "admin", "adminpass", UserRole.ADMIN), // En un caso real, "adminpass" sería un hash
        User("waiter01", "mesero1", "meseropass", UserRole.WAITER)
    )

    override suspend fun login(username: String, passwordAttempt: String): User? {
        // Simulación de login. En un caso real, passwordAttempt se hashearía
        // y se compararía con el passwordHash almacenado.
        return users.find { it.username == username && it.passwordHash == passwordAttempt }
    }

    override suspend fun getUserById(userId: String): User? {
        return users.find { it.id == userId }
    }

    override suspend fun createUser(user: User): Boolean {
        if (users.any { it.username == user.username || it.id == user.id }) {
            return false // Usuario ya existe
        }
        users.add(user)
        return true
    }

    override suspend fun getUsers(role: UserRole?): List<User> {
        return if (role == null) {
            users.toList()
        } else {
            users.filter { it.role == role }
        }
    }
}


//Notas Importantes sobre este código:
//•TODO: He añadido un comentario TODO para recordarnos que esto es una implementación simulada y que necesitaremos reemplazarla.
//•Contraseñas: Las contraseñas están en texto plano aquí solo para la simulación. En una implementación real con Room, almacenaríamos hashes de contraseñas.
//•Simplicidad: La lógica es muy simple y directa para simular las operaciones.