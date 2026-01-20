package com.example.proyectofinalivanroldan.data.local.json

import com.example.proyectofinalivanroldan.dominio.model.Usuario
import com.google.gson.Gson

class UsuarioDataSource(
    private val fileManager: JsonFileManager,
    private val gson: Gson
) {

    fun getAll(): List<Usuario> {
        val json = fileManager.read("usuarios.json")
        return gson.fromJson(json, Array<Usuario>::class.java).toList()
    }

    fun saveAll(usuarios: List<Usuario>) {
        val json = gson.toJson(usuarios)
        fileManager.write("usuarios.json", json)
    }
}
