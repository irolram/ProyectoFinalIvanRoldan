package com.example.proyectofinalivanroldan.data.repository

import com.example.proyectofinalivanroldan.data.local.json.AutorizacionDataSource

class AutorizacionRepository(
    private val dataSource: AutorizacionDataSource
) : IAutorizacionRepo{

    override fun estaAutorizado(tutorId: String, alumnoId: String): Boolean {
        return dataSource.getAll()
            .any {
                it.tutorId == tutorId &&
                        it.alumnoId == alumnoId &&
                        it.autorizada
            }
    }
}
