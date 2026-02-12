package com.example.proyectofinalivanroldan

import LoginScreen
import PermissionRequestScreen
import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import com.example.proyectofinalivanroldan.data.repository.*
import com.example.proyectofinalivanroldan.ui.mainScreen.AdminScreen
import com.example.proyectofinalivanroldan.ui.mainScreen.ConserjeScreen
import com.example.proyectofinalivanroldan.ui.mainScreen.TutorScreen
import com.example.proyectofinalivanroldan.ui.theme.SafePickTheme // <--- IMPORTANTE: Importar tu tema
import com.example.proyectofinalivanroldan.ui.viewmodel.AdminViewModelFactory
import com.example.proyectofinalivanroldan.ui.viewmodel.LoginViewModelFactory
import com.example.proyectofinalivanroldan.ui.viewmodel.AdminViewModel
import com.example.proyectofinalivanroldan.ui.viewmodel.LoginViewModel

import com.example.proyectofinalivanroldan.util.Roles

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userRepo = UsuarioRepository(applicationContext)

        if (userRepo.getAll().isEmpty()) {
            val adminDefault = com.example.proyectofinalivanroldan.dominio.model.Usuario(
                id = java.util.UUID.randomUUID().toString(),
                nombre = "Administrador",
                username = "admin",
                password = "admin",
                rol = com.example.proyectofinalivanroldan.util.Roles.ADMIN
            )
            userRepo.addUsuario(adminDefault)
        }

        val alumnoRepo = AlumnoRepository(applicationContext)
        val vinculoRepo = VinculoRepository(applicationContext)

        // Factories para ViewModels
        val loginFactory = LoginViewModelFactory(userRepo)
        val adminFactory = AdminViewModelFactory(userRepo, alumnoRepo, vinculoRepo)

        setContent {

            SafePickTheme {

                // AQUI APLICAMOS EL FONDO (Para que cambie a negro en modo oscuro)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    val loginViewModel: LoginViewModel = ViewModelProvider(this, loginFactory)[LoginViewModel::class.java]
                    val adminViewModel: AdminViewModel = ViewModelProvider(this, adminFactory)[AdminViewModel::class.java]

                    NavHost(navController = navController, startDestination = "login") {

                        // LOGIN
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

                        // ADMIN
                        composable("admin") {
                            AdminScreen(
                                viewModel = adminViewModel,
                                onLogout = {
                                    loginViewModel.resetState()
                                    navController.navigate("login") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // TUTOR
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
                                        loginViewModel.resetState()
                                        navController.navigate("login") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }

                        // CONSERJE
                        composable("conserje") {
                            val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

                            if (cameraPermissionState.status.isGranted) {
                                ConserjeScreen(
                                    alumnoRepo = alumnoRepo,
                                    vinculoRepo = vinculoRepo,
                                    onLogout = {
                                        loginViewModel.resetState()
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
    }
}