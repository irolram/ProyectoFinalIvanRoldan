package com.example.proyectofinalivanroldan.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.proyectofinalivanroldan.data.repository.UsuarioRepository
import com.example.proyectofinalivanroldan.data.repository.AlumnoRepository
import com.example.proyectofinalivanroldan.data.repository.VinculoRepository
import com.example.proyectofinalivanroldan.dominio.model.Usuario
import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.example.proyectofinalivanroldan.dominio.model.Vinculo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class AdminViewModel(
    private val userRepo: UsuarioRepository,
    private val alumnoRepo: AlumnoRepository,
    private val vinculoRepo: VinculoRepository
) : ViewModel() {

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios = _usuarios.asStateFlow()

    private val _alumnos = MutableStateFlow<List<Alumno>>(emptyList())
    val alumnos = _alumnos.asStateFlow()

    private val _vinculos = MutableStateFlow<List<Vinculo>>(emptyList())
    val vinculos = _vinculos.asStateFlow()

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

    fun vincular(idTutor: String, idAlumno: String) {
        vinculoRepo.addVinculo(Vinculo(idTutor, idAlumno))
        refreshData()
    }

    fun eliminarVinculo(idTutor: String, idAlumno: String) {
        vinculoRepo.deleteVinculo(idTutor, idAlumno)
        refreshData()
    }

    /**
     * Genera un informe en formato CSV con la relación de alumnos y sus tutores.
     * Cumple con los criterios RA5.f (Uso de herramientas) y RA5.g (Modificación de código).
     */
    fun generarInformeCSV(context: Context): String {
        val listaVinculos = _vinculos.value
        val listaAlumnos = _alumnos.value
        val listaUsuarios = _usuarios.value

        val sb = StringBuilder()
        sb.append("ID Alumno;Nombre Alumno;Curso;ID Tutor;Nombre Tutor\n")

        listaVinculos.forEach { vinculo ->
            val alumno = listaAlumnos.find { it.id == vinculo.idAlumno }
            val tutor = listaUsuarios.find { it.id == vinculo.idTutor }
            sb.append("${alumno?.id ?: "N/A"};${alumno?.nombre ?: "Desconocido"};${alumno?.curso ?: "N/A"};${tutor?.id ?: "N/A"};${tutor?.nombre ?: "Desconocido"}\n")
        }

        return try {
            val file = File(context.filesDir, "informe_alumnos_tutores.csv")
            file.writeText(sb.toString())
            "Informe CSV generado exitosamente en: ${file.name}"
        } catch (e: Exception) {
            "Error al generar el informe: ${e.message}"
        }
    }
}
