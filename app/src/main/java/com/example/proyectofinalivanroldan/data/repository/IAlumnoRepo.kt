package com.example.proyectofinalivanroldan.data.repository

import com.example.proyectofinalivanroldan.dominio.model.Alumno

interface IAlumnoRepo {

    fun getAlumnoById(id: String): Alumno?
    fun getAll(): List<Alumno>
    fun addAlumno(alumno: Alumno)
    fun updateAlumno(alumno: Alumno)
    fun deleteAlumno(id: String)
}