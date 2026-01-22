package com.example.proyectofinalivanroldan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.proyectofinalivanroldan.dominio.model.Usuario
import com.example.proyectofinalivanroldan.data.repository.AlumnoRepository
import com.example.proyectofinalivanroldan.data.repository.VinculoRepository
import com.example.proyectofinalivanroldan.util.QrGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorScreen(
    tutor: Usuario,
    alumnoRepo: AlumnoRepository,
    vinculoRepo: VinculoRepository
) {
    // 1. Obtenemos los alumnos vinculados a este tutor
    val idsAlumnos = remember { vinculoRepo.getAlumnosByTutor(tutor.id) }
    val misAlumnos = remember { idsAlumnos.mapNotNull { alumnoRepo.getAlumnoById(it) } }

    // 2. Generamos el QR con el ID del tutor
    val qrBitmap = remember { QrGenerator.generarQr(tutor.id) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi Código de Acceso") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Hola, ${tutor.nombre}", style = MaterialTheme.typography.headlineSmall)
            
            Spacer(modifier = Modifier.height(24.dp))

            // Mostramos el QR generado
            qrBitmap?.let {
                Card(elevation = CardDefaults.cardElevation(8.dp)) {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Código QR de acceso",
                        modifier = Modifier.size(250.dp).padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Alumnos autorizados:", style = MaterialTheme.typography.titleMedium)
            
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(misAlumnos) { alumno ->
                    ListItem(
                        headlineContent = { Text(alumno.nombre) },
                        supportingContent = { Text(alumno.curso) }
                    )
                }
            }
        }
    }
}