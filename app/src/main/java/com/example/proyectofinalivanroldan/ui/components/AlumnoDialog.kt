package com.example.proyectofinalivanroldan.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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

@Composable
fun AddAlumnoDialog(
    onDismiss: () -> Unit,
    onConfirm: (Alumno) -> Unit
) {
    // Estados para controlar los campos del formulario
    var nombre by remember { mutableStateOf("") }
    var curso by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Registrar Nuevo Alumno")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Completo") },
                    placeholder = { Text("Ej: Juan Pérez") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = curso,
                    onValueChange = { curso = it },
                    label = { Text("Curso") },
                    placeholder = { Text("Ej: 1º ESO A") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombre.isNotBlank() && curso.isNotBlank()) {
                        // Creamos el objeto Alumno con un ID único generado automáticamente
                        val nuevoAlumno = Alumno(
                            id = UUID.randomUUID().toString(),
                            nombre = nombre,
                            curso = curso
                        )
                        onConfirm(nuevoAlumno)
                    }
                }
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
