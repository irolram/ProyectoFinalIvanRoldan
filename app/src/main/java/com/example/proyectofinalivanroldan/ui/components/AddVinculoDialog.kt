package com.example.proyectofinalivanroldan.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectofinalivanroldan.dominio.model.Alumno
import com.example.proyectofinalivanroldan.dominio.model.Usuario
import com.example.proyectofinalivanroldan.dominio.model.Vinculo

/**
 * Componente de interfaz que proporciona un diálogo interactivo para la creación de vínculos.
 *
 * Utiliza componentes avanzados de Material Design 3, como [ExposedDropdownMenuBox],
 * para permitir la selección bidireccional entre las entidades Usuario (Tutor) y Alumno.
 * El componente implementa validación de estado en tiempo real, manteniendo el botón
 * de confirmación deshabilitado hasta que ambos campos requeridos han sido seleccionados,
 * asegurando así la integridad de la relación N:M antes de su persistencia.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVinculoDialog(
    tutores: List<Usuario>,
    alumnos: List<Alumno>,
    onDismiss: () -> Unit,
    onConfirm: (Vinculo) -> Unit
) {
    var selectedTutor by remember { mutableStateOf<Usuario?>(null) }
    var selectedAlumno by remember { mutableStateOf<Alumno?>(null) }

    var tutorExpanded by remember { mutableStateOf(false) }
    var alumnoExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Vincular Tutor y Alumno") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Selector de Tutor
                Text("Selecciona Tutor:", style = MaterialTheme.typography.labelLarge)
                ExposedDropdownMenuBox(
                    expanded = tutorExpanded,
                    onExpandedChange = { tutorExpanded = !tutorExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedTutor?.nombre ?: "Seleccionar...",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tutorExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = tutorExpanded,
                        onDismissRequest = { tutorExpanded = false }
                    ) {
                        tutores.forEach { tutor ->
                            DropdownMenuItem(
                                text = { Text(tutor.nombre) },
                                onClick = {
                                    selectedTutor = tutor
                                    tutorExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de Alumno
                Text("Selecciona Alumno:", style = MaterialTheme.typography.labelLarge)
                ExposedDropdownMenuBox(
                    expanded = alumnoExpanded,
                    onExpandedChange = { alumnoExpanded = !alumnoExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedAlumno?.nombre ?: "Seleccionar...",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = alumnoExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = alumnoExpanded,
                        onDismissRequest = { alumnoExpanded = false }
                    ) {
                        alumnos.forEach { alumno ->
                            DropdownMenuItem(
                                text = { Text("${alumno.nombre} (${alumno.curso})") },
                                onClick = {
                                    selectedAlumno = alumno
                                    alumnoExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                enabled = selectedTutor != null && selectedAlumno != null,
                onClick = {
                    onConfirm(Vinculo(selectedTutor!!.id, selectedAlumno!!.id))
                }
            ) { Text("Vincular") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}