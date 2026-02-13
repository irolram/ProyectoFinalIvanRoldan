package com.example.proyectofinalivanroldan

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import com.example.proyectofinalivanroldan.data.repository.AlumnoRepository
import com.example.proyectofinalivanroldan.data.repository.UsuarioRepository
import com.example.proyectofinalivanroldan.data.repository.VinculoRepository
import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.example.proyectofinalivanroldan.ui.mainScreen.AdminScreen
import com.example.proyectofinalivanroldan.ui.viewmodel.AdminViewModel
import org.junit.Rule
import org.junit.Test

class AdminIntegrationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificar_que_alumno_añadido_aparece_en_lista() {
        // 1. Preparamos el entorno real (Contexto de la app)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val alumnoRepo = AlumnoRepository(context)
        val userRepo = UsuarioRepository(context)
        val vinculoRepo = VinculoRepository(context)

        // 2. Insertamos un dato directamente en el "motor" (Repositorio)
        val nombreAlumno = "Alumno de Prueba Integracion"
        alumnoRepo.addAlumno(Alumno(id = "999", nombre = nombreAlumno, curso = "1º ESO"))

        // 3. Lanzamos la pantalla vinculada al ViewModel real
        composeTestRule.setContent {
            val navController = rememberNavController()
            val viewModel = AdminViewModel(userRepo, alumnoRepo, vinculoRepo)

            AdminScreen(
                navController = navController,
                viewModel = viewModel,
                onLogout = {}
            )
        }

        // 4. Verificamos que la UI refleja el cambio del repositorio
        // Primero navegamos a la pestaña de alumnos si no es la inicial
        composeTestRule.onNodeWithText("Alumnos").performClick()

        // Comprobamos que el nombre aparece en pantalla
        composeTestRule.onNodeWithText(nombreAlumno).assertIsDisplayed()
    }
}