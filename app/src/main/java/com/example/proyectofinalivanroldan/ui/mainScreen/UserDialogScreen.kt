package com.example.proyectofinalivanroldan.ui.mainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectofinalivanroldan.dominio.model.Usuario
import com.example.proyectofinalivanroldan.util.Roles

@Composable
fun AddUserDialog(
    onDismiss: () -> Unit,
    onConfirm: (Usuario) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(Roles.TUTOR) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Usuario") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") })
                OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
                OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

                Text("Seleccionar Rol:", modifier = Modifier.padding(top = 8.dp))
                Row {
                    RadioButton(selected = selectedRole == Roles.TUTOR, onClick = { selectedRole = Roles.TUTOR })
                    Text("Tutor", modifier = Modifier.padding(top = 12.dp))
                    Spacer(Modifier.width(8.dp))
                    RadioButton(selected = selectedRole == Roles.CONSERJE, onClick = { selectedRole = Roles.CONSERJE })
                    Text("Conserje", modifier = Modifier.padding(top = 12.dp))
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val nuevo = Usuario(
                    id = java.util.UUID.randomUUID().toString(),
                    username = username,
                    nombre = nombre,
                    password = password,
                    rol = selectedRole
                )
                onConfirm(nuevo)
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}