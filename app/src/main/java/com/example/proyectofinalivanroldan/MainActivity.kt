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

// Permisos (Accompanist)
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted // ESTE ES EL QUE TE FALTA

// Repositorios
import com.example.proyectofinalivanroldan.data.repository.*
import com.example.proyectofinalivanroldan.ui.mainScreen.AdminScreen

// Pantallas (Corregido a mainScreen según tu captura)
import com.example.proyectofinalivanroldan.ui.mainScreen.ConserjeScreen

import com.example.proyectofinalivanroldan.ui.viewModel.AdminViewModelFactory
import com.example.proyectofinalivanroldan.ui.viewModel.LoginViewModelFactory
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
                    AdminScreen(viewModel = adminViewModel)
                }

                // --- TUTOR (Pasamos ID por ruta) ---
                composable("tutor/{userId}") { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")
                    val tutor = userRepo.getUsuarioById(userId ?: "")
                    tutor?.let {
                        TutorScreen(it, alumnoRepo, vinculoRepo)
                    }
                }

                composable("conserje") {
                    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

                    if (cameraPermissionState.status.isGranted) {
                        ConserjeScreen(alumnoRepo, vinculoRepo)
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