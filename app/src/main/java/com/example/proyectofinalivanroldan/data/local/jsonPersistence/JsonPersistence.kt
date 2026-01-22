package com.example.proyectofinalivanroldan.data.local.jsonPersistence

import android.content.Context
import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class JsonPersistence(private val context: Context) {
    private val gson = Gson()

    // Guardar la lista de alumnos
    fun guardarAlumnos(lista: List<Alumno>) {
        val jsonString = gson.toJson(lista)
        context.openFileOutput("alumnos.json", Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    // Lee los alumnos
    fun leerAlumnos(): List<Alumno> {
        val file = File(context.filesDir, "alumnos.json")
        if (!file.exists()) return emptyList()

        val jsonString = file.readText()
        val itemType = object : TypeToken<List<Alumno>>() {}.type
        return gson.fromJson(jsonString, itemType)
    }
}