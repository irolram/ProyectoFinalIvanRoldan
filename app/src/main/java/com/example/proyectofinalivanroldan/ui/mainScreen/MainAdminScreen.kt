package com.example.proyectofinalivanroldan.ui.mainScreen

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

import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.example.proyectofinalivanroldan.dominio.model.Usuario
import com.example.proyectofinalivanroldan.dominio.model.Vinculo
import com.example.proyectofinalivanroldan.ui.components.AddAlumnoDialog
import com.example.proyectofinalivanroldan.ui.components.AddVinculoDialog
import com.example.proyectofinalivanroldan.ui.viewmodel.AdminViewModel
import com.example.proyectofinalivanroldan.util.Roles
import kotlinx.coroutines.launch

/**
 * Panel de control integral para el perfil de Administrador.
 *
 * CARACTERÍSTICAS TÉCNICAS (RA1):
 * - RA1.b: Interfaz estructurada con Scaffold y TabRow.
 * - RA1.c: Uso eficiente de LazyColumn para listas.
 * - RA1.d: Implementación de componentes reutilizables (AdminItemCard).
 * - Seguridad: Bloqueo de borrado para el usuario 'admin'.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(viewModel: AdminViewModel, onLogout: () -> Unit) {
    // --- ESTADO Y COLECCIONES ---
    val usuarios by viewModel.usuarios.collectAsState()
    val alumnos by viewModel.alumnos.collectAsState()
    val vinculos by viewModel.vinculos.collectAsState()

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Estados para visibilidad de diálogos
    var showUserDialog by remember { mutableStateOf(false) }
    var showAlumnoDialog by remember { mutableStateOf(false) }
    var showVinculoDialog by remember { mutableStateOf(false) }

    // Gestión de Pestañas
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Usuarios", "Alumnos", "Vínculos")

    // --- INTERFAZ PRINCIPAL ---
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
                    // RA5: Generar Informe CSV
                    IconButton(onClick = {
                        val resultado = viewModel.generarInformeCSV(context)
                        scope.launch { snackbarHostState.showSnackbar(resultado) }
                    }) {
                        Icon(Icons.Default.Description, contentDescription = "Generar Informe CSV")
                    }
                    // Cerrar Sesión
                    IconButton(onClick = onLogout) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = MaterialTheme.colorScheme.error
                        )
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
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir elemento")
            }
        }
    ) { padding ->

        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {

            // Selector de Pestañas
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, maxLines = 1) }
                    )
                }
            }

            // Contenido cambiante (Polimorfismo visual)
            when (selectedTab) {
                0 -> UserList(usuarios, onDelete = { viewModel.borrarUsuario(it) })
                1 -> AlumnoList(alumnos, onDelete = { viewModel.borrarAlumno(it) })
                2 -> VinculoList(vinculos, usuarios, alumnos, onDelete = { t, a -> viewModel.eliminarVinculo(t, a) })
            }
        }

        // --- DIÁLOGOS MODALES ---
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
                onConfirm = {
                    viewModel.vincular(it.idTutor, it.idAlumno)
                    showVinculoDialog = false
                }
            )
        }
    }
}


@Composable
fun UserList(usuarios: List<Usuario>, onDelete: (String) -> Unit) {
    if (usuarios.isEmpty()) EmptyState("No hay usuarios registrados")
    else {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(usuarios) { usuario ->
                // SEGURIDAD: Evitamos borrar al admin principal
                // Si el nombre es "admin" (mayus o minus), NO es borrable
                val isDeletable = !usuario.username.equals("admin", ignoreCase = true)

                AdminItemCard(
                    title = usuario.nombre,
                    subtitle = "Rol: ${usuario.rol} | @${usuario.username}",
                    icon = Icons.Default.Person,
                    isDeletable = isDeletable, // Pasamos la condición
                    onDelete = { onDelete(usuario.id) }
                )
            }
        }
    }
}

@Composable
fun AlumnoList(alumnos: List<Alumno>, onDelete: (String) -> Unit) {
    if (alumnos.isEmpty()) EmptyState("No hay alumnos registrados")
    else {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(alumnos) { alumno ->
                AdminItemCard(
                    title = alumno.nombre,
                    subtitle = "Curso: ${alumno.curso}",
                    icon = Icons.Default.Face,
                    onDelete = { onDelete(alumno.id) }
                )
            }
        }
    }
}

@Composable
fun VinculoList(
    vinculos: List<Vinculo>,
    usuarios: List<Usuario>,
    alumnos: List<Alumno>,
    onDelete: (String, String) -> Unit
) {
    if (vinculos.isEmpty()) EmptyState("No hay vínculos activos")
    else {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(vinculos) { vinculo ->
                val tutor = usuarios.find { it.id == vinculo.idTutor }?.nombre ?: "Desconocido"
                val alumno = alumnos.find { it.id == vinculo.idAlumno }?.nombre ?: "Desconocido"

                AdminItemCard(
                    title = "Tutor: $tutor",
                    subtitle = "Alumno: $alumno",
                    icon = Icons.Default.Link,
                    onDelete = { onDelete(vinculo.idTutor, vinculo.idAlumno) }
                )
            }
        }
    }
}

/**
 * COMPONENTE MAESTRO REUTILIZABLE
 * Centraliza el diseño de las tarjetas para toda la administración.
 * Permite ocultar el botón de borrar mediante 'isDeletable'.
 */
@Composable
fun AdminItemCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isDeletable: Boolean = true, 
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono circular
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Textos
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Botón de Borrar (Condicional)
            if (isDeletable) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}