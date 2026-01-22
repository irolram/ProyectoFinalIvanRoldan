package com.example.proyectofinalivanroldan.dominio.model

import com.example.proyectofinalivanroldan.util.Roles

data class Usuario(
    val id: String,
    val username: String, // Lo usaremos para el campo "Usuario" del login
    val nombre: String,   // Nombre completo de la persona
    val password: String, // ¡Imprescindible para validar el acceso!
    val rol: Roles         // Usamos el enum para evitar errores de escritura
)