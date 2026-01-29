package com.example.proyectofinalivanroldan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinalivanroldan.data.repository.UsuarioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // El StateFlow que observará la UI de Compose
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, pass: String) {
        if (username.isBlank() || pass.isBlank()) {
            _loginState.value = LoginState.Error("Rellena todos los campos")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            // Simulamos un pequeño delay de 500ms para que la UI no parpadee
            // y se vea que la app está "pensando" (leyendo el JSON)
            delay(500)

            val usuario = repository.getAll().find {
                it.username == username && it.password == pass
            }

            if (usuario != null) {
                _loginState.value = LoginState.Success(usuario)
            } else {
                _loginState.value = LoginState.Error("Usuario o contraseña incorrectos")
            }
        }
    }

    // Para resetear el estado si el usuario vuelve atrás
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}