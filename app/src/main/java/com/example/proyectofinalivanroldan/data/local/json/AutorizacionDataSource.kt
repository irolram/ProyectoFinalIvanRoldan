package com.example.proyectofinalivanroldan.data.local.json

import com.example.proyectofinalivanroldan.dominio.model.Autorizacion
import com.google.gson.Gson

class AutorizacionDataSource(
    private val fileManager: JsonFileManager,
    private val gson: Gson
) {

    private val fileName = "autorizaciones.json"

    fun getAll(): List<Autorizacion> {
        val json = fileManager.read(fileName)

        return gson.fromJson(
            json,
            Array<Autorizacion>::class.java
        ).toList()
    }

    fun saveAll(autorizaciones: List<Autorizacion>) {
        val json = gson.toJson(autorizaciones)
        fileManager.write(fileName, json)
    }
}
