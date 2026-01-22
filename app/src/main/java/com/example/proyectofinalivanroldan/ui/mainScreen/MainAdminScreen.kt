package com.example.proyectofinalivanroldan.ui.mainScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.proyectofinalivanroldan.ui.components.AddAlumnoDialog
import com.example.proyectofinalivanroldan.ui.components.AddVinculoDialog
import com.example.proyectofinalivanroldan.ui.viewmodel.AdminViewModel
import com.example.proyectofinalivanroldan.util.Roles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    val usuarios by viewModel.usuarios.collectAsState()
    val alumnos by viewModel.alumnos.collectAsState()
    val vinculos by viewModel.vinculos.collectAsState()

    var showUserDialog by remember { mutableStateOf(false) }
    var showAlumnoDialog by remember { mutableStateOf(false) }
    var showVinculoDialog by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Usuarios", "Alumnos", "Vínculos")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Panel de Administración") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // CORRECCIÓN AQUÍ: Ahora manejamos las 3 pestañas
                when (selectedTab) {
                    0 -> showUserDialog = true
                    1 -> showAlumnoDialog = true
                    2 -> showVinculoDialog = true // Esto abrirá el de los desplegables
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
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
                    LazyColumn {
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
                    LazyColumn {
                        items(alumnos) { alumno ->
                            Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Face, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(alumno.nombre, style = MaterialTheme.typography.titleLarge)
                                        Text("Curso: ${alumno.curso}")
                                    }
                                    // Aquí podrías añadir también un método borrarAlumno en tu ViewModel
                                    IconButton(onClick = { viewModel.borrarAlumno(alumno.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Lista de Vínculos actuales
                    Text("Relaciones Tutor-Alumno", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
                    LazyColumn {
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

        // --- GESTIÓN DE DIÁLOGOS ---

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
            // Este es el diálogo que tiene los desplegables
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


