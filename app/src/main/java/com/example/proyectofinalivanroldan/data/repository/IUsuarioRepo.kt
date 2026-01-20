package com.example.proyectofinalivanroldan.data.repository

import com.example.proyectofinalivanroldan.dominio.model.Usuario

interface IUsuarioRepo {

    fun getUsuarioById(id: String): Usuario?
    fun getAll(): List<Usuario>
    fun addUsuario(usuario: Usuario)
    fun updateUsuario(usuario: Usuario)
    fun deleteUsuario(id: String)
}