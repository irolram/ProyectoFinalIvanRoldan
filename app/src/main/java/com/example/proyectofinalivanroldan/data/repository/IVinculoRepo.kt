package com.example.proyectofinalivanroldan.data.repository


import com.example.proyectofinalivanroldan.dominio.model.Vinculo

interface IVinculoRepo {
    fun getAll(): List<Vinculo>
    fun addVinculo(vinculo: Vinculo)
    fun deleteVinculo(idTutor: String, idAlumno: String)
    fun getAlumnosByTutor(idTutor: String): List<String> // Devuelve IDs de alumnos
}