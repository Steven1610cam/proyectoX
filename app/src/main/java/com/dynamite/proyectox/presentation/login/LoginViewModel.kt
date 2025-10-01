package com.dynamite.proyectox.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamite.proyectox.domain.usecase.auth.LoginUserUseCase
import com.dynamite.proyectox.common.Resource
// Asegúrate de que User también esté importado si lo necesitas directamente aquí, aunque para loginState<Boolean> no es estrictamente necesario.
// import com.dynamite.proyectox.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch // Importante para viewModelScope.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {

    private val _loginState = mutableStateOf<Resource<Boolean>>(Resource.Success(false)) // Inicialmente no logueado
    val loginState: State<Resource<Boolean>> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading() // Indicar que la operación está en curso
            try {
                val result = loginUserUseCase(username = email, passwordAttempt = password)
                if (result.isSuccess) {
                    // val user = result.getOrNull() // Puedes obtener el usuario si lo necesitas
                    _loginState.value = Resource.Success(true) // Login exitoso
                } else {
                    _loginState.value = Resource.Error(
                        result.exceptionOrNull()?.message ?: "Error desconocido en el login"
                    )
                }
            } catch (e: Exception) {
                // Capturar otras excepciones inesperadas durante la llamada al caso de uso
                _loginState.value = Resource.Error(e.message ?: "Error inesperado")
            }
        }
    }
}
