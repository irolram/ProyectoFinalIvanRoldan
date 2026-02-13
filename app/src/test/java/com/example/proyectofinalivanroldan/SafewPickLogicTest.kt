package com.example.proyectofinalivanroldan

import org.junit.Test
import org.junit.Assert.*
import com.example.proyectofinalivanroldan.data.repository.AlumnoRepository
import com.example.proyectofinalivanroldan.data.repository.UsuarioRepository
import com.example.proyectofinalivanroldan.data.repository.VinculoRepository
import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.example.proyectofinalivanroldan.ui.viewmodel.AdminViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import java.util.UUID
import kotlin.system.measureTimeMillis


class SafePickLogicTest {

    data class Vinculo(val tutorId: String, val alumnoId: String)

    /**
     * Esta función representa la lógica que queremos validar.
     * En un entorno real, esto estaría en tu Repository o UseCase.
     */
    private fun validarAcceso(tutorId: String, alumnoId: String, listaVinculos: List<Vinculo>): Boolean {
        return listaVinculos.any { it.tutorId == tutorId && it.alumnoId == alumnoId }
    }

    @Test
    fun `acceso_concedido_cuando_tutor_y_alumno_estan_vinculados`() {

        val tutorId = "user_tutor_1"
        val alumnoId = "alumno_001"
        val misVinculos = listOf(
            Vinculo("user_tutor_1", "alumno_001"),
            Vinculo("user_tutor_1", "alumno_002")
        )

        val resultado = validarAcceso(tutorId, alumnoId, misVinculos)

        assertTrue("El test debería pasar porque el vínculo existe", resultado)
    }

    @Test
    fun `acceso_denegado_cuando_tutor_no_esta_vinculado_al_alumno`() {
        val tutorId = "user_tutor_1"
        val alumnoId = "alumno_999"
        val misVinculos = listOf(Vinculo("user_tutor_1", "alumno_001"))

        val resultado = validarAcceso(tutorId, alumnoId, misVinculos)

        assertFalse("El test debería fallar (devolver false) porque el alumno no es suyo", resultado)
    }
}




@OptIn(ExperimentalCoroutinesApi::class)
class AdminViewModelCargaTest {

    private lateinit var viewModel: AdminViewModel
    private val userRepo = mockk<UsuarioRepository>(relaxed = true)
    private val alumnoRepo = mockk<AlumnoRepository>(relaxed = true)
    private val vinculoRepo = mockk<VinculoRepository>(relaxed = true)

    @Before
    fun setup() {
        // Redirigimos el despachador principal para pruebas de corrutinas
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `verificar que el ViewModel carga y transforma los alumnos del repositorio al estado de la UI`() = runTest {
        // 1. CONFIGURACIÓN (Escenario de carga)
        val alumnosPredefinidos = listOf(
            Alumno(id = "1", nombre = "Juan García", curso = "1º ESO"),
            Alumno(id = "2", nombre = "Ana López", curso = "2º ESO"),
            Alumno(id = "3", nombre = "Carlos Ruiz", curso = "1º ESO")
        )

        // Configuramos el repositorio simulado para que devuelva la lista predefinida
        every { alumnoRepo.getAll() } returns alumnosPredefinidos

        // 2. EJECUCIÓN
        // Al instanciar el ViewModel, este suele disparar la carga de datos inicial
        viewModel = AdminViewModel(userRepo, alumnoRepo, vinculoRepo)

        // 3. COMPROBACIÓN
        val estadoAlumnosUI = viewModel.alumnos.value

        // Verificamos que el tamaño de la lista coincide
        assertEquals("La cantidad de alumnos en la UI debe ser 3", 3, estadoAlumnosUI.size)

        // Verificamos que los datos del primer registro son correctos
        assertEquals("Juan García", estadoAlumnosUI[0].nombre)
        assertEquals("1º ESO", estadoAlumnosUI[0].curso)

        // Verificamos que los datos del segundo registro son correctos
        assertEquals("Ana López", estadoAlumnosUI[1].nombre)
        assertEquals("2º ESO", estadoAlumnosUI[1].curso)
    }

    @Test
    fun prueba_volumen_carga_masiva_alumnos_en_memoria() {
        val volumenEsperado = 1000
        val listaMasiva = mutableListOf<Alumno>()

        val tiempoEjecucionMs = measureTimeMillis {
            for (i in 1..volumenEsperado) {
                listaMasiva.add(
                    Alumno(
                        id = UUID.randomUUID().toString(),
                        nombre = "Alumno Estrés $i",
                        curso = "3º ESO"
                    )
                )
            }
        }

        assertEquals("La lista debe contener exactamente $volumenEsperado alumnos", volumenEsperado, listaMasiva.size)
        assertTrue("El tiempo de carga ($tiempoEjecucionMs ms) excede el límite aceptable de 500ms", tiempoEjecucionMs < 500)
    }

    private fun validarDatosAlumno(nombre: String, curso: String): Boolean {
        return nombre.isNotBlank() && curso.isNotBlank()
    }

    @Test
    fun prueba_seguridad_rechazo_entradas_vacias() {
        val nombreValido = "Ana Pérez"
        val cursoValido = "2º ESO"
        val nombreVacio = ""
        val cursoEspacios = "   "

        assertTrue("El sistema debe aceptar datos válidos", validarDatosAlumno(nombreValido, cursoValido))
        assertFalse("El sistema debe rechazar un nombre vacío", validarDatosAlumno(nombreVacio, cursoValido))
        assertFalse("El sistema debe rechazar un curso compuesto solo de espacios", validarDatosAlumno(nombreValido, cursoEspacios))
    }

    @Test
    fun prueba_seguridad_consistencia_identificadores_uuid() {
        val cantidadIds = 5000
        val idsGenerados = mutableListOf<String>()

        for (i in 1..cantidadIds) {
            idsGenerados.add(UUID.randomUUID().toString())
        }

        val idsUnicos = idsGenerados.toSet()

        assertEquals(
            "Se detectó una colisión de UUID. Identificadores duplicados encontrados.",
            cantidadIds,
            idsUnicos.size
        )
    }
}
class EstadoSeguridadUI {
    var mostrarDatosSensibles: Boolean = false
    var mensajeAlerta: String = ""
}

private fun intentarAccesoRutaProtegida(estaAutenticado: Boolean, estado: EstadoSeguridadUI) {
    if (!estaAutenticado) {
        estado.mostrarDatosSensibles = false
        estado.mensajeAlerta = "Acceso denegado. Autenticación requerida."
    } else {
        estado.mostrarDatosSensibles = true
        estado.mensajeAlerta = ""
    }

    @Test
    fun prueba_seguridad_control_de_estados_sin_autenticacion() {
        val estadoActualUI = EstadoSeguridadUI()

        intentarAccesoRutaProtegida(estaAutenticado = false, estado = estadoActualUI)

        assertFalse("Brecha de seguridad: Los datos sensibles son visibles sin autenticación", estadoActualUI.mostrarDatosSensibles)
        assertEquals("Falta el mensaje de alerta para el usuario", "Acceso denegado. Autenticación requerida.", estadoActualUI.mensajeAlerta)
    }
}


