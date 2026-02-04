import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/**
 * Pantalla de gestión de permisos en tiempo de ejecución para el acceso al hardware de la cámara.
 *
 * Este componente actúa como una barrera de seguridad y usabilidad, cumpliendo con las
 * directivas de Android sobre "Mínimo Privilegio". Proporciona una interfaz clara que
 * explica al usuario la necesidad técnica del permiso antes de lanzar el sistema de
 * solicitud nativo. Es un componente crítico para el rol de Conserje, asegurando que
 * la funcionalidad de visión artificial (ML Kit) tenga los privilegios necesarios
 * para operar correctamente.
 */
@Composable
fun PermissionRequestScreen(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Se necesita acceso a la cámara para escanear los QR de los tutores.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("Conceder Permiso")
        }
    }
}