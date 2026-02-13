package com.example.proyectofinalivanroldan.ui.mainScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.example.proyectofinalivanroldan.dominio.model.Usuario
import com.example.proyectofinalivanroldan.dominio.model.Vinculo
import com.example.proyectofinalivanroldan.ui.components.*
import com.example.proyectofinalivanroldan.ui.viewmodel.AdminViewModel
import com.example.proyectofinalivanroldan.util.Roles
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    navController: NavController,
    viewModel: AdminViewModel,
    onLogout: () -> Unit
) {
    val usuarios by viewModel.usuarios.collectAsState()
    val alumnos by viewModel.alumnos.collectAsState()
    val vinculos by viewModel.vinculos.collectAsState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showUserDialog by remember { mutableStateOf(false) }
    var showAlumnoDialog by remember { mutableStateOf(false) }
    var showVinculoDialog by remember { mutableStateOf(false) }

    var alumnoToDelete by remember { mutableStateOf<Alumno?>(null) }
    var usuarioToDelete by remember { mutableStateOf<Usuario?>(null) }
    var showExitDialog by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Usuarios", "Alumnos", "Vínculos")
    var filtroCurso by remember { mutableStateOf("Todos") }

    BackHandler { showExitDialog = true }

    if (showExitDialog) {
        ConfirmDeleteDialog(
            title = "¿Cerrar Sesión?",
            text = "Vas a salir al menú de login. ¿Deseas continuar?",
            onDismiss = { showExitDialog = false },
            onConfirm = {
                showExitDialog = false
                onLogout()
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administración") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = {
                        val resultado = viewModel.generarInformeCSV(context)
                        scope.launch { snackbarHostState.showSnackbar(resultado) }
                    }) { Icon(Icons.Default.Description, "CSV") }
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Salir", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (selectedTab) {
                        0 -> showUserDialog = true
                        1 -> showAlumnoDialog = true
                        2 -> showVinculoDialog = true
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) { Icon(Icons.Default.Add, "Añadir") }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title) })
                }
            }

            when (selectedTab) {
                0 -> UserList(usuarios) { id -> usuarioToDelete = usuarios.find { it.id == id } }
                1 -> {
                    Column {
                        CursoFilterDropdown(filtroCurso) { filtroCurso = it }
                        val filtrados = if (filtroCurso == "Todos") alumnos else alumnos.filter { it.curso == filtroCurso }
                        AlumnoList(filtrados) { id -> alumnoToDelete = alumnos.find { it.id == id } }
                    }
                }
                2 -> VinculoList(vinculos, usuarios, alumnos) { t, a -> viewModel.eliminarVinculo(t, a) }
            }
        }

        if (showUserDialog) AddUserDialog(onDismiss = { showUserDialog = false }, onConfirm = { viewModel.crearUsuario(it); showUserDialog = false })
        if (showAlumnoDialog) AddAlumnoDialog(onDismiss = { showAlumnoDialog = false }, onConfirm = { viewModel.crearAlumno(it); showAlumnoDialog = false })
        if (showVinculoDialog) AddVinculoDialog(tutores = usuarios.filter { it.rol == Roles.TUTOR }, alumnos = alumnos, onDismiss = { showVinculoDialog = false }, onConfirm = { viewModel.vincular(it.idTutor, it.idAlumno); showVinculoDialog = false })

        if (alumnoToDelete != null) {
            ConfirmDeleteDialog(
                title = "¿Eliminar Alumno?",
                text = "Vas a eliminar a \"${alumnoToDelete!!.nombre}\".",
                onDismiss = { alumnoToDelete = null },
                onConfirm = { viewModel.borrarAlumno(alumnoToDelete!!.id); alumnoToDelete = null; scope.launch { snackbarHostState.showSnackbar("Eliminado") } }
            )
        }

        if (usuarioToDelete != null) {
            ConfirmDeleteDialog(
                title = "¿Eliminar Usuario?",
                text = "Vas a eliminar a \"${usuarioToDelete!!.nombre}\".",
                onDismiss = { usuarioToDelete = null },
                onConfirm = { viewModel.borrarUsuario(usuarioToDelete!!.id); usuarioToDelete = null; scope.launch { snackbarHostState.showSnackbar("Eliminado") } }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CursoFilterDropdown(seleccionado: String, onSeleccion: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val opciones = listOf("Todos", "1º ESO", "2º ESO", "3º ESO", "4º ESO", "1º Bachillerato", "2º Bachillerato")
    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = seleccionado, onValueChange = {}, readOnly = true, label = { Text("Filtrar por Curso") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                opciones.forEach { DropdownMenuItem(text = { Text(it) }, onClick = { onSeleccion(it); expanded = false }) }
            }
        }
    }
}

@Composable
fun UserList(usuarios: List<Usuario>, onDelete: (String) -> Unit) {
    if (usuarios.isEmpty()) EmptyState("No hay usuarios")
    else LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(usuarios) { u -> AdminItemCard(u.nombre, "Rol: ${u.rol}", Icons.Default.Person, !u.username.equals("admin", true)) { onDelete(u.id) } }
    }
}

@Composable
fun AlumnoList(alumnos: List<Alumno>, onDelete: (String) -> Unit) {
    if (alumnos.isEmpty()) EmptyState("No hay alumnos")
    else LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(alumnos) { a -> AdminItemCard(a.nombre, "Curso: ${a.curso}", Icons.Default.Face) { onDelete(a.id) } }
    }
}

@Composable
fun VinculoList(vinculos: List<Vinculo>, usuarios: List<Usuario>, alumnos: List<Alumno>, onDelete: (String, String) -> Unit) {
    if (vinculos.isEmpty()) EmptyState("No hay vínculos")
    else LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(vinculos) { v ->
            val t = usuarios.find { it.id == v.idTutor }?.nombre ?: "N/A"
            val a = alumnos.find { it.id == v.idAlumno }?.nombre ?: "N/A"
            AdminItemCard("Tutor: $t", "Alumno: $a", Icons.Default.Link) { onDelete(v.idTutor, v.idAlumno) }
        }
    }
}

@Composable
fun AdminItemCard(title: String, subtitle: String, icon: ImageVector, isDeletable: Boolean = true, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(icon, null, tint = MaterialTheme.colorScheme.onPrimaryContainer) }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (isDeletable) IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) }
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(message) }
}