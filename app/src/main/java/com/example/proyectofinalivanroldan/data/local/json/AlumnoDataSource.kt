package com.example.proyectofinalivanroldan.data.local.json

import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.google.gson.Gson

class AlumnoDataSource(private val fileManager: JsonFileManager,
                       private val gson: Gson
) {
    private val fileName = "alumnos.json"

    fun getAll(): List<Alumno> {
        val json = fileManager.read(fileName)

        return gson.fromJson(
            json,
            Array<Alumno>::class.java
        ).toList()
    }

    fun saveAll(alumnos: List<Alumno>) {
        val json = gson.toJson(alumnos)
        fileManager.write(fileName, json)
    }
}