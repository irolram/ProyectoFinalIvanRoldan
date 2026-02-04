package com.example.proyectofinalivanroldan.ui.mainScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.proyectofinalivanroldan.ui.components.AddAlumnoDialog
import com.example.proyectofinalivanroldan.ui.components.AddVinculoDialog
import com.example.proyectofinalivanroldan.ui.viewmodel.AdminViewModel
import com.example.proyectofinalivanroldan.util.Roles
import kotlinx.coroutines.launch


/**
 * Panel de control integral para el perfil de Administrador.
 * * Centraliza la gestión de usuarios, alumnos y sus vinculaciones mediante una interfaz
 * organizada por pestañas ([TabRow]). Integra la funcionalidad de generación de
 * informes CSV (RA5) y orquesta la apertura de diálogos de creación ([AddAlumnoDialog],
 * [AddVinculoDialog]) mediante una arquitectura reactiva basada en estados, garantizando
 * una administración fluida de la persistencia local del centro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: AdminViewModel, onLogout: () -> Unit) {
    val usuarios by viewModel.usuarios.collectAsState()
    val alumnos by viewModel.alumnos.collectAsState()
    val vinculos by viewModel.vinculos.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showUserDialog by remember { mutableStateOf(false) }
    var showAlumnoDialog by remember { mutableStateOf(false) }
    var showVinculoDialog by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Usuarios", "Alumnos", "Vínculos")

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración") },
                actions = {
                    // Botón para generar Informe (RA5 de la Rúbrica)
                    IconButton(onClick = {
                        val resultado = viewModel.generarInformeCSV(context)
                        // Mostramos el resultado en un Snackbar
                        scope.launch {
                            snackbarHostState.showSnackbar(resultado)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Generar Informe CSV"
                        )
                    }

                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                when (selectedTab) {
                    0 -> showUserDialog = true
                    1 -> showAlumnoDialog = true
                    2 -> showVinculoDialog = true
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> {
                    Text("Gestión de Usuarios", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(usuarios) { usuario ->
                            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(usuario.nombre, style = MaterialTheme.typography.titleLarge)
                                        Text("Rol: ${usuario.rol} | User: ${usuario.username}")
                                    }
                                    IconButton(onClick = { viewModel.borrarUsuario(usuario.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    Text("Gestión de Alumnos", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(alumnos) { alumno ->
                            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Face, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(alumno.nombre, style = MaterialTheme.typography.titleLarge)
                                        Text("Curso: ${alumno.curso}")
                                    }
                                    IconButton(onClick = { viewModel.borrarAlumno(alumno.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    Text("Relaciones Tutor-Alumno", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(vinculos) { vinculo ->
                            val tutor = usuarios.find { it.id == vinculo.idTutor }
                            val alumno = alumnos.find { it.id == vinculo.idAlumno }
                            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Tutor: ${tutor?.nombre ?: "Desconocido"}")
                                        Text("Alumno: ${alumno?.nombre ?: "Desconocido"}")
                                    }
                                    IconButton(onClick = { viewModel.eliminarVinculo(vinculo.idTutor, vinculo.idAlumno) }) {
                                        Icon(Icons.Default.Delete, tint = Color.Red, contentDescription = null)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showUserDialog) {
            AddUserDialog(
                onDismiss = { showUserDialog = false },
                onConfirm = { viewModel.crearUsuario(it); showUserDialog = false }
            )
        }

        if (showAlumnoDialog) {
            AddAlumnoDialog(
                onDismiss = { showAlumnoDialog = false },
                onConfirm = { viewModel.crearAlumno(it); showAlumnoDialog = false }
            )
        }

        if (showVinculoDialog) {
            AddVinculoDialog(
                tutores = usuarios.filter { it.rol == Roles.TUTOR },
                alumnos = alumnos,
                onDismiss = { showVinculoDialog = false },
                onConfirm = { vinculo ->
                    viewModel.vincular(vinculo.idTutor, vinculo.idAlumno)
                    showVinculoDialog = false
                }
            )
        }
    }
}
