package com.example.proyectofinalivanroldan.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectofinalivanroldan.data.repository.AlumnoRepository
import com.example.proyectofinalivanroldan.data.repository.UsuarioRepository
import com.example.proyectofinalivanroldan.data.repository.VinculoRepository

class AdminViewModelFactory(
    private val userRepo: UsuarioRepository,
    private val alumnoRepo: AlumnoRepository,
    private val vinculoRepo: VinculoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            return AdminViewModel(userRepo, alumnoRepo, vinculoRepo) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida: ${modelClass.name}")
    }
}