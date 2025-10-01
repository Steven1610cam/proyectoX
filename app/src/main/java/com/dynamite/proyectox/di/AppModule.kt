package com.dynamite.proyectox.di

import com.dynamite.proyectox.data.repository.OrderRepositoryImpl
import com.dynamite.proyectox.data.repository.ProductRepositoryImpl
import com.dynamite.proyectox.data.repository.TableRepositoryImpl
import com.dynamite.proyectox.data.repository.UserRepositoryImpl
import com.dynamite.proyectox.domain.repository.OrderRepository
import com.dynamite.proyectox.domain.repository.ProductRepository
import com.dynamite.proyectox.domain.repository.TableRepository
import com.dynamite.proyectox.domain.repository.UserRepository
import com.dynamite.proyectox.domain.usecase.auth.LoginUserUseCase
import com.dynamite.proyectox.domain.usecase.product.GetProductsUseCase
import com.dynamite.proyectox.domain.usecase.table.GetTablesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Define el alcance de las dependencias (App-wide)
object AppModule {

    // --- Repositories ---
    @Provides
    @Singleton // Única instancia del repositorio en toda la app
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl() // Nuestra implementación simulada
    }

    @Provides
    @Singleton
    fun provideProductRepository(): ProductRepository {
        return ProductRepositoryImpl() // Nuestra implementación simulada
    }

    @Provides
    @Singleton
    fun provideTableRepository(): TableRepository {
        return TableRepositoryImpl() // Nuestra implementación simulada
    }

    @Provides
    @Singleton
    fun provideOrderRepository(): OrderRepository {
        return OrderRepositoryImpl() // Nuestra implementación simulada
    }

    // --- Use Cases ---
    // Los casos de uso suelen ser stateless, por lo que @Singleton podría no ser estrictamente necesario
    // a menos que tengan algún estado interno o su creación sea costosa.
    // Por simplicidad y consistencia, los marcaremos como Singleton por ahora.

    @Provides
    @Singleton
    fun provideLoginUserUseCase(userRepository: UserRepository): LoginUserUseCase {
        return LoginUserUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetTablesUseCase(tableRepository: TableRepository): GetTablesUseCase {
        return GetTablesUseCase(tableRepository)
    }

    @Provides
    @Singleton
    fun provideGetProductsUseCase(productRepository: ProductRepository): GetProductsUseCase {
        return GetProductsUseCase(productRepository)
    }

    // Aquí añadirías providers para otros casos de uso a medida que los crees
    // Ejemplo:
    // @Provides
    // @Singleton
    // fun providePlaceOrderUseCase(orderRepository: OrderRepository, tableRepository: TableRepository): PlaceOrderUseCase {
    //     return PlaceOrderUseCase(orderRepository, tableRepository)
    // }
}
