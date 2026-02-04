package com.example.proyectofinalivanroldan.data.repository

import android.content.Context
import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


/**
 * Repositorio encargado de la gestión de persistencia de los datos de alumnos.
 * * Implementa una estrategia de almacenamiento **Offline First** mediante el uso de archivos
 * JSON en el almacenamiento interno de la aplicación. Utiliza la librería [Gson] para
 * la serialización/deserialización de objetos y garantiza la integridad de la información
 * durante las operaciones CRUD.
 */
class AlumnoRepository(private val context: Context) : IAlumnoRepo {

    private val gson = Gson()
    private val fileName = "alumnos.json"
    private val file = File(context.filesDir, fileName)

    override fun getAll(): List<Alumno> {
        if (!file.exists()) return emptyList()
        return try {
            val jsonString = file.readText()
            val type = object : TypeToken<List<Alumno>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun getAlumnoById(id: String): Alumno? {
        return getAll().find { it.id == id }
    }

    override fun addAlumno(alumno: Alumno) {
        val lista = getAll().toMutableList()
        lista.add(alumno)
        guardarArchivo(lista)
    }

    override fun updateAlumno(alumno: Alumno) {
        val lista = getAll().toMutableList()
        val index = lista.indexOfFirst { it.id == alumno.id }
        if (index != -1) {
            lista[index] = alumno
            guardarArchivo(lista)
        }
    }

    override fun deleteAlumno(id: String) {
        val lista = getAll().toMutableList()
        if (lista.removeAll { it.id == id }) {
            guardarArchivo(lista)
        }
    }

    private fun guardarArchivo(lista: List<Alumno>) {
        try {
            val jsonString = gson.toJson(lista)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}