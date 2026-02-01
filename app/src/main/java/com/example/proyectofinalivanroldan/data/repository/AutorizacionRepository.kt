package com.example.proyectofinalivanroldan.data.repository

class AutorizacionRepository(private val vinculoRepo: IVinculoRepo) : IAutorizacionRepo {

    override fun estaAutorizado(tutorId: String, alumnoId: String): Boolean {

        return vinculoRepo.getAll().any { it.idTutor == tutorId && it.idAlumno == alumnoId }
    }
}
