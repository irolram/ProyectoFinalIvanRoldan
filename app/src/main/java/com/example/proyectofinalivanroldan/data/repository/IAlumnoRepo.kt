package com.example.proyectofinalivanroldan.data.repository

import com.example.proyectofinalivanroldan.dominio.model.Alumno

interface IAlumnoRepo {
    fun getAll(): List<Alumno>
    fun getAlumnoById(id: String): Alumno?
    fun addAlumno(alumno: Alumno)
    fun updateAlumno(alumno: Alumno)
    fun deleteAlumno(id: String)
}