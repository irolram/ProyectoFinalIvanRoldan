package com.example.proyectofinalivanroldan.ui.mainScreen

import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.proyectofinalivanroldan.data.repository.AlumnoRepository
import com.example.proyectofinalivanroldan.data.repository.VinculoRepository
import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.example.proyectofinalivanroldan.util.QrAnalyzer
import java.util.concurrent.Executors


/**
 * Pantalla operativa para el perfil Conserje centrada en la validación de accesos.
 * * Implementa un flujo de visión artificial mediante [CameraX] y [ImageAnalysis]
 * para la decodificación de códigos QR en tiempo real. Gestiona de forma reactiva
 * la consulta cruzada entre repositorios para validar la autorización de recogida,
 * proporcionando feedback visual inmediato (Acceso Autorizado/Denegado) y optimizando
 * el uso de recursos mediante el ciclo de vida del componente.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConserjeScreen(
    alumnoRepo: AlumnoRepository,
    vinculoRepo: VinculoRepository,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    // Estados para controlar el escaneo y los resultados
    var scannedId by remember { mutableStateOf<String?>(null) }
    var alumnosAutorizados by remember { mutableStateOf<List<Alumno>>(emptyList()) }

    // Cerramos el executor al salir de la pantalla para evitar fugas de memoria
    DisposableEffect(Unit) {
        onDispose { cameraExecutor.shutdown() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escáner de Conserjería") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // CÁMARA
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxWidth()
                    .background(Color.Black)
            ) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()

                            // Configuración de la vista previa
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }

                            val resolutionSelector = ResolutionSelector.Builder()
                                .setResolutionStrategy(
                                    ResolutionStrategy(
                                        Size(1280, 720),
                                        ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER
                                    )
                                )
                                .build()

                            // Configuración del análisis de QR
                            val imageAnalysis = ImageAnalysis.Builder()
                                .setResolutionSelector(resolutionSelector)
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()

                            imageAnalysis.setAnalyzer(cameraExecutor, QrAnalyzer { id ->
                                if (scannedId != id) {
                                    scannedId = id
                                    val ids = vinculoRepo.getAlumnosByTutor(id)
                                    alumnosAutorizados = ids.mapNotNull { alumnoRepo.getAlumnoById(it) }
                                }
                            })

                            try {
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    CameraSelector.DEFAULT_BACK_CAMERA,
                                    preview,
                                    imageAnalysis
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }, ContextCompat.getMainExecutor(context))
                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // RESULTADOS
            Card(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when {
                        // Estado inicial: No se ha escaneado nada todavía
                        scannedId == null -> {
                            Text("Esperando código QR...", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Enfoque el código del tutor con la cámara", color = Color.Gray)
                        }

                        // Estado Error: ID escaneado pero sin alumnos vinculados
                        alumnosAutorizados.isEmpty() -> {
                            Text("ACCESO DENEGADO", color = Color.Red, style = MaterialTheme.typography.headlineMedium)
                            Text("ID Tutor: $scannedId", style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Este tutor no tiene alumnos autorizados.")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { scannedId = null }) {
                                Text("Reintentar escaneo")
                            }
                        }

                        // Estado Éxito: Se han encontrado alumnos vinculados
                        else -> {
                            Text("ACCESO AUTORIZADO", color = Color(0xFF2E7D32), style = MaterialTheme.typography.headlineMedium)
                            Text("Tutor validado correctamente", style = MaterialTheme.typography.labelSmall)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Alumnos a recoger:", style = MaterialTheme.typography.titleMedium)

                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items(alumnosAutorizados) { alumno ->
                                    Text(
                                        text = "• ${alumno.nombre} (${alumno.curso})",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { scannedId = null },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                            ) {
                                Text("Limpiar y siguiente")
                            }
                        }
                    }
                }
            }
        }
    }
}
