# SafePick: Sistema de recogida de alumnos

# Motivación 

El origen de este proyecto nace de una experiencia personal muy cercana. Al observar el trabajo diario de mi madre como conserje en un centro educativo, identifiqué una problemática crítica en el proceso de recogida de alumnos: la dificultad para verificar, de forma rápida y segura, la identidad de los tutores legales.

Actualmente, este proceso depende en gran medida de la memoria visual del personal o de listados en papel, lo que conlleva un alto riesgo de error humano y una gran presión administrativa ante posibles entregas no autorizadas. Mi objetivo principal ha sido desarrollar una solución tecnológica que elimine esta incertidumbre, agilizando el flujo de trabajo del personal de conserjería y garantizando la seguridad de los menores, transformando una tarea estresante y lenta en un proceso automatizado y fiable.

# Flujo del programa

El Motor de Arranque: [MainActivity.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt)  el punto de entrada. Su función es orquestar:
•Inicializa los Repositorios: Crea las instancias que manejan los datos (UsuarioRepository, AlumnoRepository, etc.).
•Configura la Navegación (NavHost): Define las "rutas" de la app (login, admin, tutor, conserje).
•Inyecta Dependencias: Pasa los repositorios a los ViewModels para que estos puedan funcionar.

Capa de Datos: Aquí reside la información. No saben nada de la interfaz gráfica.
•Modelos ([Usuario](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Usuario.kt ), [Alumno](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Alumno.kt), [Vinculo](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Vinculo.kt)): Clases simples que definen qué datos tiene cada entidad.
•Repositorios La carpeta [repository](https://github.com/irolram/ProyectoFinalIvanRoldan/tree/main/app/src/main/java/com/example/proyectofinalivanroldan/data/repository) Leen y escriben en archivos JSON locales (usando Gson).
IAutorizacionRepo: Es el "juez". Verifica si un Tutor tiene permiso para recoger a un Alumno específico.
•JsonPersistence.kt: Es la herramienta que ayuda a convertir los objetos Kotlin a texto JSON y viceversa.

Capa de Lógica de Interfaz: ViewModels Actúan como el "cerebro" de cada pantalla.
•LoginViewModel: Recibe las credenciales, consulta al UsuarioRepository y le dice a la vista si el acceso es correcto o si hay un error. Maneja el estado de la sesión.
•AdminViewModel: Gestiona la lógica de crear/borrar usuarios y alumnos. También contiene la función de generar el informe CSV.

Capa de Vista: Screens y ComponentsEs lo que el usuario ve y toca (Jetpack Compose).•LoginScreen.kt: Recoge los datos del usuario. Al tener éxito, el flujo vuelve a MainActivity para redirigir según el Rol.•AdminScreen.kt: Interfaz con pestañas. Usa diálogos (AddUserDialog, etc.) para modificar los datos.•TutorScreen.kt:◦Usa el QrGenerator.kt para convertir el ID del tutor en una imagen QR.◦Muestra la lista de sus alumnos vinculados.•ConserjeScreen.kt:◦Usa la cámara mediante CameraX.◦Conecta con el QrAnalyzer.kt, que usa ML Kit para "leer" el código.◦Una vez leído el ID, consulta al repositorio para confirmar si el tutor es válido y qué alumnos puede llevarse.
