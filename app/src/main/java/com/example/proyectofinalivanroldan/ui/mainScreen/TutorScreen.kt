package com.example.proyectofinalivanroldan.ui.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectofinalivanroldan.data.repository.AlumnoRepository
import com.example.proyectofinalivanroldan.data.repository.VinculoRepository
import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.example.proyectofinalivanroldan.util.QrGenerator


/**
 * Interfaz de usuario para el perfil Tutor centrada en la identificación y consulta.
 * * Implementa un sistema de navegación mediante [NavigationBar] para alternar entre
 * la visualización de alumnos vinculados y la generación dinámica de credenciales QR.
 * Utiliza [LazyColumn] para optimizar el listado de registros y garantiza una
 * experiencia de usuario fluida mediante el uso de [remember] para evitar
 * recálculos innecesarios de mapas de bits (QR) durante las recomposiciones.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorScreen(
    tutorId: String,
    tutorNombre: String,
    alumnoRepo: AlumnoRepository,
    vinculoRepo: VinculoRepository,
    onLogout: () -> Unit
) {
    // Estado para controlar qué pestaña estamos viendo (0 = Alumnos, 1 = QR)
    var selectedTab by remember { mutableIntStateOf(0) }

    // Cargar alumnos vinculados
    val alumnosAutorizados = remember(tutorId) {
        val ids = vinculoRepo.getAlumnosByTutor(tutorId)
        ids.mapNotNull { alumnoRepo.getAlumnoById(it) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Portal del Tutor") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Salir", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        bottomBar = {

            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Face, contentDescription = "Alumnos") },
                    label = { Text("Mis Alumnos") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.QrCode2, contentDescription = "QR Acceso") },
                    label = { Text("Pase QR") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
            }
        }
    ) { paddingValues ->

        // Contenido cambiante según el botón pulsado
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (selectedTab == 0) {
                VistaAlumnos(tutorNombre, alumnosAutorizados)
            } else {
                VistaQR(tutorId, tutorNombre)
            }
        }
    }
}

// LISTA DE ALUMNOS
@Composable
fun VistaAlumnos(nombreTutor: String, listaAlumnos: List<Alumno>) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Bienvenido/a,", style = MaterialTheme.typography.labelMedium)
                Text(nombreTutor, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            }
        }

        Text("Alumnos Vinculados", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))

        if (listaAlumnos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tienes alumnos asignados.", color = Color.Gray)
            }
        } else {
            LazyColumn {
                items(listaAlumnos) { alumno ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        ListItem(
                            headlineContent = { Text(alumno.nombre, fontWeight = FontWeight.Bold) },
                            supportingContent = { Text("Curso: ${alumno.curso}") },
                            leadingContent = {
                                Icon(Icons.Default.Face, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        )
                    }
                }
            }
        }
    }
}

// CÓDIGO QR
@Composable
fun VistaQR(tutorId: String, nombreTutor: String) {
    // Generamos el QR solo cuando se entra en esta vista
    val qrBitmap = remember(tutorId) {
        QrGenerator.generarQr(tutorId) // Cambia "generateQrCode" por "generarQr" y quita el 600
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pase de Recogida", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(32.dp))

        Card(
            elevation = CardDefaults.cardElevation(10.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(300.dp) // Tamaño grande para el QR
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                qrBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Código QR",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(nombreTutor, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Muestra este código al conserje", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}