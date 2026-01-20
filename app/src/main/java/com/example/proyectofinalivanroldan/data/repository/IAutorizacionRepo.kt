package com.example.proyectofinalivanroldan.data.repository

interface IAutorizacionRepo {
    fun estaAutorizado(tutorId: String, alumnoId: String): Boolean

}