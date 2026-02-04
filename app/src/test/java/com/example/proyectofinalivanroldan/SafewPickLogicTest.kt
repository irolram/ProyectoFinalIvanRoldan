package com.example.proyectofinalivanroldan

import org.junit.Test
import org.junit.Assert.*

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