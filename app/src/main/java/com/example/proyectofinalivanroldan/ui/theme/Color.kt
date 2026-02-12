package com.example.proyectofinalivanroldan.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// 1. Definición de colores base (Hexadecimales)
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// 2. Definición de las Paletas (Oscura y Clara)
// Al hacerlas 'public' (sin poner private), el archivo Theme.kt las podrá leer.

val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF1C1B1F), // Fondo oscuro
    surface = Color(0xFF1C1B1F)
)

val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE), // Fondo claro
    surface = Color(0xFFFFFBFE)
)