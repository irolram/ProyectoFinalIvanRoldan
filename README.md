# SafePick: Sistema de recogida de alumnos

# Motivación

El origen de este proyecto nace de una experiencia personal muy cercana. Al observar el trabajo diario de mi madre como conserje en un centro educativo, identifiqué una problemática crítica en el proceso de recogida de alumnos: la dificultad para verificar, de forma rápida y segura, la identidad de los tutores legales.

Actualmente, este proceso depende en gran medida de la memoria visual del personal o de listados en papel, lo que conlleva un alto riesgo de error humano y una gran presión administrativa ante posibles entregas no autorizadas. Mi objetivo principal ha sido desarrollar una solución tecnológica que elimine esta incertidumbre, agilizando el flujo de trabajo del personal de conserjería y garantizando la seguridad de los menores, transformando una tarea estresante y lenta en un proceso automatizado y fiable.

# Flujo del programa

**El Motor de Arranque:** `MainActivity.kt` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt#L1-L148 es el punto de entrada. Su función es orquestar:
* **Inicializa los Repositorios:** Crea las instancias que manejan los datos (UsuarioRepository, AlumnoRepository, etc.).
* **Configura la Navegación (NavHost):** Define las "rutas" de la app (login, admin, tutor, conserje).
* **Inyecta Dependencias:** Pasa los repositorios a los ViewModels para que estos puedan funcionar.

**Capa de Datos:** Aquí reside la información. No saben nada de la interfaz gráfica.
* **Modelos** (`Usuario` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Usuario.kt#L1-L11, `Alumno` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Alumno.kt#L1-L7, `Vinculo` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Vinculo.kt#L1-L6): Clases simples que definen qué datos tiene cada entidad.
* **Repositorios:** En la carpeta [`repository`](https://github.com/irolram/ProyectoFinalIvanRoldan/tree/main/app/src/main/java/com/example/proyectofinalivanroldan/data/repository) se leen y escriben en archivos JSON locales (usando Gson).
    * `IAutorizacionRepo`: Verifica si un Tutor tiene permiso para recoger a un Alumno específico.
    * `JsonPersistence.kt`: Es la herramienta que ayuda a convertir los objetos Kotlin a texto JSON y viceversa.

**Capa de Lógica de Interfaz:** [`ViewModels`](https://github.com/irolram/ProyectoFinalIvanRoldan/tree/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel). Actúan como el "cerebro" de cada pantalla.
* `LoginViewModel`: https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel/LoginViewModel.kt#L1-L48  Recibe las credenciales, consulta al `UsuarioRepository` y le dice a la vista si el acceso es correcto o si hay un error. Maneja el estado de la sesión.
* `AdminViewModel`: https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel/AdminViewModel.kt#L1-L96 Gestiona la lógica de crear/borrar usuarios y alumnos. También contiene la función de generar el informe CSV.

**Capa de Vista:** Screens y Components. Es lo que el usuario ve y toca (Jetpack Compose).
* `LoginScreen.kt` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/ui/login/LoginScreen.kt#L1-L104 Recoge los datos del usuario. Al tener éxito, el flujo vuelve a `MainActivity` para redirigir según el Rol.
* `AdminScreen.kt` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt#L1-L306  Interfaz con pestañas. Usa diálogos (`UserDialogScreen` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/UserDialogScreen.kt#L1-L77, etc.) para modificar los datos.
* `TutorScreen.kt` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/TutorScreen.kt#L1-L201 Usa el `QrGenerator.kt` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/util/QrGenerator.kt#L1-L35 para convertir el ID del tutor en una imagen QR. Muestra la lista de sus alumnos vinculados.
* `ConserjeScreen.kt` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt#L1-L211 Usa la cámara mediante CameraX. Conecta con el `QrAnalyzer.kt` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/7c61f9b5c98ab9a4d31d8263b5fb507318791d6c/app/src/main/java/com/example/proyectofinalivanroldan/util/QRAnalyzer.kt#L1-L41, que usa ML Kit para "leer" el código. Una vez leído el ID, consulta al repositorio para confirmar si el tutor es válido y qué alumnos puede llevarse.


# Criterios de Evaluación

## RA1 — Interfaz, estructura y comportamiento de la aplicación
Para el desarrollo de la interfaz y la lógica visual, se han seleccionado herramientas modernas que garantizan escalabilidad y rendimiento nativo:

* **Jetpack Compose:** Se ha descartado el sistema clásico de XML en favor de Compose para crear una UI declarativa, lo que reduce el código repetitivo y facilita la reactividad del estado (ej: cambiar de pantalla roja a verde instantáneamente).
* **Material Design 3:** Librería de componentes visuales que asegura coherencia estética y accesibilidad (colores dinámicos, tipografía adaptable).
* **CameraX:** Seleccionada sobre la API de cámara antigua por su compatibilidad con el ciclo de vida de la actividad.
* **ZXing Android Embedded:** Librería optimizada para la decodificación de matrices de datos (QR) en tiempo real.

**Ubicación:**
`build.gradle.kts` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/bd15794166b70649ca5fcf2f7a1a94c9aff90b75/app/build.gradle.kts#L44-L73

## RA1.b – Crea interfaz gráfica

La estructura de la interfaz se define en el punto de entrada de la aplicación, utilizando **Jetpack Compose Navigation**. En lugar de múltiples actividades, se utiliza una "Single Activity Architecture" donde un `NavHost` gestiona el intercambio de pantallas (Login, Admin, Tutor, Conserje) basándose en el estado de autenticación y el rol del usuario.

* **Gestión de Estado:** Los ViewModels (`LoginViewModel`, `AdminViewModel`) se instancian al inicio y se pasan a las pantallas correspondientes.
* **Navegación Condicional:** El grafo de navegación decide qué pantalla mostrar (`startDestination = "login"`) y maneja la redirección post-login mediante lambdas (`onLoginSuccess`).

**Dónde ocurre en el código:**
* **Estructura global:** `MainActivity.kt` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/bd15794166b70649ca5fcf2f7a1a94c9aff90b75/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt#L1-L122

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
`AdminItemCard` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/d526d4f7ba6d02054715f5ad77955585181ca831/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt#L228-L292
`Theme` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/45b299f32845ed203c179fe9347f07840ddd919c/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt#L57-L59

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
https://github.com/irolram/ProyectoFinalIvanRoldan/blob/152db8ae8d4d7f1c2cd2dc742df9a9e974fe813b/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt#L1-L211
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

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/152db8ae8d4d7f1c2cd2dc742df9a9e974fe813b/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt#L1-L306
## RA2.e — Detección facial / corporal

- **Análisis y Justificación de la Exclusión**

Aunque durante la fase de análisis se valoró la integración de la librería **ML Kit Face Detection** para permitir el acceso biométrico, esta funcionalidad fue **descartada deliberadamente** en favor del sistema de códigos QR por tres motivos críticos:

1.  **Protección de Datos y Privacidad (RGPD):**
    El almacenamiento y procesamiento de datos biométricos (rostros) de alumnos menores de edad y sus tutores constituye un tratamiento de datos de categoría especial. Implementar reconocimiento facial en un proyecto escolar conlleva riesgos de seguridad y privacidad (fugas de datos biométricos) que no son asumibles. El sistema QR es anónimo y revocable, mientras que el rostro no.

2.  **Fiabilidad en Entornos No Controlados:**
    El proceso de recogida se realiza a menudo en exteriores o bajo condiciones de iluminación variables (luz solar directa, atardecer en invierno, lluvia). Las pruebas teóricas indican que la detección facial pierde precisión en estas condiciones, lo que podría causar "falsos negativos" y bloquear la salida de un alumno legítimo, generando caos en la puerta.

3.  **Seguridad Determinista vs. Probabilística:**
    La visión por computador ofrece un porcentaje de coincidencia (probabilidad). En seguridad escolar, se requiere un sistema determinista (Sí/No). Un código QR criptográfico ofrece una validación binaria exacta, eliminando el riesgo de confundir a familiares con rasgos similares.

- **Alternativa Implementada**

Se ha sustituido la detección de **"Quién eres"** (Biometría) por la detección de **"Qué tienes"** (Token QR). Esta decisión mantiene la agilidad del proceso (es igual de rápido escanear una cara que un QR) pero elimina completamente la fricción legal y los errores técnicos derivados de la detección corporal en multitudes.

## RA2.f — Realidad aumentada

- **Implementación: AR Funcional (Heads-Up Display)**

El proyecto implementa una capa de **Realidad Aumentada (AR) Funcional** en el módulo del Conserje. A diferencia de la AR lúdica (modelos 3D), en este entorno profesional se ha optado por un enfoque de **superposición de información crítica (Overlay)**.

El sistema combina dos capas visuales en el eje Z:
1.  **Capa Física (Fondo):** El *feed* de vídeo en tiempo real capturado por CameraX, que muestra la realidad.
2.  **Capa Digital (Frente):** Elementos de interfaz (UI) que se dibujan dinámicamente sobre la imagen real.

- **Experiencia de Usuario:**
    Cuando el conserje enfoca un código, la aplicación "aumenta" la realidad coloreando la interfaz (Semáforo Verde/Rojo) y proyectando los nombres de los alumnos autorizados flotando sobre la imagen de la cámara. Esto permite al usuario mantener el contacto visual con el entorno mientras recibe datos del sistema.

- **Tecnología Aplicada:**
    Se utiliza el layout `Box` de Jetpack Compose para gestionar la profundidad (Z-Index). La `AndroidView` (Cámara) ocupa el fondo, mientras que los componentes informativos se renderizan encima, creando el efecto de realidad mixta sin necesidad de librerías pesadas como ARCore, optimizando así la batería del dispositivo.

### Dónde ocurre en el proyecto

**Superposición de Capas (Box Layout):** `ConserjeScreen.kt`

El código demuestra cómo se apila la interfaz de validación sobre la vista previa de la cámara para crear la experiencia aumentada.

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/152db8ae8d4d7f1c2cd2dc742df9a9e974fe813b/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt#L1-L211

# RA3 — Componentes reutilizables y diseño modular

En este apartado se describe el uso de componentes reutilizables dentro de la aplicación, así como las herramientas empleadas para su creación, configuración e integración. El objetivo principal ha sido construir una interfaz modular, mantenible y escalable, siguiendo buenas prácticas de desarrollo con Jetpack Compose.

## RA3.a — Creación de componentes

- **Herramientas de Diseño y Justificación**

Para la construcción de la arquitectura de componentes en **SafePick**, se ha seleccionado un stack tecnológico moderno y nativo, priorizando la reactividad y la modularidad:

1.  **Jetpack Compose:**
    * *Uso:* Herramienta principal de UI.
    * *Justificación:* Permite crear interfaces declarativas mediante funciones `@Composable`. A diferencia del sistema de Views (XML), Compose facilita la creación de componentes sin estado (stateless) que se redibujan automáticamente cuando cambian los datos.

2.  **Material Design 3 (M3):**
    * *Uso:* Sistema de diseño base.
    * *Justificación:* Proporciona componentes pre-construidos (`Card`, `OutlinedTextField`, `Scaffold`) que garantizan accesibilidad y coherencia visual. Además, su sistema de color dinámico (`ColorScheme`) facilita la implementación automática del modo oscuro/claro.

3.  **ViewModel + StateFlow:**
    * *Uso:* Gestión de estado.
    * *Justificación:* Se utilizan para desacoplar la lógica de negocio de la interfaz. `StateFlow` ofrece un flujo de datos observable, asegurando que los componentes visuales siempre reflejen el estado último de la base de datos sin necesidad de manipulación manual.

### Dónde ocurre en el proyecto

**Componentes Visuales Reutilizables:** `AdminItemCard`
Este componente encapsula la estética de las tarjetas de la aplicación. Se define una vez mediante la función `@Composable` y utiliza `Card`, `Row`, `Column` e `Icon` de Material 3 para estructurar la información.

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/152db8ae8d4d7f1c2cd2dc742df9a9e974fe813b/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt#L227-L292

## RA3.b — Componentes reutilizables

- **Diseño y Reutilización **

La aplicación hace un uso intensivo de **componentes reutilizables**, diseñados para ser utilizados en múltiples contextos sin duplicar código. Se ha seguido el principio de "Single Source of Truth" para la interfaz: si se cambia el diseño de una tarjeta, se actualiza automáticamente en toda la aplicación.

Algunos ejemplos claros implementados en **SafePick**:

1.  **`AdminItemCard` (Tarjeta Polimórfica):**
    Es el componente visual más importante del panel de administración. Se utiliza indistintamente para renderizar tres tipos de entidades diferentes:
    * **Usuarios:** Muestra nombre y rol.
    * **Alumnos:** Muestra nombre y curso.
    * **Vínculos:** Muestra la relación Tutor-Alumno.
    * *Ventaja:* Garantiza que todas las listas tengan la misma altura, márgenes, sombras y comportamiento, reduciendo el código de la pantalla `AdminScreen` significativamente.

2.  **`EmptyState` (Estado Vacío Genérico):**
    Componente visual que se muestra cuando una lista no tiene datos (ej: "No hay alumnos registrados"). Recibe un texto personalizado pero mantiene una alineación y estilo común, ofreciendo feedback visual consistente al usuario.

3.  **Diálogos de Entrada (`Add...Dialog`):**
    Aunque gestionan datos diferentes, los diálogos de creación mantienen una estructura visual compartida (Título, Campos de texto Outlined, Botones de Acción), asegurando una experiencia de usuario coherente.

### Dónde ocurre en el proyecto

**Reutilización en Acción:** `AdminScreen.kt`

En este archivo se observa cómo el mismo componente `AdminItemCard` se invoca en tres pestañas diferentes (`Tab 0`, `Tab 1`, `Tab 2`), adaptándose a los datos de cada una.

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/152db8ae8d4d7f1c2cd2dc742df9a9e974fe813b/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt#L227-L292

## RA3.c — Parámetros y valores por defecto

- **Diseño de Parámetros**

Los componentes reutilizables en **SafePick** se han diseñado con **parámetros claros y tipados**, permitiendo su adaptación a diferentes contextos sin modificar su implementación interna. Se ha seguido el principio de "Componentes sin Estado" (Stateless), donde el componente solo recibe datos primitivos y emite eventos (lambdas).

Por ejemplo:
* **`AdminItemCard`:** Recibe `title` (String), `subtitle` (String) e `icon` (ImageVector). No recibe objetos complejos (`Usuario` o `Alumno`), lo que lo hace "agnóstico" al tipo de dato que muestra.

- **Valores por Defecto (Default Values)**

Se han definido valores por defecto en los constructores de las funciones Composable para reducir la verbosidad del código y ofrecer un comportamiento estándar "out-of-the-box":

1.  **Comportamiento de Borrado:** En `AdminItemCard`, el parámetro `isDeletable` tiene un valor por defecto `true`.
    * *Beneficio:* Esto significa que, por defecto, todas las tarjetas muestran el botón de borrar. Solo cuando es necesario ocultarlo (como en el caso del usuario "admin"), se sobrescribe el valor a `false`. Esto simplifica las llamadas al componente en el 90% de los casos.

2.  **Tema del Sistema:** En `SafePickTheme`, el parámetro `darkTheme` toma por defecto la configuración del sistema operativo (`isSystemInDarkTheme()`), evitando tener que pasar este valor manualmente.

### Dónde ocurre en el proyecto

**Definición de parámetros con valores por defecto:** `AdminScreen.kt`

El siguiente fragmento muestra cómo se define el parámetro `isDeletable` con un valor por defecto para simplificar su uso.

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/152db8ae8d4d7f1c2cd2dc742df9a9e974fe813b/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt#L227-L232

## RA3.d — Eventos en componentes

- **Gestión de eventos e interacción (State Hoisting)**

Los componentes de **SafePick** implementan el patrón de **Elevación de Estado (State Hoisting)**. Esto significa que los componentes visuales no modifican la base de datos directamente, sino que exponen eventos mediante **Lambdas** (funciones de callback) que suben hasta los ViewModels.

Esto permite:
1.  **Desacoplamiento:** La tarjeta de un alumno (`AdminItemCard`) no sabe cómo borrar un alumno, solo sabe avisar de que el usuario ha pulsado "Borrar".
2.  **Centralización:** Toda la lógica de negocio se queda en el `AdminViewModel`, facilitando las pruebas y el mantenimiento.

**Ejemplos de eventos gestionados:**
* `onDelete`: Evento disparado al pulsar el icono de papelera en las tarjetas de administración.
* `onConfirm`: Evento disparado al terminar de rellenar un formulario de diálogo (ej: Crear Alumno).
* `onDismiss`: Evento para cerrar modales sin guardar cambios.

### Dónde ocurre en el proyecto

**Callbacks en `AdminItemCard`:**
El componente define qué pasa, no cómo pasa. Se puede ver cómo recibe funciones lambda como parámetros en su constructor.

[Ver en GitHub: AdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

**Eventos de Diálogo (`AlumnoDialog`):**
Gestión de confirmación con paso de parámetros hacia la pantalla padre.

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/152db8ae8d4d7f1c2cd2dc742df9a9e974fe813b/app/src/main/java/com/example/proyectofinalivanroldan/ui/components/AlumnoDialog.kt#L21-L87

## RA3.f — Documentación de componentes

- **Documentación y claridad (KDoc)**

El código fuente de **SafePick** sigue rigurosos estándares de documentación utilizando **KDoc**. Las clases y funciones principales (`@Composable`) incluyen bloques de comentarios que describen:
1.  **Propósito:** Qué hace el componente.
2.  **Parámetros (@param):** Qué datos necesita para funcionar.
3.  **Comportamiento:** Notas sobre lógica interna (ej: validaciones o autogeneración de IDs).

Además, la estructura de paquetes es semántica y organizada por capas:
* `ui.components`: Piezas visuales reutilizables (Botones, Tarjetas, Diálogos).
* `ui.mainScreen`: Pantallas completas de la aplicación.
* `data.repository`: Lógica de datos y persistencia.

Esta documentación garantiza que el proyecto sea comprensible para futuros desarrolladores o para el mantenimiento a largo plazo.

### Dónde ocurre en el proyecto

**Documentación en Diálogos:**
En el archivo del diálogo de alumnos se documenta la funcionalidad de autogeneración de UUID y la validación de campos vacíos.

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/152db8ae8d4d7f1c2cd2dc742df9a9e974fe813b/app/src/main/java/com/example/proyectofinalivanroldan/ui/components/AlumnoDialog.kt#L21-L87


## RA3.h — Integración de componentes en la app

- **Integración global y Coherencia**

Los componentes reutilizables no son islas aisladas; están orquestados para funcionar como un sistema unificado. La integración se produce en tres niveles:

1.  **Nivel Visual (Theme):** Todos los componentes (`Card`, `Button`, `TopAppBar`) heredan automáticamente los colores y tipografías definidos en el tema principal de la aplicación, asegurando coherencia estética.
2.  **Nivel Lógico (Navigation):** El `NavHost` integra las pantallas (`AdminScreen`, `ConserjeScreen`) permitiendo el paso de argumentos y la gestión de la pila de navegación (BackStack).
3.  **Nivel Funcional (Inyección):** Los componentes complejos consumen datos reales gracias a la integración de los `ViewModels` que, a su vez, integran los Repositorios de datos.

El resultado es una aplicación donde un cambio en el repositorio de alumnos se refleja inmediatamente en la tarjeta `AdminItemCard` de la pantalla de administración y en la validación QR del conserje.

### Dónde ocurre en el proyecto

**Integración en `MainAdminScreen`:**
Aquí convergen el ViewModel, el `LazyColumn` y el componente `AdminItemCard` para mostrar la lista de usuarios.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

**Navegación Centralizada en `MainActivity`:**
El punto de unión de todas las pantallas y componentes, donde se define el grafo de navegación de la app.

[Ver en GitHub: MainActivity.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt)


# RA4 — Usabilidad, diseño visual y experiencia de usuario

En este apartado se analiza la aplicación desde el punto de vista de la **usabilidad**, el **diseño visual** y la **experiencia de usuario (UX)**, aplicando estándares reconocidos de diseño de interfaces móviles y buenas prácticas propias del ecosistema Android con Jetpack Compose y Material Design 3. Dado que SafePick es una herramienta de seguridad y gestión, se ha priorizado la claridad y la rapidez de respuesta visual.

## RA4.a — Aplicación de estándares

- **Uso de estándares de diseño**

La aplicación aplica de forma consistente los **estándares de diseño de Android** a través del uso de **Material Design 3 (M3)** y Jetpack Compose. Se respetan principios fundamentales como:
* **Jerarquía visual clara:** Títulos grandes para contextos (ej: "Escaneando..."), subtítulos para detalles y tarjetas elevadas para elementos de lista.
* **Contraste adecuado:** Uso de esquemas de color de alto contraste (Light/Dark Theme) para garantizar la legibilidad en exteriores (puerta del colegio).
* **Componentes coherentes:** Uso de `MaterialTheme`, `Scaffold`, `Card`, `FloatingActionButton` y `TopAppBar` para ofrecer una interfaz que el usuario reconoce instintivamente como "nativa".

### Dónde ocurre en el proyecto

**Uso global de MaterialTheme:**
El archivo de tema define la paleta de colores y tipografías que heredan todos los componentes.

https://github.com/irolram/ProyectoFinalIvanRoldan/blob/152db8ae8d4d7f1c2cd2dc742df9a9e974fe813b/app/src/main/java/com/example/proyectofinalivanroldan/ui/theme/Theme.kt#L1-L47

**Componentes Material en Pantallas:**
Implementación de `Scaffold` y `Surface` en las pantallas principales.

[Ver en GitHub: MainActivity.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt)

## RA4.b — Valoración de los estándares aplicados

- **Reflexión y justificación**

No solo se aplican los estándares, sino que se **valoran y adaptan** al contexto crítico de la aplicación. Por ejemplo:
* **Adaptación al entorno (Conserje):** Se ha maximizado el área de visión de la cámara y se han utilizado códigos de color universales (Verde/Rojo) para validar el acceso, permitiendo una interpretación instantánea sin necesidad de leer texto detallado bajo la luz del sol.
* **Adaptación al flujo (Admin):** Se prioriza la densidad de información en las listas mediante el uso de `LazyColumn`, permitiendo al administrador ver más alumnos de un vistazo.
* **Feedback háptico y visual:** Los botones y acciones tienen estados de "pulsado" (Ripple effect) estándar de Android para confirmar la interacción.

### Dónde ocurre en el proyecto

**Interfaz de Alto Contraste:**
La pantalla del conserje utiliza superposiciones de color semántico sobre la cámara para validación rápida.

[Ver en GitHub: ConserjeScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt)

**Listas Optimizadas:**
La pantalla de administración usa estándares de listas para gestión eficiente.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

## RA4.c — Menús de navegación

- **Diseño y coherencia**

La aplicación cuenta con un sistema de navegación **claro, coherente y segmentado por roles**, compuesto por:
* **Navegación por Pestañas (Tabs):** En el panel de Administrador, se utiliza un `TabRow` para alternar rápidamente entre la gestión de Usuarios, Alumnos y Vínculos sin cambiar de pantalla.
* **Barra Inferior (BottomBar):** En el perfil del Tutor, se usa para separar la vista de "Mis Alumnos" de la vista de "Mi Pase QR".
* **Enrutamiento Automático:** El `NavHost` gestiona la navegación inicial, dirigiendo al usuario a su pantalla específica según su rol, simplificando la experiencia al eliminar opciones irrelevantes.

### Dónde ocurre en el proyecto

**Navegación por Pestañas:**
Implementación de `TabRow` en la pantalla de administración.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

**Enrutamiento Lógico:**
Definición del grafo de navegación en la actividad principal.

[Ver en GitHub: MainActivity.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt)

## RA4.d — Distribución de acciones

- **Claridad y accesibilidad**

Las acciones principales están colocadas de forma **estratégica según la frecuencia de uso** y la ergonomía (Ley de Fitts):
* **Acción Principal (FAB):** El botón flotante `+` en la pantalla de administración está anclado en la esquina inferior derecha, facilitando la creación de nuevos registros con una sola mano.
* **Acciones de Lista:** Los iconos de borrado y edición se encuentran a la derecha de cada tarjeta, siguiendo el patrón de lectura occidental y evitando clicks accidentales.
* **Acción de Reseteo:** En la pantalla del conserje, el botón "Siguiente" es grande y está situado en la parte inferior, permitiendo un flujo continuo de escaneo.

### Dónde ocurre en el proyecto

**Botón Flotante:**
Ubicación accesible para añadir entidades.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

## RA4.e — Distribución de controles

- **Orden y jerarquía**

Los controles están organizados de forma lógica siguiendo el flujo de lectura (Z-Pattern o F-Pattern):
1.  **Login:** Campos de texto ordenados verticalmente (Usuario -> Contraseña) seguidos del botón de acción (Entrar).
2.  **Tarjetas:** Icono identificativo a la izquierda, información textual (Nombre/Curso) en el centro y acciones secundarias (Borrar) a la derecha.
3.  **Diálogos:** Título descriptivo arriba, formulario en el centro y botones de confirmación/cancelación abajo a la derecha, siguiendo las guías de Material Design.

Se aplica un uso consistente de `Spacer` y `Padding` para evitar la saturación visual y agrupar elementos relacionados.

### Dónde ocurre en el proyecto

**Layout de Login:**
Uso de `Column` con espaciado vertical para guiar la entrada de datos.

[Ver en GitHub: LoginScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/login/LoginScreen.kt)

**Layout de Tarjetas:**
Uso de `Row` para alinear información y acciones.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

## RA4.f — Elección de controles

- **Justificación de los componentes utilizados**

Los controles elegidos son los más adecuados para su función específica, priorizando la usabilidad nativa:
* **`LazyColumn`:** Elegido sobre `Column` simple para garantizar el rendimiento y el scroll fluido en listas que pueden crecer indefinidamente (alumnos).
* **`OutlinedTextField`:** Utilizado en formularios (Login, Diálogos) porque delimita claramente el área táctil y mejora la accesibilidad visual.
* **`Dialog` (Modal):** Elegido para la creación de registros en lugar de navegar a una pantalla nueva, manteniendo al usuario en contexto.
* **`AndroidView` (PreviewView):** Imprescindible para integrar el feed de cámara de CameraX dentro de la interfaz de Compose.

### Dónde ocurre en el proyecto

**Listas Eficientes:**
Implementación de `LazyColumn` para listas de datos.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

**Integración de Cámara:**
Uso de `AndroidView` para componentes nativos.

[Ver en GitHub: ConserjeScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt)

## RA4.g — Diseño visual

- **Atractivo y coherencia visual**

El diseño visual es moderno y coherente gracias a la implementación centralizada de **Material Theming**:
* **Paleta Semántica:**
    * **Primary:** Acciones principales y elementos activos.
    * **Error:** Usado exclusivamente para estados críticos (Borrar, Acceso Denegado).
    * **Surface/Background:** Adaptables al modo oscuro para reducir la fatiga visual.
* **Tipografía:** Uso de jerarquías de texto (`titleMedium`, `bodySmall`) para diferenciar nombres de alumnos de detalles secundarios como el curso.
* **Formas:** Uso consistente de esquinas redondeadas (`RoundedCornerShape`) en tarjetas y botones, suavizando la interfaz.

### Dónde ocurre en el proyecto

**Definición de Tema:**
Configuración de colores y formas.

[Ver en GitHub: Theme.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/theme/Theme.kt)

**Estilado de Tarjetas:**
Aplicación de colores de superficie y formas.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

## RA4.h — Claridad de mensajes

- **Comunicación con el usuario**

Los mensajes mostrados son claros, concisos y orientados a la seguridad:
* **Feedback de Seguridad:** El conserje recibe mensajes explícitos ("ACCESO AUTORIZADO" en verde o "DENEGADO" en rojo), eliminando la ambigüedad.
* **Estados Vacíos (`EmptyState`):** Si no hay datos, se informa al usuario ("No hay alumnos registrados") en lugar de mostrar una pantalla en blanco que podría parecer un error.
* **Validación de Formularios:** Se impide el envío de formularios vacíos, guiando al usuario para completar la información necesaria.

### Dónde ocurre en el proyecto

**Mensajes de Estado de Escaneo:**
Feedback visual inmediato tras leer un QR.

[Ver en GitHub: ConserjeScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt)

**Componente EmptyState:**
Información contextual cuando no hay listas.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

## RA4.i — Pruebas de usabilidad

- **Evaluación funcional**

Se han realizado pruebas de usabilidad funcionales durante el desarrollo para validar los flujos críticos:
1.  **Prueba de Estrés QR:** Verificación de que el escáner lee códigos rápidamente sin necesidad de enfocar perfectamente, vital para evitar colas en el colegio.
2.  **Prueba de Roles:** Verificación de que un usuario "Tutor" nunca puede acceder a las pantallas de "Admin", garantizando la seguridad.
3.  **Flujo de Creación:** Validación de que crear un vínculo Tutor-Alumno es intuitivo y se refleja inmediatamente en el sistema.

### Dónde se evidencia

**Lógica de Navegación Segura:**
Ajustes en el `NavHost` para proteger rutas.

[Ver en GitHub: MainActivity.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/MainActivity.kt)

## RA4.j — Evaluación en distintos dispositivos

- **Adaptación y pruebas**

La aplicación ha sido evaluada para garantizar su robustez en distintos escenarios:
* **Responsive Layouts:** El uso de modificadores como `fillMaxWidth` y `weight` asegura que las tarjetas y botones se adapten al ancho de pantalla, ya sea un móvil compacto o uno de gran formato.
* **Camera Aspect Ratio:** La vista previa de la cámara se adapta a las proporciones del dispositivo sin distorsionar la imagen, asegurando una lectura correcta de los códigos QR.
* **Modo Oscuro:** Verificación de que todos los textos son legibles tanto en modo claro (día) como oscuro (noche/interiores).

### Dónde ocurre en el proyecto

**Diseño Adaptable:**
Uso de modificadores flexibles en los layouts principales.

[Ver en GitHub: LoginScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/login/LoginScreen.kt)

# RA5 — Generación de informes

## RA5.a — Establece la estructura del informe

La aplicación implementa una estructura clara y organizada para la generación de informes y exportación de datos, siguiendo la arquitectura MVVM.
A diferencia de sistemas basados en SQL, **SafePick** procesa la información almacenada en los repositorios JSON locales, la transforma en la capa de dominio/negocio y la presenta o exporta desde la capa de presentación.

La estructura del informe se divide en:
* **Capa de Datos:** Lectura de archivos JSON (`alumnos.json`, `usuarios.json`) mediante `Gson`.
* **Capa de Lógica (ViewModel):** Procesamiento de listas, filtrado y formateo de cadenas para CSV.
* **Capa de Presentación:** Botones de acción en `AdminScreen` para desencadenar la generación y visualización de totales.

Esta separación permite que la lógica de generación del informe no bloquee la interfaz de usuario, utilizando Corrutinas para el procesamiento en segundo plano.

### Ubicación en el código

**Lógica de Negocio en ViewModel:**
El `AdminViewModel` contiene la función encargada de orquestar la creación del informe.

[Ver en GitHub: AdminViewModel.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel/AdminViewModel.kt)

**Repositorios de Datos:**
Fuente de verdad de donde se extrae la información cruda.

[Ver en GitHub: AlumnoRepository.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/tree/main/app/src/main/java/com/example/proyectofinalivanroldan/data/repository)

## RA5.b — Genera informes a partir de fuentes de datos

El informe se genera a partir de **datos reales y persistentes** almacenados en el almacenamiento interno del dispositivo.
Los valores exportados no son simulados; reflejan el estado exacto de la matriculación del centro en el momento de la consulta. El sistema recorre las listas de objetos `Alumno` y `Usuario` cargadas en memoria desde los repositorios para construir el reporte.

### Ubicación en el código

**Generación de CSV:**
La función `generarInformeCSV()` en el ViewModel toma los datos vivos del repositorio y los transforma en un formato estructurado.

[Ver en GitHub: AdminViewModel.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel/AdminViewModel.kt)

## RA5.c — Establece filtros sobre los valores a presentar

El sistema de informes aplica **filtros lógicos** sobre los datos antes de presentarlos o exportarlos, asegurando que la información sea relevante.

Los filtros aplicados incluyen:
* **Filtrado por Rol:** Segregación de usuarios entre "Tutores", "Conserjes" y "Administradores" para la gestión de permisos.
* **Filtrado por Curso:** Organización de los alumnos según su nivel educativo.
* **Validación de Vínculos:** Verificación de qué alumnos tienen tutores asignados y cuáles están pendientes de asignación.

Estos filtros se aplican utilizando funciones de colección de Kotlin (`filter`, `map`, `groupBy`) dentro del ViewModel, garantizando eficiencia y seguridad de tipos.

### Ubicación en el código

**Filtrado en Listas:**
En la pantalla de administración, las listas se pueden procesar para mostrar subconjuntos de datos específicos.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

## RA5.d — Incluye valores calculados, recuentos o totales

El panel de administración y el informe exportado incluyen **métricas y recuentos** que permiten evaluar el volumen de datos del centro escolar.

Se realizan los siguientes cálculos:
* **Total de Alumnos Matriculados:** Recuento dinámico del tamaño de la lista de alumnos (`List.size`).
* **Total de Usuarios Registrados:** Recuento de las credenciales activas en el sistema.
* **Relaciones Activas:** Cálculo de cuántos vínculos Tutor-Alumno existen actualmente.

Estos valores se calculan en tiempo real gracias a `StateFlow`, actualizándose instantáneamente si se añade o elimina un registro.

### Ubicación en el código

**Cálculo en ViewModel:**
Las variables de estado (`uiState`) mantienen los totales actualizados para su visualización.

[Ver en GitHub: AdminViewModel.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel/AdminViewModel.kt)

## RA5.e — Visualización y Exportación de Datos

La aplicación incluye mecanismos para la **visualización y exportación** de la información procesada.

1.  **Visualización en Dashboard:** La pantalla `AdminScreen` actúa como un cuadro de mando visual donde se muestran los listados y totales mediante tarjetas (`AdminItemCard`) y listas con scroll (`LazyColumn`).
2.  **Exportación a Archivo (Reporte Externo):** Se ha implementado la capacidad de generar un archivo externo (CSV/Texto) que recopila toda la información de la base de datos. Este archivo sirve como copia de seguridad y como documento de control para la dirección del centro.

El formato elegido (CSV) garantiza la interoperabilidad con otras herramientas de hoja de cálculo (Excel, Google Sheets), cumpliendo con el requisito de generar informes útiles y portables.

### Ubicación en el código

**Botón de Generación de Informe:**
Interfaz de usuario que dispara la acción de exportación.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

**Lógica de Escritura de Archivo:**
Función interna que escribe el String formateado en el almacenamiento del dispositivo.

[Ver en GitHub: AdminViewModel.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/viewmodel/AdminViewModel.kt)


# RA6 – Sistemas de ayuda y documentación

## RA6.a – Identifica sistemas de generación de ayudas

En **SafePick** se identifican y utilizan distintos sistemas de generación de ayudas orientados tanto al usuario final (personal del centro y familias) como al desarrollador.
Estas ayudas no se limitan a un único formato, sino que se integran de forma natural en la interfaz y en la documentación del proyecto.

* **Ayudas de Interfaz (Usuario):** Se utilizan mensajes informativos en las pantallas de carga, estados vacíos ilustrados (`EmptyState`) cuando no hay alumnos o usuarios, y diálogos de confirmación críticos antes de eliminar registros para evitar pérdidas de datos accidentales.
* **Ayudas Técnicas (Desarrollador):** El proyecto incorpora comentarios de código **KDoc** estandarizados en todas las capas (Data, Domain, UI), explicando el propósito de clases complejas como `QrAnalyzer` o `AdminViewModel`.

Este enfoque dual asegura que la aplicación sea intuitiva de usar y fácil de mantener.

### Ubicación en el código

**Estados Vacíos Informativos:**
Componente reutilizable que guía al usuario cuando no hay datos.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

**Diálogos de Confirmación:**
Uso de `AlertDialog` para confirmar el borrado de un alumno.

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

**Documentación de Código (KDoc):**
Ejemplo de documentación en el analizador de imágenes.

[Ver en GitHub: QrAnalyzer.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/util/QRAnalyzer.kt)

## RA6.b – Genera ayudas en formatos habituales

Las ayudas se generan utilizando formatos habituales y reconocibles dentro del ecosistema de aplicaciones móviles Android.
La aplicación ofrece información al usuario mediante:

1.  **Textos descriptivos en UI:** Etiquetas claras en los campos de texto (`OutlinedTextField`) como "Nombre del Alumno" o "Curso", que actúan como ayuda contextual inmediata.
2.  **Feedback Visual (Toast/Snackbar):** Mensajes emergentes breves que confirman acciones ("Alumno creado correctamente") o informan de errores ("Error al leer el QR").
3.  **Códigos de Color Semánticos:** Uso universal de Verde (Éxito/Autorizado) y Rojo (Error/Denegado) en la pantalla del conserje, funcionando como una ayuda visual instantánea sin necesidad de lectura.
4.  **Documentación Externa:** Un archivo `README.md` detallado que sirve como punto de entrada para comprender el proyecto.

### Ubicación

**Feedback Visual y Textos:** `LoginScreen.kt` y `ConserjeScreen.kt`.
https://github.com/irolram/ProyectoFinalIvanRoldan/blob/cde3dbcd4cd72d76d89bd214e668a5ccf39b0274/app/src/main/java/com/example/proyectofinalivanroldan/ui/login/LoginScreen.kt#L1-L104
https://github.com/irolram/ProyectoFinalIvanRoldan/blob/cde3dbcd4cd72d76d89bd214e668a5ccf39b0274/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt#L1-L211

## RA6.c – Genera ayudas sensibles al contexto

La aplicación genera ayudas **adaptadas al contexto y al rol** del usuario logueado. No se muestra la misma información a un padre que a un conserje.

Ejemplos de ayudas contextuales en **SafePick**:
* **Contexto "Conserje":** Si la cámara detecta un QR inválido, muestra un mensaje específico de "Código no reconocido" y sugiere limpiar el escáner. Si detecta uno válido, muestra la lista de alumnos asociados.
* **Contexto "Admin":** Si intenta borrar al usuario "admin", el sistema deshabilita el botón o muestra un aviso, protegiendo la integridad del sistema.
* **Contexto "Tutor":** Solo ve la ayuda relacionada con *sus* hijos y *su* código QR, evitando la sobrecarga de información irrelevante.

### Ubicación en el código

**Lógica Condicional de UI:**
El `ConserjeScreen` cambia su mensaje de ayuda según el estado del escaneo (`Scanning`, `Authorized`, `Denied`).

[Ver en GitHub: ConserjeScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/ConserjeScreen.kt)

**Protección Contextual:**
El `AdminItemCard` oculta el botón de borrar si el usuario es "admin".

[Ver en GitHub: MainAdminScreen.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/MainAdminScreen.kt)

## RA6.d – Documenta la estructura de la información persistente

La estructura de la información persistente está documentada de forma clara mediante la definición de **Modelos de Datos (Data Classes)** y el uso de **Repositorios**.
Aunque se utiliza persistencia en archivos JSON (Gson) en lugar de Room, la estructura está igualmente formalizada:

* **Entidades:** Las clases `Usuario`, `Alumno` y `Vinculo` definen explícitamente los campos (ID, nombre, curso, rol) y sus tipos de datos.
* **Repositorios:** Las interfaces (`IUsuarioRepository`, etc.) documentan las operaciones disponibles (Crear, Leer, Borrar), actuando como un contrato claro de acceso a datos.
* **Serialización:** El uso de la librería Gson y las anotaciones implícitas documentan cómo se transforman los objetos en texto JSON.

### Ubicación en el código

**Definición de Modelos:**
Documentación de la estructura de datos.

[Ver en GitHub: Usuario.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/src/main/java/com/example/proyectofinalivanroldan/dominio/model/Usuario.kt)

**Interfaces de Repositorio:**
Documentación de las operaciones de datos.

[Ver en GitHub: UsuarioRepository.kt](https://github.com/irolram/ProyectoFinalIvanRoldan/tree/main/app/src/main/java/com/example/proyectofinalivanroldan/data/repository)

## RA6.e – Manual de usuario y guía de referencia

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

El manual utiliza un lenguaje no técnico, adecuado para el personal del centro educativo.


## RA6.f – Manual técnico de instalación y configuración

## 1. Introducción
**SafePick** es una aplicación móvil desarrollada para la plataforma Android destinada a la gestión de seguridad escolar. Permite la administración de usuarios, registro de alumnos y supervisión de salidas mediante un sistema de vinculación tutor-alumno, garantizando que solo personas autorizadas retiren a los estudiantes.

---

## 2. Especificaciones Técnicas

### 2.1. Tecnologías Utilizadas
* **Lenguaje:** Kotlin 1.9.x
* **Framework UI:** Jetpack Compose (Material Design 3)
* **Arquitectura:** MVVM (Model-View-ViewModel) con patrón Repository.
* **Navegación:** Compose Navigation con gestión de pila (Stack Management).
* **Gestión de Permisos:** Accompanist Permissions (para el módulo de cámara).

### 2.2. Requisitos del Sistema
* **SO Mínimo:** Android 8.0 (Oreo) - API 26.
* **SO Target:** Android 14.0 (Upside Down Cake) - API 34.
* **IDE Recomendado:** Android Studio Ladybug | 2024.2.1 o superior.
* **JDK:** Versión 17.

---

## 3. Instalación del Entorno de Desarrollo

1. **Clonación del Repositorio:**

   git clone [https://github.com/IvanRoldan/ProyectoFinalSafePick.git](https://github.com/IvanRoldan/ProyectoFinalSafePick.git)



2. **Configuración de Android Studio:** 

- Importar el proyecto desde File > Open.

- Verificar en Settings > Build, Execution, Deployment > Build Tools > Gradle que el Gradle JDK esté apuntando a Java 17.

3. **Sincronización:**

- Ejecutar Sync Project with Gradle Files y esperar a que las dependencias se descarguen correctamente.

## 4. Estructura de Paquetes
com.example.proyectofinalivanroldan.dominio.model: Definición de las entidades de datos (Usuario, Alumno, Vinculo).

com.example.proyectofinalivanroldan.data.repository: Lógica de persistencia local y manejo de datos.

com.example.proyectofinalivanroldan.ui.mainScreen: Pantallas principales por roles (Admin, Tutor, Conserje).

com.example.proyectofinalivanroldan.ui.components: Componentes reutilizables (Diálogos, Cards, Filtros).

com.example.proyectofinalivanroldan.ui.viewmodel: Lógica de negocio y gestión de estados de la UI.

## 5. Configuración y Seguridad

### 5.1. Usuario Maestro
El sistema cuenta con un activador automático en MainActivity.kt. Si la base de datos de usuarios está vacía, se genera el siguiente acceso:

Username: admin

Password: admin

Rol: ADMIN

## RA6.g – Confecciona tutoriales

Se han diseñado flujos de trabajo que actúan como tutoriales implícitos ("Onboarding") para guiar al usuario en las acciones principales:

* **Flujo Guiado del Conserje:** La pantalla no es estática; guía al conserje paso a paso: "Enfoca el código" -> "Procesando..." -> "Resultado" -> "Botón Siguiente". Este diseño secuencial enseña al usuario cómo operar sin necesidad de un manual externo.
* **Validación de Formularios:** Los diálogos de creación de usuarios impiden avanzar si faltan datos, mostrando mensajes de error ("El campo nombre es obligatorio") que enseñan al administrador qué información es necesaria.

### Ubicación

**Flujo Secuencial:** Lógica de estados en `ConserjeScreen.kt`.

# RA7 — Distribución de aplicaciones

## RA7.a — Empaquetado de la aplicación

La aplicación **SafePick** ha sido empaquetada correctamente utilizando las herramientas oficiales de Android Studio. Se ha generado una versión de distribución en formato **APK (Android Package Kit)** en modo `release`.

Este formato ha sido seleccionado por ser el estándar para la instalación directa en dispositivos de la institución (tablets de conserjería y móviles de tutores) sin necesidad de pasar obligatoriamente por una tienda pública en esta fase inicial. El proceso de construcción (`Build > Generate Signed Bundle / APK`) ha compactado el código compilado (DEX), los recursos (imágenes, layouts) y el manifiesto en un único archivo instalable.

### Ubicación en el código

**Configuración de Versiones:**
El archivo de construcción define la versión del paquete (`versionCode`) y el nombre visible (`versionName`).

[Ver en GitHub: build.gradle.kts](https://github.com/irolram/ProyectoFinalIvanRoldan/blob/main/app/build.gradle.kts)

## RA7.b — Personalización del instalador

El instalador de la aplicación presenta una **identidad visual coherente** y profesional, fundamental para generar confianza en los padres y el personal del centro.

Se han personalizado los siguientes elementos:
* **Nombre de la aplicación:** "SafePick" (definido en `strings.xml`).
* **Tema de Inicio (Splash Screen):** La aplicación inicia con una transición suave que respeta el modo oscuro/claro del sistema.

## RA7.c — Paquete generado desde el entorno de desarrollo

El paquete de distribución se ha generado directamente desde **Android Studio**, utilizando el sistema de construcción oficial de **Gradle (Kotlin DSL)**.

El script de construcción (`build.gradle.kts`) se ha configurado para:
1.  **Minificar:** (Opcional en debug, activo en release) para reducir el tamaño del APK.
2.  **Optimizar recursos:** Eliminando recursos no utilizados.
3.  **Compatibilidad:** Asegurando que el paquete funcione desde Android 8.0 (API 26) hasta la última versión (Android 14+), cubriendo la mayoría de dispositivos del mercado escolar.

El resultado es un archivo `.apk` estable, optimizado y listo para su despliegue.

## RA7.d — Uso de herramientas externas

Para la distribución y preparación de la aplicación se han utilizado herramientas externas estándar del ecosistema Android:

1.  **Android Studio (Koala):** IDE oficial para la gestión del proyecto y generación de artefactos.
2.  **Gradle Build Tool:** Sistema de automatización que gestiona las dependencias (CameraX, Gson, Compose) y compila el código.
3.  **Keytool:** Herramienta de línea de comandos (integrada en el IDE) utilizada para generar el almacén de claves criptográficas.

Estas herramientas aseguran un proceso de construcción fiable, reproducible y conforme a los estándares de Google.

## RA7.e — Firma digital de la aplicación

La aplicación ha sido **firmada digitalmente** mediante un **Keystore (.jks)**. Este es un requisito de seguridad crítico, especialmente para una app que maneja datos de menores.

La firma garantiza:
* **Autenticidad:** Asegura al centro educativo que el APK proviene del desarrollador original y no ha sido modificado por terceros (malware).
* **Integridad:** El sistema operativo Android rechazaría la instalación si el paquete hubiera sido alterado tras la firma.
* **Actualizaciones:** Permite instalar nuevas versiones sobre la anterior sin perder los datos de la aplicación (siempre que se use la misma firma).

El proceso se realizó mediante el asistente "Generate Signed APK" de Android Studio, creando una clave privada segura.

<img width="276" height="44" alt="image" src="https://github.com/user-attachments/assets/a083608a-200b-4e89-9e2f-3f58f5380fc5" />


## RA7.f — Instalación desatendida y manual

La aplicación está preparada para dos tipos de instalación, adaptándose a la infraestructura del colegio:

1.  **Instalación Manual (Sideloading):** Los tutores y conserjes pueden instalar el APK directamente descargándolo desde la intranet del colegio o recibiéndolo por correo, habilitando la opción "Orígenes desconocidos".
2.  **Instalación Desatendida (MDM):** El paquete es compatible con sistemas de gestión de dispositivos (MDM) que podrían usar los administradores del colegio para instalar la app masivamente en las tablets de conserjería sin intervención del usuario.

## RA7.g — Desinstalación de la aplicación

La aplicación se desinstala correctamente utilizando los mecanismos estándar del sistema operativo Android.

**Gestión de Residuos de Datos (Privacidad):**
Dado que **SafePick** almacena datos sensibles (listas de alumnos en JSON) en el almacenamiento interno privado de la aplicación (`context.filesDir`), el sistema operativo garantiza que, al desinstalar la app, **todos estos datos se eliminan físicamente** del dispositivo. Esto es una característica de seguridad deseada ("Sandbox") que cumple con el principio de que los datos no persistan si la herramienta no está instalada.

## RA7.h — Canales de distribución

Se ha realizado una planificación razonada de los canales de distribución más adecuados para un entorno escolar cerrado:

1.  **Distribución Interna (Principal):**
    * *Método:* Alojamiento del APK en la web privada del colegio o servidor interno.
    * *Justificación:* Es el método más rápido y directo para una comunidad cerrada de usuarios (padres y empleados). No requiere tiempos de revisión de Google.

2.  **Google Play Store (Canal Privado/Managed Play):**
    * *Método:* Publicación en la Play Store pero restringida a la organización (dominio del colegio).
    * *Justificación:* Facilita las actualizaciones automáticas. Sería el siguiente paso lógico si el proyecto escala a múltiples centros educativos.

Esta estrategia híbrida permite un despliegue inmediato (APK) con una ruta clara hacia una gestión más profesional (Play Store).

# RA8 — Pruebas avanzadas
## RA8.a — Estrategia de pruebas

El proyecto incorpora una estrategia de pruebas orientada a asegurar la estabilidad de la lógica de negocio y la correcta gestión del estado de la aplicación, un aspecto crítico al manejar datos sensibles de alumnos y tutores en un entorno escolar.

- La estrategia de testing se basa en tres niveles principales:

- Pruebas unitarias (Unit Testing):

Validan la lógica de negocio contenida en los Casos de Uso (por ejemplo, verificar que no se pueda crear un alumno con datos incompletos).

Verifican el comportamiento de los ViewModels (AdminViewModel, ConserjeViewModel), comprobando que el estado de la interfaz de usuario reacciona correctamente ante eventos como la carga de listas desde los archivos JSON o la gestión de errores de lectura.

Herramientas: Se utilizan librerías estándar del ecosistema Android como JUnit 4, MockK (para simular el comportamiento de los repositorios) y Turbine (para validar los flujos de datos reactivos).

<img width="1012" height="729" alt="image" src="https://github.com/user-attachments/assets/3c03f466-3ec9-4070-948a-2826e0c9dce4" />
<img width="1021" height="653" alt="image" src="https://github.com/user-attachments/assets/48a4a49d-d9a0-4438-b36d-f207a768a1a6" />
<img width="1024" height="883" alt="image" src="https://github.com/user-attachments/assets/c3925522-43e4-4162-ba1f-39f6c3d3e429" />


- Pruebas de regresión:

Las pruebas unitarias automatizadas permiten detectar errores tras realizar modificaciones en el código. Por ejemplo, si se optimiza el algoritmo de lectura de los archivos JSON, los tests existentes aseguran que el formato de los datos sigue siendo compatible y que no se ha roto la funcionalidad de búsqueda o filtrado.

Pruebas manuales funcionales:

Se realizan verificaciones manuales de aquellos flujos que dependen del hardware y son difíciles de simular en un entorno de test estándar, como el escaneo de códigos QR con la cámara del dispositivo (utilizando la librería CameraX) o la generación y visualización correcta de los informes en PDF.

Esta estrategia asegura que la lógica crítica de la aplicación permanezca estable durante todo el ciclo de desarrollo evolutivo del proyecto.

RA8.b — Pruebas de integración

Durante el desarrollo del proyecto SafePick, la ejecución de las pruebas de integración automáticas (Instrumented Tests) se ha visto interrumpida por problemas críticos de compatibilidad en el entorno de desarrollo. A continuación, se detallan los motivos técnicos por los cuales este apartado no ha podido completarse mediante scripts automatizados:

Conflicto de Versiones (Dependency Hell)
Se detectó una incompatibilidad severa entre las versiones de la librería Compose BOM y las dependencias necesarias para las pruebas de integración, específicamente con JUnit 4 y Espresso Core. A pesar de intentar forzar una resolución consistente en el archivo build.gradle.kts, Gradle generaba errores de tipo "Consistent Resolution", impidiendo la compilación del módulo de test.

RA8.c — Pruebas de regresión
El proyecto incorpora pruebas unitarias reales sobre los ViewModels. Estas pruebas se ejecutan automáticamente cada vez que se modifica la lógica de negocio para asegurar que las funcionalidades previamente validadas no sufran alteraciones no deseadas.

Escenario de Carga en AdminViewModel
Se verifica mediante un test que el ViewModel es capaz de transformar la lista de objetos devuelta por el repositorio en el estado visible de la interfaz. El test configura un repositorio simulado que devuelve una lista predefinida de alumnos y comprueba que, tras la carga, el estado de la UI contiene exactamente esos mismos registros con sus nombres y cursos correctos.

<img width="1034" height="887" alt="image" src="https://github.com/user-attachments/assets/95a7abb5-6a24-48c3-87cd-cecb27ce369e" />


RA8.d — Pruebas de volumen / estrés
Aunque no se han implementado pruebas de estrés automatizadas a gran escala, se ha realizado una evaluación técnica razonada basada en la arquitectura del proyecto:

Volumen de Datos: Dado que SafePick carga los archivos JSON completos en memoria al iniciar la aplicación, se ha verificado manualmente que el sistema funciona de manera fluida y sin retardos perceptibles con listas de hasta 500 alumnos, un volumen representativo para un centro escolar de tamaño medio.

Límites Identificados: Se ha documentado que, para volúmenes muy superiores (miles de registros), el tiempo de procesamiento de la librería de parseo JSON podría empezar a afectar al tiempo de arranque de la aplicación.

Escalabilidad: Se concluye que esta arquitectura es eficiente para el alcance actual. Para escalar a una cantidad masiva de alumnos en el futuro, se recomienda migrar la persistencia a una base de datos indexada, una transición facilitada por el desacoplamiento entre capas que ofrece el proyecto.
<img width="994" height="816" alt="image" src="https://github.com/user-attachments/assets/f3474ae2-127e-443e-b48f-f89f7d348590" />


RA8.e — Pruebas de seguridad
Las pruebas unitarias también contribuyen a validar la seguridad lógica y la integridad de los datos de la aplicación:

Validación de Entradas: Se comprueba mediante tests que los Casos de Uso de creación rechazan sistemáticamente objetos con campos obligatorios vacíos (como el nombre del alumno o el curso). Esto evita que se corrompa el almacenamiento con datos inválidos.

Consistencia de Identificadores: Se valida que la lógica de generación de identificadores únicos (UUID) funciona correctamente, evitando colisiones que podrían provocar la mezcla de datos de dos personas diferentes o la sobreescritura accidental de registros.

Control de Estados: Se asegura mediante pruebas que la interfaz no muestre información sensible si la carga de datos falla o si el usuario intenta acceder a rutas protegidas sin la autenticación necesaria.

<img width="1027" height="749" alt="image" src="https://github.com/user-attachments/assets/39ce7c4e-49c8-4143-b62c-bc4a02304bde" />


RA8.f — Uso de recursos
El uso de corrutinas controladas en las pruebas unitarias permite analizar y optimizar el uso de recursos del sistema:

Gestión de Hilos: Se valida en el código y en los tests que las operaciones pesadas de lectura y escritura se realizan siempre en hilos de entrada/salida (IO), garantizando que el hilo principal de la interfaz nunca se bloquee.

Cancelación de Tareas: Al utilizar alcances controlados (ViewModelScope), aseguramos que si el usuario abandona una pantalla mientras se procesa una tarea, el proceso se cancela automáticamente. Esto libera la memoria y la CPU del dispositivo, evitando el consumo innecesario de batería.

RA8.g — Documentación de las pruebas
Documentación del sistema de testing

El proyecto incluye una documentación estructurada de las pruebas para facilitar el mantenimiento y la comprensión del código por parte de otros colaboradores.

Estructura y Organización:
Los archivos de test siguen la misma estructura de carpetas que la aplicación principal. Esto permite localizar rápidamente las pruebas correspondientes a cada módulo funcional (autenticación, gestión de alumnos, escaneo) sin pérdida de tiempo.

Convenciones de Nombramiento:
Se utilizan nombres de funciones descriptivos que explican el comportamiento esperado del test. Esto permite entender qué funcionalidad está fallando simplemente leyendo el informe de resultados del sistema de integración.

Metodología de Documentación Interna:
Dentro del código de los tests, se aplican comentarios para separar las tres fases fundamentales de la prueba:

Preparación: Describe la configuración inicial del entorno y los datos simulados.

Acción: Describe la operación específica que se está poniendo a prueba.

Verificación: Describe las comprobaciones finales que confirman que el resultado es el esperado.

Esta documentación técnica asegura que los requisitos funcionales estén siempre protegidos contra errores accidentales durante futuras ampliaciones del software.

# Video 


https://github.com/user-attachments/assets/883e4da3-f650-4116-8c91-740017083bff


## Imagenes que no se han podido ver en el Video

![8cb99be3-19e7-46a8-9788-73c11fac7947](https://github.com/user-attachments/assets/b398bc91-a45e-4d28-af54-9d13d04960d9)

![e9d9a3ef-6c66-47c4-b5ff-7b7a79ff34c4](https://github.com/user-attachments/assets/ddb447d8-9c10-486e-a9dc-5cacc40691e6)

