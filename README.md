# SafePick: Sistema de recogida de alumnos

# Motivación

El origen de este proyecto nace de una experiencia personal muy cercana. Al observar el trabajo diario de mi madre como conserje en un centro educativo, identifiqué una problemática crítica en el proceso de recogida de alumnos: la dificultad para verificar, de forma rápida y segura, la identidad de los tutores legales.

Actualmente, este proceso depende en gran medida de la memoria visual del personal o de listados en papel, lo que conlleva un alto riesgo de error humano y una gran presión administrativa ante posibles entregas no autorizadas. Mi objetivo principal ha sido desarrollar una solución tecnológica que elimine esta incertidumbre, agilizando el flujo de trabajo del personal de conserjería y garantizando la seguridad de los menores, transformando una tarea estresante y lenta en un proceso automatizado y fiable.

# Flujo del programa

**El Motor de Arranque:** [`MainActivity.kt`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt) es el punto de entrada. Su función es orquestar:
* **Inicializa los Repositorios:** Crea las instancias que manejan los datos (UsuarioRepository, AlumnoRepository, etc.).
* **Configura la Navegación (NavHost):** Define las "rutas" de la app (login, admin, tutor, conserje).
* **Inyecta Dependencias:** Pasa los repositorios a los ViewModels para que estos puedan funcionar.

**Capa de Datos:** Aquí reside la información. No saben nada de la interfaz gráfica.
* **Modelos** ([`Usuario`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Usuario.kt), [`Alumno`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Alumno.kt), [`Vinculo`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Vinculo.kt)): Clases simples que definen qué datos tiene cada entidad.
* **Repositorios:** En la carpeta [`repository`](https://github.com/irolram/ProyectoFinalIvanRoldan/tree/main/app/src/main/java/com/example/proyectofinalivanroldan/data/repository) se leen y escriben en archivos JSON locales (usando Gson).
    * `IAutorizacionRepo`: Verifica si un Tutor tiene permiso para recoger a un Alumno específico.
    * `JsonPersistence.kt`: Es la herramienta que ayuda a convertir los objetos Kotlin a texto JSON y viceversa.

**Capa de Lógica de Interfaz:** [ViewModels](https://github.com/irolram/ProyectoFinalIvanRoldan/tree/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel). Actúan como el "cerebro" de cada pantalla.
* [`LoginViewModel`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel/LoginViewModel.kt): Recibe las credenciales, consulta al `UsuarioRepository` y le dice a la vista si el acceso es correcto o si hay un error. Maneja el estado de la sesión.
* [`AdminViewModel`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel/AdminViewModel.kt): Gestiona la lógica de crear/borrar usuarios y alumnos. También contiene la función de generar el informe CSV.

**Capa de Vista:** Screens y Components. Es lo que el usuario ve y toca (Jetpack Compose).
* [`LoginScreen.kt`](https://github.com/irolram/ProyectoFinalIvanRoldan/tree/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/login): Recoge los datos del usuario. Al tener éxito, el flujo vuelve a `MainActivity` para redirigir según el Rol.
* [`AdminScreen.kt`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt): Interfaz con pestañas. Usa diálogos ([`UserDialogScreen`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/UserDialogScreen.kt), etc.) para modificar los datos.
* [`TutorScreen.kt`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/TutorScreen.kt): Usa el [`QrGenerator.kt`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/util/QrGenerator.kt) para convertir el ID del tutor en una imagen QR. Muestra la lista de sus alumnos vinculados.
* [`ConserjeScreen.kt`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt): Usa la cámara mediante CameraX. Conecta con el [`QrAnalyzer.kt`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/util/QRAnalyzer.kt), que usa ML Kit para "leer" el código. Una vez leído el ID, consulta al repositorio para confirmar si el tutor es válido y qué alumnos puede llevarse.

# Manual de Usuario: Sistema de Gestión de Recogida Escolar

Este documento describe el funcionamiento de la aplicación diseñada para gestionar de forma segura la salida de alumnos del centro escolar mediante códigos QR.

### Acceso al Sistema (Login)
Al abrir la aplicación, se presentará la pantalla de inicio de sesión.
1.  **Introduce tu Usuario y Contraseña.**
    * El instituto le deberá de dar su usuario y contraseña con la cual luego deberá iniciar sesión.
2.  La aplicación detectará automáticamente tu perfil (Administrador, Tutor o Conserje) y te dirigirá a tu panel correspondiente.

### 1. Perfil: Administrador
El administrador es el encargado de gestionar la base de datos del centro. El panel se divide en tres pestañas:

**A. Gestión de Usuarios**
* **Visualizar:** Lista de todos los usuarios registrados (Tutores, Conserjes y otros Admins).
* **Añadir:** Pulsa el botón flotante `+` para crear un nuevo usuario asignándole un nombre, nombre de usuario, contraseña y rol.
* **Eliminar:** Pulsa el icono de la papelera en el usuario que desees dar de baja.

**B. Gestión de Alumnos**
* **Visualizar:** Lista de alumnos matriculados y su curso.
* **Añadir:** Pulsa el botón `+` para registrar un nuevo alumno con su nombre completo y curso actual.
* **Eliminar:** Pulsa el icono de la papelera para borrar el registro de un alumno.

**C. Vínculos (Relación Tutor-Alumno)**
* **Propósito:** Aquí se define qué tutor tiene permiso para recoger a qué alumno.
* **Crear Vínculo:** Pulsa `+`, selecciona un tutor de la lista y el alumno al que está autorizado a recoger.
* **Eliminar Vínculo:** Pulsa el icono de borrar para revocar un permiso de recogida específico.

### 2. Perfil: Tutor
El tutor dispone de una interfaz simplificada para gestionar la recogida de sus menores.
* En la pestaña "Mis alumnos" aparecerá la lista de alumnos que tienes autorizados para recoger, junto con su curso.
* **Pase QR (Generación de Código):**
    1.  Selecciona la pestaña "Pase QR" en la barra inferior.
    2.  Se generará un Código QR único vinculado a tu identidad.
    3.  **Uso:** Debes mostrar este código en la pantalla de tu móvil al personal de conserjería cuando llegues al centro.

### 3. Perfil: Conserje
El conserje es el encargado de validar las salidas físicamente en la puerta del centro.
* **Escaneo de Seguridad:** Al entrar, se activará la cámara del dispositivo (es necesario aceptar el permiso de cámara).
* **Validación:** Enfoca el código QR que presente el tutor.
* **Resultados:**
    * **Acceso Autorizado:** Se mostrará un mensaje en verde y aparecerá la lista de alumnos que esa persona tiene permiso para recoger. El conserje podrá verificar visualmente a los niños antes de permitir la salida.
    * **Acceso Denegado:** Se mostrará un mensaje en rojo si el código no es válido o el tutor no tiene alumnos vinculados.
* **Siguiente:** Pulsa el botón "Limpiar y siguiente" para resetear el escáner para el próximo tutor.

### 4. Recomendaciones de Seguridad
* **No compartas tus credenciales:** Cada usuario es responsable de las acciones realizadas con su cuenta.
* **Privacidad del QR:** El código QR es personal. No envíes capturas de pantalla a personas no autorizadas.
* **Cerrar Sesión:** Si utilizas un dispositivo compartido, asegúrate de pulsar el botón de Salir (icono de puerta) para cerrar tu sesión al finalizar.

# Criterios de Evaluación

## RA1 — Interfaz, estructura y comportamiento de la aplicación
Para el desarrollo de la interfaz y la lógica visual, se han seleccionado herramientas modernas que garantizan escalabilidad y rendimiento nativo:

* **Jetpack Compose:** Se ha descartado el sistema clásico de XML en favor de Compose para crear una UI declarativa, lo que reduce el código repetitivo y facilita la reactividad del estado (ej: cambiar de pantalla roja a verde instantáneamente).
* **Material Design 3:** Librería de componentes visuales que asegura coherencia estética y accesibilidad (colores dinámicos, tipografía adaptable).
* **CameraX:** Seleccionada sobre la API de cámara antigua por su compatibilidad con el ciclo de vida de la actividad.
* **ZXing Android Embedded:** Librería optimizada para la decodificación de matrices de datos (QR) en tiempo real.

**Ubicación:**
[build.gradle.kts (Líneas 44-73)](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/bd15794166b70649ca5fcf2f7a1a94c9aff90b75/app/build.gradle.kts#L44-L73)

## RA1.b – Crea interfaz gráfica

La estructura de la interfaz se define en el punto de entrada de la aplicación, utilizando **Jetpack Compose Navigation**. En lugar de múltiples actividades, se utiliza una "Single Activity Architecture" donde un `NavHost` gestiona el intercambio de pantallas (Login, Admin, Tutor, Conserje) basándose en el estado de autenticación y el rol del usuario.

* **Gestión de Estado:** Los ViewModels (`LoginViewModel`, `AdminViewModel`) se instancian al inicio y se pasan a las pantallas correspondientes.
* **Navegación Condicional:** El grafo de navegación decide qué pantalla mostrar (`startDestination = "login"`) y maneja la redirección post-login mediante lambdas (`onLoginSuccess`).

**Dónde ocurre en el código:**
* **Estructura global:** [`MainActivity.kt`](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/bd15794166b70649ca5fcf2f7a1a94c9aff90b75/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt#L1-L122)

## RA1.c – Uso de layouts y posicionamiento

Se ha implementado un diseño declarativo utilizando los contenedores nativos de Jetpack Compose para organizar los elementos de forma eficiente y responsiva:

* **Estructura Base (Scaffold):** Se utiliza en todas las pantallas principales para proporcionar "slots" estandarizados para la barra superior (`TopAppBar`), el botón flotante (`FloatingActionButton`) y el contenido principal, gestionando automáticamente los márgenes seguros del sistema (`paddingValues`).
* **Listas Eficientes (LazyColumn):** En la `AdminScreen`, se utiliza `LazyColumn` en lugar de un `Column` con scroll. Esto permite renderizar solo los elementos visibles en pantalla, optimizando la memoria cuando la lista de usuarios o alumnos crece.
* **Superposición (Box):** Fundamental en la `ConserjeScreen`. El layout `Box` permite apilar elementos en el eje Z. Se usa para superponer la interfaz de usuario (mensajes de "Escaneando..." o bordes de colores) encima de la vista previa de la cámara (`AndroidView`).
* **Alineación Flex (Column / Row):** En `LoginScreen`, se combinan columnas y filas con modificadores de peso (`weight`) y arreglos (`Arrangement.Center`, `Alignment.CenterHorizontally`) para centrar el formulario independientemente del tamaño de la pantalla del dispositivo.

**Funciones Clave:**
* **AdminScreen():** Implementa `LazyColumn` para el listado.
    https://github.com/irolram/ProyectoFinalIvanRoldan/blob/bd15794166b70649ca5fcf2f7a1a94c9aff90b75/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt#L35-L193
* **ConserjeScreen():** Implementa `Box` para la cámara.
    https://github.com/irolram/ProyectoFinalIvanRoldan/blob/bd15794166b70649ca5fcf2f7a1a94c9aff90b75/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt#L79-L141
* **LoginScreen():** Implementa `Column` con alineación centrada.
    https://github.com/irolram/ProyectoFinalIvanRoldan/blob/bd15794166b70649ca5fcf2f7a1a94c9aff90b75/app/src/main/java/com/example/proyectofinalivanroldan/ui/login/LoginScreen.kt#L47-L104

## RA1.d – Personalización de componentes

Se ha implementado un sistema de diseño propio (**Design System**) que personaliza la librería Material Design 3 para adaptarla a la identidad y funcionalidad de SafePick, garantizando coherencia visual y usabilidad:

* **Arquitectura de Componentes Reutilizables:**
    En lugar de duplicar código en las vistas, se han extraído elementos comunes a componentes parametrizables. Destaca el componente `AdminItemCard` (en `AdminScreen.kt`), que unifica el diseño de las tarjetas de Usuarios, Alumnos y Vínculos. Este componente encapsula la lógica de diseño (bordes redondeados de 12.dp, elevación, iconos con fondo circular) y lógica de negocio (ocultar el botón de borrado mediante el parámetro `isDeletable` para proteger al usuario administrador).

* **Sistema de Temas Dinámico (Theming):**
    La aplicación implementa un tema personalizado `SafePickTheme` (en `Theme.kt`). Este sistema detecta la configuración del dispositivo mediante `isSystemInDarkTheme()` y alterna automáticamente entre paletas de colores de alto contraste (`LightColorScheme` y `DarkColorScheme`), asegurando la legibilidad tanto en entornos de oficina como en exteriores nocturnos (guardia del conserje).

* **Feedback Visual Semántico:**
    La personalización no es solo estética, sino funcional. Se utilizan colores semánticos definidos en `Color.kt`:
    * **Container/Surface:** Uso de contenedores con distintos tonos para jerarquizar la información.
    * **Error:** Uso del color rojo (`ColorScheme.error`) en los iconos de borrado y mensajes de alerta.
    * **Primary:** Uso del color primario de la marca en elementos interactivos clave como el `FloatingActionButton`.

**Funciones clave:**
[AdminItemCard - Ver en GitHub](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/d526d4f7ba6d02054715f5ad77955585181ca831/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt#L228-L292)
[Theme - Ver en GitHub](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/45b299f32845ed203c179fe9347f07840ddd919c/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt#L57-L59)

<img width="237" height="512" alt="image" src="https://github.com/user-attachments/assets/f499f555-3cc9-48ca-b9ea-32b584ca7518" />
<img width="240" height="498" alt="image" src="https://github.com/user-attachments/assets/9fcc6eaf-5c8c-491d-91b8-df1a70b9181d" />

## RA1.e — Análisis del código

**Descripción y justificación**

El código del proyecto SafePick se estructura siguiendo el patrón de arquitectura **MVVM (Model-View-ViewModel)** junto con el patrón **Repository**, garantizando una clara separación de responsabilidades y un flujo de datos unidireccional:

* **Capa de Presentación (UI Declarativa):** Implementada con **Jetpack Compose**. Las vistas (`Screens`) son funciones "sin estado" que se limitan a renderizar la información proporcionada por los ViewModels. Se utiliza el patrón de **Elevación de Estado (State Hoisting)**, donde los eventos (clicks, entradas de texto) suben hacia el ViewModel y el estado baja hacia la vista, asegurando que la UI sea puramente reactiva.
* **Capa de Lógica y Estado (ViewModel):** Los ViewModels (`AdminViewModel`, `LoginViewModel`) actúan como gestores de estado. No contienen referencias a las vistas (evitando fugas de memoria) y exponen los datos mediante flujos observables (`StateFlow`). Esto permite que la interfaz se actualice automáticamente ante cualquier cambio en los datos sin necesidad de manipulación manual del DOM o `findViewById`.
* **Capa de Datos (Repositorios):** La persistencia de datos se abstrae mediante Repositorios (`UsuarioRepository`, `VinculoRepository`). Esta capa encapsula la lógica de lectura y escritura en el sistema de ficheros local (JSON Parsing).

**Justificación:** Al centralizar el acceso a datos en repositorios, el resto de la aplicación desconoce si los datos vienen de una API, una base de datos SQL o un archivo de texto. Esto hace que el código sea escalable y facilita futuras migraciones (por ejemplo, pasar de JSON a una base de datos Room) sin romper la interfaz gráfica.

**Conclusión:** Esta arquitectura modular facilita el mantenimiento, ya que la lógica de negocio está desacoplada de la interfaz visual, y permite la inyección de dependencias manual a través de `ViewModelFactory` para una gestión eficiente de los recursos compartidos.

## RA1.f – Modificación del código

**Calificación Objetivo:** Cambios creativos y justificados (2 puntos).

Durante el desarrollo, el código base ha evolucionado significativamente mediante técnicas de refactorización para mejorar la robustez y la mantenibilidad del proyecto. Se destacan tres modificaciones críticas respecto a una implementación estándar:

**1. Refactorización a Componentes Polimórficos (DRY)**
* **Situación Inicial:** El archivo `AdminScreen.kt` contenía bloques de código repetidos (`Card { ... }`) para mostrar usuarios, alumnos y vínculos, lo que dificultaba el mantenimiento.
* **Modificación:** Se implementó el patrón de Composición creando un componente único y reutilizable: `AdminItemCard`.
* **Justificación:**
    * **Escalabilidad:** Al modificar el diseño de este componente único (ej: cambiar el redondeo de las esquinas), el cambio se propaga automáticamente a las tres listas de gestión, garantizando coherencia visual inmediata.

**2. Programación Defensiva en la Interfaz (Seguridad)**
* **Situación Inicial:** Todos los elementos de la lista incluían incondicionalmente un botón de borrado, permitiendo accidentalmente eliminar al usuario "Administrador" y bloquear el acceso a la App.
* **Modificación:** Se inyectó lógica condicional en la UI.
    ```kotlin
    val isDeletable = !usuario.username.equals("admin", ignoreCase = true)
    ```
    Se modificó el componente visual para aceptar un parámetro booleano `isDeletable`. Si este es falso, el botón de la papelera no se renderiza.
* **Justificación:** Previene errores críticos de usuario ("User Lockout") desde la propia capa de presentación, mejorando la seguridad del sistema.

**3. Inyección de Resiliencia de Datos**
* **Situación Inicial:** Al borrar los datos de la aplicación o instalarla por primera vez, la base de datos (JSON) estaba vacía, impidiendo el inicio de sesión.
* **Modificación:** Se modificó el ciclo de vida `onCreate` en `MainActivity.kt` para incluir una comprobación de arranque.
* **Justificación:** El sistema ahora se "autorepara". Si detecta que no existen usuarios, inyecta automáticamente las credenciales de administrador por defecto, facilitando el despliegue y las pruebas sin necesidad de configuración manual externa.

**4. Integración Reactiva del Tema (Theming)**
* **Situación Inicial:** El uso de colores fijos (`Color.White`, `Color.Black`) impedía la adaptación al entorno.
* **Modificación:** Se reemplazaron todas las referencias estáticas por referencias semánticas del tema (`MaterialTheme.colorScheme.surface`). Además, se envolvió el `NavHost` dentro de un `Surface` contenedor en la actividad principal.
* **Justificación:** Permite que la aplicación responda nativamente al cambio de modo claro/oscuro del sistema operativo sin reiniciar la actividad, cumpliendo con los estándares de accesibilidad modernos.

<img width="420" height="530" alt="image" src="https://github.com/user-attachments/assets/27a2bb13-0220-4166-99c1-1ab0a2e6f5b8" />

## RA1.g — Asociación de eventos

**Descripción y justificación**

La aplicación implementa un modelo de **Gestión de Eventos Reactiva** basado en el patrón de "Elevación de Estado" (State Hoisting). Las acciones del usuario no modifican la interfaz directamente, sino que disparan eventos que son procesados por los ViewModels, garantizando un flujo de datos unidireccional y predecible.

* **Eventos de Interfaz (Clicks y Navegación):**
    Se utilizan expresiones Lambda y funciones de orden superior para desacoplar la vista de la lógica.
    * *Ejemplo:* En el Login, el evento `onClick` del botón no realiza la autenticación; invoca una función suspendida en el `LoginViewModel` (`viewModel.login()`), la cual gestiona la asincronía y actualiza el estado de la UI (Cargando -> Éxito/Error).

* **Eventos de Hardware (Sensores y Cámara):**
    La aplicación gestiona eventos complejos de hardware en tiempo real.
    * *Ejemplo:* En el módulo del Conserje, el evento no es provocado por un "click", sino por la detección de un patrón visual. El `ImageAnalysis` dispara un Callback (`onQrDetected`) solo cuando reconoce un código QR válido, transformando una señal de hardware en una acción lógica (validar alumno) y devolviendo feedback visual instantáneo (semáforo verde/rojo).

* **Eventos de Confirmación (Diálogos):**
    Para acciones destructivas o de creación, se gestionan eventos de ciclo de vida de los diálogos.
    * *Ejemplo:* En el Panel de Administración, los eventos `onDismiss` (cancelar) y `onConfirm` (guardar) permiten transacciones seguras, asegurando que los datos solo se persistan en el repositorio JSON cuando el usuario valida explícitamente la intención.

**Dónde ocurre en el código:**

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/f75ae5833d280ced45a8658a8a1450384ac39fee/app/src/main/java/com/example/proyectofinalivanroldan/ui/login/LoginScreen.kt#L80-L86

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/f75ae5833d280ced45a8658a8a1450384ac39fee/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt#L117-L123

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/f75ae5833d280ced45a8658a8a1450384ac39fee/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt#L227-L292

## RA1.h — Aplicación integrada

**Descripción y justificación**

SafePick funciona como un sistema cohesionado y robusto donde todos los módulos operan sobre una única fuente de verdad. El flujo de la aplicación garantiza una experiencia continua:

1.  **Unificación de Roles:** La aplicación integra tres perfiles distintos (Administrador, Tutor, Conserje) en un solo ejecutable. El sistema de enrutamiento detecta el rol tras la autenticación y despacha al usuario a su interfaz específica sin fricción.
2.  **Consistencia de Datos:** La integración no es solo visual, sino lógica. Cuando un Administrador registra un alumno o genera un vínculo, esa información está inmediatamente disponible para el Conserje (para validar el escaneo QR) y para el Tutor (para ver a su hijo), gracias a la inyección de repositorios compartidos (`UsuarioRepository`, `AlumnoRepository`).
3.  **Ciclo de Vida de Sesión:** El sistema gestiona la seguridad de la navegación, impidiendo el retorno a pantallas protegidas tras el cierre de sesión (`Logout`), garantizando que la aplicación se comporte como un producto profesional y seguro.

**Dónde ocurre en el código:**

* **Integración Global y Enrutamiento (MainActivity):**
    Es el "cerebro" que conecta el Login con los distintos módulos según el rol.
    https://github.com/irolram/ProyectoFinalIvanRoldan/blob/c83b1de680ec07de4618cae4acb6a2a7ebf685ef/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt#L32-L148

* **Interconexión de Funcionalidades (Admin -> Datos -> Conserje):**
    El sistema demuestra integración porque las acciones del Admin (crear alumno) habilitan las acciones del Conserje (leer alumno). Esto ocurre gracias a que comparten los mismos Repositorios.
    [AdminViewModel](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/c83b1de680ec07de4618cae4acb6a2a7ebf685ef/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel/AdminViewModel.kt#L50-L53)
    [ConserjeScreen](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/c83b1de680ec07de4618cae4acb6a2a7ebf685ef/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt#L44-L48)

**Evidencias:**

<img width="339" height="752" alt="image" src="https://github.com/user-attachments/assets/bc674ed1-bb16-4609-9348-c143beb1cc9e" />
<img width="358" height="799" alt="image" src="https://github.com/user-attachments/assets/0958fbdc-9c16-494a-b5e9-6b4aeb1a863c" />
<img width="358" height="765" alt="image" src="https://github.com/user-attachments/assets/d3fdee1d-9adc-4329-90d6-4241a4e793d2" />

## RA2 — Interfaces Naturales de Usuario (NUI)

**Descripción y justificación**

En este apartado se analizan las interfaces naturales de usuario (NUI) integradas en el ecosistema **SafePick**. El objetivo principal ha sido trascender la interacción tradicional basada en formularios y teclados (GUI), incorporando mecanismos de **Visión Artificial** que hacen el uso de la aplicación más intuitivo, rápido y cercano a la realidad física del centro escolar.

La aplicación está orientada a un entorno crítico: el **control de seguridad y recogida de alumnos**, un escenario donde la velocidad y la precisión son vitales para evitar aglomeraciones en las puertas del colegio. Por ello, se ha priorizado la reducción de la fricción cognitiva y motora.

## RA2.a — Herramientas NUI

**Análisis y justificación**

Durante el desarrollo de **SafePick** se ha realizado un análisis consciente de las herramientas de Interfaz Natural de Usuario (NUI) disponibles en el ecosistema Android, seleccionando aquellas que aportan agilidad operativa en un entorno de seguridad escolar.

La principal herramienta NUI implementada es la **Visión por Computador** a través de la librería nativa **CameraX** y su caso de uso **ImageAnalysis**.
* **Justificación:** Esta herramienta permite transformar la cámara del dispositivo en un sensor activo. En lugar de obligar al conserje a introducir manualmente un código de identificación (lo que sería lento y propenso a errores), el sistema "lee" el entorno. Esto elimina la barrera entre el mundo físico (el carnet del padre) y el digital (la base de datos).

De forma complementaria, se han utilizado los **Gestos Táctiles Nativos** proporcionados por **Jetpack Compose**:
* **Scroll Inercial (Vertical Swipe):** Implementado en las listas `LazyColumn` del panel de administración, permitiendo navegar por cientos de registros de forma natural mediante deslizamiento.
* **Tap & Long Press:** Interacciones directas sobre las tarjetas (`Card`) para expandir información o ejecutar acciones de borrado.

Se han analizado otras herramientas NUI relevantes, descartadas para esta versión pero viables para el futuro:
* **BiometricPrompt:** Para el inicio de sesión del administrador mediante huella dactilar.
* **SpeechRecognizer:** Para permitir al conserje dictar incidencias por voz.
* **ML Kit (Face Detection):** Para reconocimiento facial de alumnos (descartado por privacidad de menores).

### Dónde ocurre en el proyecto

* **Visión Artificial (Image Analysis):** `ConserjeScreen.kt`
    Este es el núcleo NUI del proyecto. El código no solo muestra la cámara, sino que analiza cada fotograma en tiempo real para buscar patrones QR.
  https://github.com/irolram/ProyectoFinalIvanRoldan/blob/c83b1de680ec07de4618cae4acb6a2a7ebf685ef/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt#L112-L123

* **Gestos Táctiles (Scroll y Click):** `AdminScreen.kt`
    El uso de `LazyColumn` implementa automáticamente el gesto de deslizamiento vertical (Swipe) con física de inercia, fundamental para manejar listas largas de alumnos.
    https://github.com/irolram/ProyectoFinalIvanRoldan/blob/c83b1de680ec07de4618cae4acb6a2a7ebf685ef/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt#L159-L178

## RA2.b — Diseño conceptual NUI

**Diseño y enfoque conceptual**

El diseño conceptual de las interfaces NUI en **SafePick** se basa en la **interacción con el entorno físico** mediante la cámara, priorizando la velocidad de operación sobre la introducción de datos tradicional.

El objetivo principal es adaptar la interfaz al contexto real del usuario:
1.  **Conserje (Entorno de alta movilidad):** Se sustituye el teclado por el **escaneo visual**. En la puerta de un colegio, escribir nombres es inviable; "apuntar y validar" es la interacción natural óptima.
2.  **Administrador (Entorno de gestión):** Se mantiene la interacción táctil clásica (scroll y tap) para garantizar la precisión en el manejo de datos sensibles.

Este enfoque no es intrusivo: la aplicación sabe cuándo desplegar la interfaz NUI (cámara) y cuándo mantener la interfaz GUI estándar (formularios), dependiendo del rol del usuario logueado.

**Dónde ocurre en el proyecto**

* **Interacción Visual (Machine Vision):** `ConserjeScreen.kt`
    La cámara actúa como el "ojo" del sistema. El diseño conceptual aquí es que la pantalla desaparece como interfaz y se convierte en una ventana a la realidad aumentada con información superpuesta.
    https://github.com/irolram/ProyectoFinalIvanRoldan/blob/0101fd17c2d2cc1f892b55bfade8e320c565d05c/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt#L117-L122

* **Navegación Gestual (Inercia):** `AdminScreen.kt`
    Se aprovecha la física del **Scroll Inercial** nativo de Android para navegar por listas extensas de alumnos sin necesidad de botones de paginación "Siguiente/Anterior".
    https://github.com/irolram/ProyectoFinalIvanRoldan/blob/0101fd17c2d2cc1f892b55bfade8e320c565d05c/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt#L181-L195

## RA2.c — Interacción por voz

**Análisis y Justificación de la No-Implementación**

Tras evaluar la viabilidad técnica y operativa, se ha decidido **excluir explícitamente** la interacción por comandos de voz (`SpeechRecognizer`) en la versión final de **SafePick**. Esta decisión de diseño responde a tres factores críticos identificados durante el análisis del entorno real de despliegue (la puerta de un centro escolar):

1.  **Contaminación Acústica (Fiabilidad):**
    El entorno de uso principal (salida del colegio) se caracteriza por un nivel de ruido ambiente elevado (gritos, tráfico, conversaciones). Las pruebas teóricas indican que la tasa de error (Word Error Rate - WER) de las APIs de reconocimiento de voz estándar aumentaría drásticamente en este escenario, causando frustración en el conserje.
2.  **Privacidad y Protección de Datos (LOPD):**
    La interacción por voz obligaría al personal a verbalizar nombres de alumnos y tutores en un espacio público. Para garantizar la privacidad de los menores y cumplir con la normativa de protección de datos, se ha optado por una interacción **silenciosa y visual** (Escaneo QR).
3.  **Eficiencia Operativa:**
    El flujo de "Pulsar micrófono -> Hablar -> Procesar -> Confirmar texto" es significativamente más lento (aprox. 5-8 segundos) que el flujo implementado de "Apuntar cámara -> Validación automática" (< 1 segundo).

**Alternativa NUI Implementada**

En lugar de la voz, se ha potenciado la **Visión Artificial (Machine Vision)** como la interfaz natural principal. Esta tecnología cumple el mismo objetivo (evitar escribir manualmente) pero de una forma más robusta, privada y adecuada al contexto ruidoso del centro educativo.

### Dónde ocurre en el proyecto (Alternativa Visual)

Aunque no existe código de reconocimiento de voz, la lógica que sustituye esta necesidad (la identificación sin teclado) se encuentra en el módulo de escaneo:

* **Escaneo Silencioso:** `ConserjeScreen.kt`
    El sistema escucha "imágenes" en lugar de "audio" para recibir instrucciones.
    https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt

## RA2.d — Interacción por gesto

**Implementación y Experiencia de Usuario**

La interacción por gestos en **SafePick** se ha centrado en la **Navegación Vertical Inercial (Vertical Scroll/Fling)**, descartando gestos horizontales complejos para acciones críticas por motivos de seguridad.

1.  **Gesto Implementado: Deslizamiento Vertical (Scroll):**
    * Se ha implementado el gesto de arrastre vertical estándar de Android mediante el componente `LazyColumn`.
    * **Experiencia:** Este gesto permite al administrador navegar por listas extensas de alumnos o usuarios de manera fluida. Incorpora física de inercia (Fling), donde la lista continúa moviéndose tras soltar el dedo y se detiene progresivamente, proporcionando una sensación táctil natural y esperada en interfaces móviles modernas.

2.  **Justificación de Exclusión (Swipe-to-Delete):**
    * Durante el diseño, se evaluó implementar el gesto de "Deslizar para Borrar" (Swipe-to-Dismiss). Sin embargo, se **descartó** conscientemente.
    * **Motivo:** En una base de datos escolar, la eliminación accidental de un registro (un alumno o un tutor) es un error crítico que podría impedir la recogida de un menor. Los gestos de deslizamiento son propensos a activaciones accidentales al intentar hacer scroll.
    * **Solución:** Se optó por una interacción explícita y deliberada (Click en icono de papelera + Confirmación), priorizando la integridad de los datos sobre la rapidez de borrado.

### Dónde ocurre en el proyecto

**Navegación Vertical Inercial:** `AdminScreen.kt`

El contenedor `LazyColumn` encapsula la lógica de detección de gestos de arrastre (DragGestures) y la física de movimiento sin necesidad de implementación manual.

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt
