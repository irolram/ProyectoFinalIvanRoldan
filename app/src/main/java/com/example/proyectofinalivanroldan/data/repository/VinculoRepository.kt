package com.example.proyectofinalivanroldan.data.repository


import android.content.Context
import com.example.proyectofinalivanroldan.dominio.model.Vinculo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

/**
 * Repositorio encargado de gestionar la lógica de asociación entre tutores y alumnos.
 * * Administra la persistencia de relaciones N:M (muchos a muchos) mediante un sistema
 * de archivos JSON local. Su función principal es validar y filtrar qué alumnos
 * están autorizados para ser recogidos por cada tutor, sirviendo como núcleo de
 * seguridad para la generación y validación de códigos QR en el sistema.
 */
class VinculoRepository(private val context: Context) : IVinculoRepo {

    private val gson = Gson()
    private val fileName = "vinculos.json"
    private val file = File(context.filesDir, fileName)

    override fun getAll(): List<Vinculo> {
        if (!file.exists()) return emptyList()
        return try {
            val jsonString = file.readText()
            val type = object : TypeToken<List<Vinculo>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun addVinculo(vinculo: Vinculo) {
        val lista = getAll().toMutableList()
        // Evitamos duplicados
        if (!lista.any { it.idTutor == vinculo.idTutor && it.idAlumno == vinculo.idAlumno }) {
            lista.add(vinculo)
            guardarArchivo(lista)
        }
    }

    override fun deleteVinculo(idTutor: String, idAlumno: String) {
        val lista = getAll().toMutableList()
        if (lista.removeAll { it.idTutor == idTutor && it.idAlumno == idAlumno }) {
            guardarArchivo(lista)
        }
    }

    override fun getAlumnosByTutor(idTutor: String): List<String> {
        return getAll().filter { it.idTutor == idTutor }.map { it.idAlumno }
    }

    private fun guardarArchivo(lista: List<Vinculo>) {
        try {
            val jsonString = gson.toJson(lista)
            file.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}