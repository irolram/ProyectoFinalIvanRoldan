# SafePick: Sistema de recogida de alumnos

# Motivación 

El origen de este proyecto nace de una experiencia personal muy cercana. Al observar el trabajo diario de mi madre como conserje en un centro educativo, identifiqué una problemática crítica en el proceso de recogida de alumnos: la dificultad para verificar, de forma rápida y segura, la identidad de los tutores legales.

Actualmente, este proceso depende en gran medida de la memoria visual del personal o de listados en papel, lo que conlleva un alto riesgo de error humano y una gran presión administrativa ante posibles entregas no autorizadas. Mi objetivo principal ha sido desarrollar una solución tecnológica que elimine esta incertidumbre, agilizando el flujo de trabajo del personal de conserjería y garantizando la seguridad de los menores, transformando una tarea estresante y lenta en un proceso automatizado y fiable.

# Flujo del programa

El Motor de Arranque: [MainActivity.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt)  el punto de entrada. Su función es orquestar:
- Inicializa los Repositorios: Crea las instancias que manejan los datos (UsuarioRepository, AlumnoRepository, etc.).
- Configura la Navegación (NavHost): Define las "rutas" de la app (login, admin, tutor, conserje).
- Inyecta Dependencias: Pasa los repositorios a los ViewModels para que estos puedan funcionar.

Capa de Datos: Aquí reside la información. No saben nada de la interfaz gráfica.
- Modelos ([Usuario](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Usuario.kt ), [Alumno](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Alumno.kt), [Vinculo](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Vinculo.kt)): Clases simples que definen qué datos tiene cada entidad.
- Repositorios: En la carpeta [repository](https://github.com/irolram/ProyectoFinalIvanRoldan/tree/main/app/src/main/java/com/example/proyectofinalivanroldan/data/repository) se leen y escriben en archivos JSON locales (usando Gson).
IAutorizacionRepo: Verifica si un Tutor tiene permiso para recoger a un Alumno específico.
- JsonPersistence.kt: Es la herramienta que ayuda a convertir los objetos Kotlin a texto JSON y viceversa.

Capa de Lógica de Interfaz: [ViewModels](https://github.com/irolram/ProyectoFinalIvanRoldan/tree/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel) Actúan como el "cerebro" de cada pantalla.
- [LoginViewModel](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel/LoginViewModel.kt): Recibe las credenciales, consulta al UsuarioRepository y le dice a la vista si el acceso es correcto o si hay un error. Maneja el estado de la sesión.
- [AdminViewModel](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel/AdminViewModel.kt): Gestiona la lógica de crear/borrar usuarios y alumnos. También contiene la función de generar el informe CSV.

Capa de Vista: Screens y ComponentsEs lo que el usuario ve y toca (Jetpack Compose).
- [LoginScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/tree/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/login): Recoge los datos del usuario. Al tener éxito, el flujo vuelve a MainActivity para redirigir según el Rol.

- [AdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt): Interfaz con pestañas. Usa diálogos ([UserDialogScreen](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/UserDialogScreen.kt), etc.) para modificar los datos.
- [TutorScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/TutorScreen.kt): Usa el [QrGenerator.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/util/QrGenerator.kt) para convertir el ID del tutor en una imagen QR. Muestra la lista de sus alumnos vinculados.
- [ConserjeScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt): Usa la cámara mediante CameraX.◦Conecta con el [QrAnalyzer.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/util/QRAnalyzer.kt), que usa ML Kit para "leer" el código. Una vez leído el ID, consulta al repositorio para confirmar si el tutor es válido y qué alumnos puede llevarse.


# Manual de Usuario: Sistema de Gestión de Recogida Escolar

Este documento describe el funcionamiento de la aplicación diseñada para gestionar de forma segura la salida de alumnos del centro escolar mediante códigos QR.

- Acceso al Sistema (Login)
Al abrir la aplicación, se presentará la pantalla de inicio de sesión.

- Introduce tu Usuario y Contraseña.

El instituto le deberá de dar su usuario y contraseña con la cual luego deberá iniciar sesión
La aplicación detectará automáticamente tu perfil (Administrador, Tutor o Conserje) y te dirigirá a tu panel correspondiente.

1- Perfil: Administrador 
- El administrador es el encargado de gestionar la base de datos del centro. El panel se divide en tres pestañas:

A. Gestión de Usuarios

- Visualizar: Lista de todos los usuarios registrados (Tutores, Conserjes y otros Admins).

- Añadir: Pulsa el botón flotante + para crear un nuevo usuario asignándole un nombre, nombre de usuario, contraseña y rol.

- Eliminar: Pulsa el icono de la papelera en el usuario que desees dar de baja.

B. Gestión de Alumnos
- Visualizar: Lista de alumnos matriculados y su curso.

- Añadir: Pulsa el botón + para registrar un nuevo alumno con su nombre completo y curso actual.

- Eliminar: Pulsa el icono de la papelera para borrar el registro de un alumno.
C. Vínculos (Relación Tutor-Alumno)

- Propósito: Aquí se define qué tutor tiene permiso para recoger a qué alumno.

- Crear Vínculo: Pulsa +, selecciona un tutor de la lista y el alumno al que está autorizado a recoger.

- Eliminar Vínculo: Pulsa  para revocar un permiso de recogida específico.


2- Perfil: Tutor 
- El tutor dispone de una interfaz simplificada para gestionar la recogida de sus menores.

- En la pestaña "Mis alumnos" aparecerá la lista de alumnos que tienes autorizados para recoger, junto con su curso.
- Pase QR (Generación de Código)

- Selecciona la pestaña "Pase QR" en la barra inferior.

- Se generará un Código QR único vinculado a tu identidad.

- Uso: Debes mostrar este código en la pantalla de tu móvil al personal de conserjería cuando llegues al centro.

3-  Perfil: Conserje 

El conserje es el encargado de validar las salidas físicamente en la puerta del centro.

- Escaneo de Seguridad

Al entrar, se activará la cámara del dispositivo (es necesario aceptar el permiso de cámara).

- Validación: Enfoca el código QR que presente el tutor.

- Resultados:

- Acceso Autorizado: Se mostrará un mensaje en verde y aparecerá la lista de alumnos que esa persona tiene permiso para recoger. El conserje podrá verificar visualmente a los niños antes de permitir la salida.

- Acceso Denegado: Se mostrará un mensaje en rojo si el código no es válido o el tutor no tiene alumnos vinculados.

- Siguiente: Pulsa el botón "Limpiar y siguiente" para resetear el escáner para el próximo tutor.
  
4- Recomendaciones de Seguridad 

No compartas tus credenciales: Cada usuario es responsable de las acciones realizadas con su cuenta.

Privacidad del QR: El código QR es personal. No envíes capturas de pantalla a personas no autorizadas.

Cerrar Sesión: Si utilizas un dispositivo compartido, asegúrate de pulsar el botón de Salir (icono de puerta) para cerrar tu sesión al finalizar.


