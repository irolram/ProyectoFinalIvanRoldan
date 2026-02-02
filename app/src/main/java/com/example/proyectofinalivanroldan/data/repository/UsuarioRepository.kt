package com.example.proyectofinalivanroldan.data.repository

import android.content.Context
import com.example.proyectofinalivanroldan.dominio.model.Usuario
import com.example.proyectofinalivanroldan.util.Roles
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class UsuarioRepository(private val context: Context) : IUsuarioRepo {
    private val gson = Gson()
    private val fileName = "usuarios.json"
    private val file = File(context.filesDir, fileName)

    init {
        if (!file.exists()) {
            val adminDefecto = Usuario(
                id = "1",
                username = "admin",
                nombre = "Administrador",
                password = "admin",
                rol = Roles.ADMIN
            )
            addUsuario(adminDefecto)
        }
    }

    override fun getAll(): List<Usuario> {
        if (!file.exists()) {
            println("REPOSITORIO: El archivo no existe todavía.")
            return emptyList()
        }

        val jsonString = file.readText()

        println("REPOSITORIO_DATA: $jsonString")

        val type = object : TypeToken<List<Usuario>>() {}.type
        return gson.fromJson(jsonString, type) ?: emptyList()
    }

    override fun addUsuario(usuario: Usuario) {
        val lista = getAll().toMutableList()
        lista.add(usuario)
        guardarLista(lista)
    }

    override fun updateUsuario(usuario: Usuario) {
        val lista = getAll().toMutableList()
        val index = lista.indexOfFirst { it.id == usuario.id }
        if (index != -1) {
            lista[index] = usuario
            guardarLista(lista)
        }
    }

    override fun deleteUsuario(id: String) {
        val lista = getAll().toMutableList()
        if (lista.removeAll { it.id == id }) {
            guardarLista(lista)
        }
    }

    override fun getUsuarioById(id: String): Usuario? {
        return getAll().find { it.id == id }
    }

    // Función privada para evitar repetir código al escribir
    private fun guardarLista(lista: List<Usuario>) {
        try {
            val jsonString = gson.toJson(lista)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}