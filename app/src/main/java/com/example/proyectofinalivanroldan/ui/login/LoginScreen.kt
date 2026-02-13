import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.proyectofinalivanroldan.dominio.model.Usuario
import com.example.proyectofinalivanroldan.ui.viewmodel.LoginState
import com.example.proyectofinalivanroldan.ui.viewmodel.LoginViewModel


/**
 * Pantalla de autenticación principal y control de acceso por roles.
 *
 * Implementa un flujo reactivo basado en [LoginState] para gestionar los estados
 * de carga, éxito y error. Garantiza la seguridad de la interfaz mediante
 * transformaciones visuales de contraseña y validaciones en tiempo real,
 * actuando como la puerta de entrada jerárquica según el perfil de usuario.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: (Usuario) -> Unit,
    viewModel: LoginViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "SafePick", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is LoginState.Loading // Bloqueamos mientras carga
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = loginState !is LoginState.Loading
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (loginState is LoginState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
        }

        val currentState = loginState
        if (currentState is LoginState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currentState.mensaje,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        LaunchedEffect(loginState) {
            if (loginState is LoginState.Success) {
                onLoginSuccess((loginState as LoginState.Success).usuario)
            }
        }
    }
}