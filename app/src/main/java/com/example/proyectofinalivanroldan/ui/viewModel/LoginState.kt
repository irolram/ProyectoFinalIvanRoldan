package com.example.proyectofinalivanroldan.ui.viewModel

import com.example.proyectofinalivanroldan.dominio.model.Usuario

sealed class LoginState {
    object Idle : LoginState()         // El usuario aún no ha pulsado el botón
    object Loading : LoginState()      // Estamos leyendo el JSON
    data class Success(val usuario: Usuario) : LoginState() // Login correcto
    data class Error(val mensaje: String) : LoginState()    // Credenciales mal o fallo de archivo
}