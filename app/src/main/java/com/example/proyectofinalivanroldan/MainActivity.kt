package com.example.proyectofinalivanroldan

import LoginScreen
import PermissionRequestScreen
import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Permisos (Accompanist) - Asegúrate de tener la librería en build.gradle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted

// Repositorios
import com.example.proyectofinalivanroldan.data.repository.*

// Pantallas
import com.example.proyectofinalivanroldan.ui.mainScreen.AdminScreen
import com.example.proyectofinalivanroldan.ui.mainScreen.ConserjeScreen
import com.example.proyectofinalivanroldan.ui.mainScreen.TutorScreen


// ViewModels
import com.example.proyectofinalivanroldan.ui.viewmodel.AdminViewModelFactory
import com.example.proyectofinalivanroldan.ui.viewmodel.LoginViewModelFactory
import com.example.proyectofinalivanroldan.ui.viewmodel.AdminViewModel
import com.example.proyectofinalivanroldan.ui.viewmodel.LoginViewModel

import com.example.proyectofinalivanroldan.util.Roles

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de Repositorios
        val userRepo = UsuarioRepository(applicationContext)
        val alumnoRepo = AlumnoRepository(applicationContext)
        val vinculoRepo = VinculoRepository(applicationContext)

        // Factories para ViewModels
        val loginFactory = LoginViewModelFactory(userRepo)
        val adminFactory = AdminViewModelFactory(userRepo, alumnoRepo, vinculoRepo)

        setContent {
            val navController = rememberNavController()
            val loginViewModel: LoginViewModel = ViewModelProvider(this, loginFactory)[LoginViewModel::class.java]
            val adminViewModel: AdminViewModel = ViewModelProvider(this, adminFactory)[AdminViewModel::class.java]

            NavHost(navController = navController, startDestination = "login") {

                // --- LOGIN ---
                composable("login") {
                    LoginScreen(
                        viewModel = loginViewModel,
                        onLoginSuccess = { usuario ->
                            // Navegación según el ROL
                            when (usuario.rol) {
                                Roles.ADMIN -> navController.navigate("admin")
                                Roles.TUTOR -> navController.navigate("tutor/${usuario.id}")
                                Roles.CONSERJE -> navController.navigate("conserje")
                                else -> {}
                            }
                        }
                    )
                }

                // --- ADMIN ---
                composable("admin") {
                    AdminScreen(
                        viewModel = adminViewModel,
                        onLogout = {
                            loginViewModel.resetState() // LIMPIAR ESTADO PARA QUE NO REDIRIJA SOLO
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }

                // --- TUTOR ---
                composable("tutor/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")
                    val tutorUsuario = userRepo.getUsuarioById(userId ?: "")

                    if (tutorUsuario != null) {
                        TutorScreen(
                            tutorId = tutorUsuario.id,
                            tutorNombre = tutorUsuario.nombre,
                            alumnoRepo = alumnoRepo,
                            vinculoRepo = vinculoRepo,
                            onLogout = {
                                loginViewModel.resetState() // LIMPIAR ESTADO PARA QUE NO REDIRIJA SOLO
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                }

                // --- CONSERJE ---
                composable("conserje") {
                    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

                    if (cameraPermissionState.status.isGranted) {
                        ConserjeScreen(
                            alumnoRepo = alumnoRepo,
                            vinculoRepo = vinculoRepo,
                            onLogout = {
                                loginViewModel.resetState() // LIMPIAR ESTADO PARA QUE NO REDIRIJA SOLO
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    } else {
                        PermissionRequestScreen {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    }
                }
            }
        }
    }
}
