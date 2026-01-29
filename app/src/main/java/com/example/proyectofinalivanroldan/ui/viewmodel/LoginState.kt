package com.example.proyectofinalivanroldan.ui.viewmodel

import com.example.proyectofinalivanroldan.dominio.model.Usuario

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val usuario: Usuario) : LoginState()
    data class Error(val mensaje: String) : LoginState()
}