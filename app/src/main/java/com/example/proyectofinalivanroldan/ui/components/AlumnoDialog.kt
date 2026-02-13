package com.example.proyectofinalivanroldan.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectofinalivanroldan.dominio.model.Alumno
import java.util.UUID

/**
 * Diálogo modal diseñado para la captura de datos y registro de nuevas entidades Alumno.
 * Incluye selector de curso y formateo automático de nombre (Capitalización).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlumnoDialog(
    onDismiss: () -> Unit,
    onConfirm: (Alumno) -> Unit
) {
    // Estados para controlar los campos del formulario
    var nombre by remember { mutableStateOf("") }
    var curso by remember { mutableStateOf("") }

    // Estados para el menú desplegable
    var expanded by remember { mutableStateOf(false) }

    // Lista de cursos disponibles
    val opcionesCurso = listOf(
        "1º ESO",
        "2º ESO",
        "3º ESO",
        "4º ESO",
        "1º Bachillerato",
        "2º Bachillerato"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Registrar Nuevo Alumno")
        },
        text = {
            Column {
                // Campo Nombre (Texto libre)
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Completo") },
                    placeholder = { Text("Ej: Juan Pérez") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Curso (Selector Desplegable)
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = curso,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Curso") },
                        placeholder = { Text("Selecciona un curso") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        opcionesCurso.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(text = opcion) },
                                onClick = {
                                    curso = opcion
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotBlank() && curso.isNotBlank()) {

                        val nombreFormateado = nombre.trim().split("\\s+".toRegex())
                            .joinToString(" ") { palabra ->
                                palabra.lowercase().replaceFirstChar { it.uppercase() }
                            }

                        val nuevoAlumno = Alumno(
                            id = UUID.randomUUID().toString(),
                            nombre = nombreFormateado, // Usamos el nombre ya formateado
                            curso = curso
                        )
                        onConfirm(nuevoAlumno)
                    }
                },
                // Deshabilitamos el botón si no hay datos
                enabled = nombre.isNotBlank() && curso.isNotBlank()
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}