package com.example.proyectofinalivanroldan.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectofinalivanroldan.data.repository.UsuarioRepository
import com.example.proyectofinalivanroldan.data.repository.AlumnoRepository
import com.example.proyectofinalivanroldan.data.repository.VinculoRepository
import com.example.proyectofinalivanroldan.dominio.model.Usuario
import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.example.proyectofinalivanroldan.dominio.model.Vinculo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdminViewModel(
    private val userRepo: UsuarioRepository,
    private val alumnoRepo: AlumnoRepository,
    private val vinculoRepo: VinculoRepository
) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios = _usuarios.asStateFlow()

    private val _alumnos = MutableStateFlow<List<Alumno>>(emptyList())
    private val _vinculos = MutableStateFlow<List<Vinculo>>(emptyList())

    val alumnos = _alumnos.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        _usuarios.value = userRepo.getAll()
        _alumnos.value = alumnoRepo.getAll()
        _vinculos.value = vinculoRepo.getAll()
    }

    fun crearUsuario(u: Usuario) {
        userRepo.addUsuario(u)
        refreshData()
    }

    fun borrarUsuario(id: String) {
        userRepo.deleteUsuario(id)
        refreshData()
    }

    fun crearAlumno(a: Alumno) {
        alumnoRepo.addAlumno(a)
        refreshData()
    }

    fun borrarAlumno(id: String) {
        alumnoRepo.deleteAlumno(id)
        refreshData()
    }

    val vinculos = _vinculos.asStateFlow()



    fun vincular(idTutor: String, idAlumno: String) {
        vinculoRepo.addVinculo(Vinculo(idTutor, idAlumno))
        refreshData()
    }
    fun eliminarVinculo(idTutor: String, idAlumno: String) {
        vinculoRepo.deleteVinculo(idTutor, idAlumno)
        refreshData()
    }
}