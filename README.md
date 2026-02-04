# SafePick: Sistema de Gestión de Recogida de Alumnos

Aplicación nativa Android desarrollada en **Kotlin** y **Jetpack Compose** para gestionar la seguridad en la recogida de alumnos en centros educativos. El sistema permite roles diferenciados (Administrador, Tutor y Conserje) y utiliza tecnologías de **Visión Artificial (QR)** para validar accesos en tiempo real sin conexión a internet (*Offline First*).

---

## Características Principales

* **Gestión de Roles:** Arquitectura integrada con 3 perfiles de usuario (Admin, Tutor, Conserje) en una única app (**RA1.h**).
* **Escáner Inteligente:** Uso de `CameraX` y `ML Kit` para lectura de credenciales QR (**RA2.a**).
* **Seguridad Offline:** Persistencia de datos mediante JSON cifrados localmente, garantizando la privacidad (**RA6.d**).
* **Informes:** Generación automática de reportes CSV de vínculos familiares (**RA5.a**).
* **UI Moderna:** Interfaz construida 100% en **Jetpack Compose** con Material Design 3.

---

## Competencias Técnicas Cubiertas (RAs)

Este proyecto ha sido desarrollado siguiendo los Resultados de Aprendizaje (RA) del ciclo de Desarrollo de Aplicaciones Multiplataforma.

### 1. Interfaz de Usuario (RA1 - Jetpack Compose)
* **Diseño Declarativo:** Uso intensivo de `Scaffold`, `LazyColumn` y `TopAppBar` para crear una jerarquía visual clara y navegable (**RA1.b, c, d**).
* **Experiencia Fluida:** Gestión optimizada de eventos de clic y navegación entre pantallas mediante `NavHost`.
* **Integración Total:** La interfaz adapta su comportamiento dinámicamente según el rol logueado (**RA1.h**).

### 2. Interacción Natural (NUI) (RA2)
* **Visión Artificial:** Implementación de gestos y uso de cámara como método principal de interacción para el rol de Conserje (**RA2.a, b, d**).
* **Tecnología:** Uso de `CameraX` + `ML Kit` para un escaneo rápido y preciso.

### Posibles mejoras RA2.c, e, f
Aunque SafePick utiliza la Visión Artificial como eje central, se ha realizado un análisis crítico sobre la viabilidad de otras interfaces naturales para cumplir con los estándares de innovación y accesibilidad:

2.1. Interacción por Voz (RA2.c)
Se evaluó la implementación de un sistema Speech-to-Text (STT) para permitir al conserje activar el escáner mediante comandos de voz.

Decisión: Se descartó su inclusión en la versión actual para evitar falsos positivos derivados del ruido ambiente típico en las salidas escolares.

Valoración de futuro: Se considera una mejora necesaria para la accesibilidad universal, permitiendo que tutores con diversidad funcional motórica puedan interactuar con la aplicación sin necesidad de contacto físico con el dispositivo.

2.2. Detección Facial y Corporal (RA2.e)
Se analizó la posibilidad de sustituir el código QR por reconocimiento biométrico facial para la identificación de los tutores.

Decisión: Se desestimó por implicaciones éticas y legales. Bajo el marco del RGPD, el tratamiento de datos biométricos de alta sensibilidad en un entorno con presencia de menores requiere una infraestructura de seguridad y consentimientos legales que exceden el propósito de agilidad del sistema. El código QR actúa como un "token" físico que garantiza un equilibrio óptimo entre seguridad y privacidad.

2.3. Realidad Aumentada (RA2.f)
Se ha diseñado conceptualmente la integración de capas de RA sobre el preview de la cámara.

Propuesta técnica: Uso de la librería ARCore para proyectar un indicador visual dinámico (bounding box) y guías de profundidad sobre la interfaz.

Utilidad: Esto facilitaría el trabajo del conserje en condiciones de baja luminosidad o gran distancia, indicando visualmente el área exacta donde el algoritmo de ML Kit está detectando el patrón del código QR antes de realizar la validación.

### 3. Componentes Reutilizables (RA3)
* **Modularidad:** Desarrollo de componentes aislados como
* `AlumnoDialog` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/0b7d4793195f3329389237d12d43482f1ac9c9d6/app/src/main/java/com/example/proyectofinalivanroldan/ui/components/AlumnoDialog.kt#L1-L77,
* `AddVinculoDialog` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/31ae5bca3653060cdfe6888e266b1db9c188bb1b/app/src/main/java/com/example/proyectofinalivanroldan/ui/components/AddVinculoDialog.kt#L1-L104,
* `PermissionRequest` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/31ae5bca3653060cdfe6888e266b1db9c188bb1b/app/src/main/java/com/example/proyectofinalivanroldan/ui/components/PermissionRequest.kt#L1-L24
* para facilitar el mantenimiento y la reutilización en otras vistas (**RA3.b, c, d**).

# 4. Estándares y Usabilidad (RA4)

## RA4.a Estándares de Aplicación

El proyecto se ha desarrollado siguiendo estrictamente el estándar **Material Design 3 (M3)** de Google, implementado a través de la biblioteca **Jetpack Compose**. Esto garantiza una consistencia visual y de comportamiento con el ecosistema Android actual.

* **Implementación Técnica:** Se han utilizado los componentes fundamentales de M3 como `Scaffold` para la estructura base, `TopAppBar` para la cabecera y `NavigationBar` para la navegación jerárquica.
* **Tipografía y Color:** Se utiliza `MaterialTheme` para gestionar centralizadamente la paleta de colores (soporte nativo para Tema Claro/Oscuro) y la escala tipográfica (`Headline`, `Body`, `Label`), asegurando que todos los textos mantengan proporciones legibles.
* **Iconografía:** Uso de la librería `androidx.compose.material:material-icons-extended` para iconos estandarizados (Filled y Outlined) reconocibles universalmente.

## RA4.b Valoración de Estándares

La adhesión a los estándares de Material Design no es solo una decisión estética, sino funcional y económica:

1.  **Reducción de la Carga Cognitiva:** Al utilizar patrones de diseño estándar (como el botón flotante FAB para acciones principales o la navegación inferior), el usuario no necesita "aprender" a usar la app. Aprovechamos su memoria muscular y conocimientos previos de otras apps Android.
2.  **Accesibilidad Implícita:** Los componentes estándar de Compose ya cumplen con los requisitos mínimos de accesibilidad (tamaño de zona táctil de 48dp, contraste de colores, soporte para lectores de pantalla TalkBack).
3.  **Eficiencia en Desarrollo:** Permite centrar el esfuerzo en la lógica de negocio (Gestión de QR y JSON) en lugar de reinventar componentes visuales básicos.

## RA4.c Menús y Navegación

La estructura de navegación se adapta al rol del usuario para maximizar la eficiencia:

* **Rol Tutor (Navegación Inferior):** Se ha implementado una `NavigationBar` (Bottom Navigation) con dos destinos claros: "Mis Alumnos" y "Pase QR". Este tipo de menú es el estándar para navegación de primer nivel en móviles, permitiendo cambiar de contexto con una sola mano y un solo clic.
* **Rol Admin (Navegación Contextual):** Se utiliza la `TopAppBar` con un menú de "overflow" (tres puntos) para acciones secundarias como "Cerrar Sesión" o "Exportar CSV", dejando el área principal limpia para la gestión de listas.
* **Rol Conserje (Navegación Lineal):** Al ser una tarea de flujo único (Escanear -> Resultado), se minimizan los menús para evitar distracciones, priorizando la vista de cámara.

## RA4.d Distribución de Acciones
Las acciones se han priorizado visualmente según su frecuencia de uso (Ley de Fitts):

* **Acciones Primarias :** El botón de "Escanear QR" (Conserje) o "Añadir Alumno" (Admin) se presentan como botones flotantes (`FloatingActionButton`) o botones rellenos (`Button`), ubicados en la zona inferior derecha, la más accesible para el pulgar.
* **Acciones Secundarias :** La navegación entre pestañas o confirmar diálogos usa botones o iconos estándar.
* **Acciones Destructivas/Poco Frecuentes:** El "Logout" o el borrado de datos se relegan a las esquinas superiores o requieren confirmación, evitando clics accidentales.

## RA4.e Distribución de Controles
La maquetación utiliza una jerarquía visual clara basada en el sistema de grid de 8dp:

* **Agrupación Lógica:** En la `TutorScreen`,https://github.com/irolram/ProyectoFinalIvanRoldan/blob/2794a651e724d61eb6aa543afc8f09500e337356/app/src/main/java/com/example/proyectofinalivanroldan/ui/mainScreen/TutorScreen.kt#L1-L169
   la información del alumno se agrupa dentro de `Cards` (Tarjetas) con elevación, separándola visualmente del fondo.
* **Espaciado:** Uso consistente de `Spacer` y `padding` (16dp para márgenes laterales, 8dp entre elementos) para evitar la sensación de abigarramiento y permitir que la interfaz "respire".
* **Alineación:** Los formularios de Login y Registro mantienen una alineación central vertical para focalizar la atención del usuario en la introducción de credenciales.

## RA4.f Elección de Controles
Cada componente se ha elegido por su idoneidad semántica:

* **`OutlinedTextField`:** Para formularios de texto. Se prefiere sobre el `TextField` relleno porque los bordes definidos ayudan a delimitar mejor el área de interacción en pantallas con mucho brillo.
* **`LazyColumn`:** Para las listas de alumnos. A diferencia de un `Column` con scroll, este componente recicla las vistas, lo cual es crítico para el rendimiento cuando la lista de alumnos crece.
* **`Dialog`:** Para añadir alumnos o usuarios. Permite realizar una acción compleja sin perder el contexto de la pantalla de fondo.

## RA4.g Diseño Visual

* **Identidad Corporativa:** Se ha personalizado el `Theme.kt` para usar colores que transmiten seguridad y calma, evitando el rojo salvo para errores críticos.
* **Feedback Visual:** Los elementos interactivos tienen estados de "pressed" y "ripple"  para confirmar al usuario que su toque ha sido registrado.

## RA4.h Claridad de Mensajes

El sistema de feedback se adapta a la criticidad del evento:

* **Feedback Invasivo (Dialogs):** Para errores críticos o confirmaciones irreversibles ("¿Seguro que desea eliminar al alumno?").
* **Feedback No Invasivo (Snackbars):** Para confirmaciones de éxito ("Alumno guardado correctamente", "CSV exportado"). Aparecen abajo y desaparecen solas.
* **Validación en Tiempo Real:** En el Login, si el usuario deja campos vacíos, aparecen textos de ayuda en color rojo (`MaterialTheme.colorScheme.error`) justo debajo del campo afectado, guiando la corrección inmediata.
* **Lenguaje:** Se usa terminología del dominio educativo ("Tutor", "Alumno", "Conserje") en lugar de términos técnicos ("User", "Item", "Admin").

## RA4.i Pruebas de Usabilidad
Se ha realizado una **Evaluación Heurística** basada en los 10 principios de Nielsen:

1.  **Visibilidad del estado del sistema:** Cuando el conserje escanea, se muestra un indicador de carga (`CircularProgressIndicator`) si el proceso demora, y la pantalla cambia de color (Verde/Rojo) para indicar Acceso/Denegado instantáneamente.
2.  **Prevención de errores:** Los campos numéricos (como teléfono) fuerzan el teclado numérico, evitando que se introduzcan letras.
3.  **Simulación de Usuario:** Se probó el flujo "Llegada del padre -> Muestra QR -> Escaneo" midiendo el tiempo de interacción, logrando reducirlo a menos de 5 segundos, lo cual es vital para evitar colas en la entrada del colegio.

## RA4.j Evaluación en Dispositivos
La interfaz es "Responsive" y se ha validado en diferentes escenarios:

* **Densidad de Pantalla:** Probado en emuladores Pixel 5 para asegurar que los iconos no se deforman.
* **Tamaño de Fuente:** La app respeta la configuración de "Tamaño de fuente" del sistema Android. Si un usuario con problemas de visión aumenta la letra, la `LazyColumn` y los textos se adaptan sin cortarse ni solaparse.
* **Orientación:** Aunque la app está diseñada para *Portrait* (Vertical), el uso de `Scaffold` y `Box` con `fillMaxSize` asegura que no crashea si se rota la tablet del conserje.

# 5. Informes (RA5)

## RA5.a – Estructura del Informe

Se ha implementado la exportación en formato estándar **CSV (Comma-Separated Values)**. El archivo incluye **cabeceras de metadatos** (`ID; NOMBRE; ALUMNOS`) y utiliza codificación **UTF-8** y punto y coma (`;`) como separador, garantizando la interoperabilidad total con Excel y sistemas de gestión educativa sin errores de caracteres.

## RA5.b – Generación desde Fuentes de Datos

El informe se genera dinámicamente combinando datos de múltiples fuentes (`UsuarioRepository`https://github.com/irolram/ProyectoFinalIvanRoldan/blob/bc5f6ab91d547b6eb3e88c9786577ea07bd4d06a/app/src/main/java/com/example/proyectofinalivanroldan/data/repository/UsuarioRepository.kt#L1-L77
y `VinculoRepository` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/bc5f6ab91d547b6eb3e88c9786577ea07bd4d06a/app/src/main/java/com/example/proyectofinalivanroldan/data/repository/VinculoRepository.kt#L1-L55). 
El sistema cruza usuarios y permisos en tiempo real y utiliza la API `MediaStore` de Android para guardar el resultado en la carpeta de descargas pública, facilitando su uso externo.

## RA5.c – Filtros sobre Valores

Se aplica lógica de negocio para filtrar los datos brutos antes de la exportación:
* **Filtro por Rol:** Se excluyen administradores y conserjes, listando únicamente a los Tutores activos.
* **Filtro de Privacidad:** Se eliminan datos sensibles como contraseñas, dejando solo la información pública necesaria para la gestión de recogidas.

## RA5.d – Valores Calculados y Totales

El informe enriquece la información base mediante cálculos automáticos en Kotlin:
* **Conteo de Vínculos:** Columna calculada `TOTAL_ALUMNOS` para verificar rápidamente cuántos niños tiene asignados cada tutor.
* **Marca Temporal:** Inserción automática de la fecha/hora de generación para auditoría y control de versiones del documento.

## RA5.e – Gráficos Generados

La aplicación genera gráficos dinámicos en tiempo real a partir de los datos del usuario:
* **Transformación:** Convierte cadenas de texto (IDs) en matrices gráficas bidimensionales (**Códigos QR**) usando la librería `ZXing`.
* **Funcionalidad:** Este gráfico no es decorativo, sino la base funcional del sistema de acceso, permitiendo la transmisión visual de datos segura y sin conexión.

# 6. Documentación (RA6)

## RA6.a – Identificación de Sistemas de Ayuda

Se han seleccionado herramientas estándar de la industria para los distintos niveles de documentación:
* **Código Fuente:** Uso de **KDoc** (estándar de Kotlin) para documentar clases y funciones complejas, permitiendo generar documentación HTML automática si fuera necesario.
* **Usuario Final:** Uso de **Markdown** (.md) en el repositorio para guías visuales y legibles desde cualquier navegador.
* **Interfaz:** Uso de los parámetros `supportingText` y `placeholder` de Material Design 3 para integrar la ayuda directamente en los componentes visuales.

## RA6.b – Formatos de Ayuda

La ayuda se presenta en formato híbrido para maximizar la accesibilidad:
* **Formato Web/Repositorio (README):** Documentación centralizada accesible online con capturas de pantalla y badges de estado.
* **Formato In-App (Tooltips):** Pequeños textos de ayuda integrados en la interfaz que guían al usuario sin salir de la aplicación.
* **Formato Estático:** Posibilidad de exportar la documentación a PDF para entregables administrativos del centro educativo.

## RA6.c – Ayudas Sensibles al Contexto

El sistema ofrece asistencia en el momento exacto en que el usuario la necesita (Just-in-Time Support):
* **Placeholders y Etiquetas:** Los campos de texto (`OutlinedTextField`) indican qué dato se espera (ej: "Ej: 12345678Z") antes de que el usuario escriba.
* **Feedback de Error:** Si el login falla o falta un campo, aparece un mensaje en rojo (`ErrorColor`) justo debajo del campo afectado o mediante un `Snackbar` temporal, explicando la causa específica del error.

## RA6.d – Estructura de Información Persistente

Se ha documentado detalladamente el esquema de persistencia JSON que sustenta la aplicación "Offline First":
* **Archivos:** `usuarios.json` (Credenciales y Roles) y `vinculos.json` (Relación N:M Tutor-Alumno).
* **Esquema de Datos:** Definición clara de los tipos de datos (UUID como String, Roles como Enum, Fechas en formato ISO-8601).
* **Integridad:** Documentación sobre cómo el `VinculoRepository` asegura que no existan vínculos huérfanos (referencias a IDs inexistentes).

## RA6.e,g – Manual de Usuario

# Manual de Usuario - SafePick

Bienvenido a **SafePick**, la aplicación segura para la gestión de recogida de alumnos. Esta guía le ayudará a utilizar la aplicación según su perfil de usuario (Administrador, Tutor o Conserje).

---

## 1. Primeros Pasos: Acceso al Sistema

Al abrir la aplicación, verá la pantalla de inicio de sesión.

1.  Introduzca su **Nombre de Usuario** y **Contraseña**.
2.  Pulse el botón **"Entrar"**.
3.  Si faltan datos, el sistema le avisará con un mensaje en rojo.

> **Nota:** La aplicación detectará automáticamente su rol y le dirigirá a su pantalla correspondiente.

---

## 2. Perfil TUTOR (Familias)

Su función principal es identificarse en la puerta del colegio para recoger a sus hijos.

### 2.1. Ver mis Alumnos vinculados
Al entrar, verá la pestaña **"Mis Alumnos"** en la barra inferior.

* Aquí aparece una lista con el nombre y curso de los hijos que tiene asignados.
* Si la lista está vacía, contacte con Administración.

### 2.2. Generar Pase de Recogida (QR)
Para recoger al alumno, siga estos pasos:

1.  Pulse el botón **"Pase QR"** en la barra de navegación inferior.
2.  Se generará automáticamente un código QR único en su pantalla.
3.  Muestre este código al personal de conserjería en la puerta.

> **Consejo:** Aumente el brillo de su móvil si el escáner no detecta el código rápidamente.

---

## 3. Perfil CONSERJE (Seguridad)

Su función es validar si la persona que viene a recoger al alumno tiene autorización.

### 3.1. Uso del Escáner
1.  Al iniciar sesión, la cámara se activará automáticamente.
2.  Si es la primera vez, pulse **"Permitir"** cuando la app solicite acceso a la cámara.
3.  Apunte con la cámara al código QR del tutor.

### 3.2. Interpretación de Resultados
El sistema le dará una respuesta visual inmediata:

* **PANTALLA VERDE + "ACCESO AUTORIZADO":** El tutor es válido. Aparecerá el nombre del tutor y la lista de alumnos que puede llevarse.
* **PANTALLA ROJA + "ACCESO DENEGADO":** El código no existe o no es válido. **No entregue al alumno.**

---

## 4. Perfil ADMINISTRADOR (Gestión)

Usted es el encargado de gestionar la base de datos del centro.

### 4.1. Gestión de Usuarios
* **Añadir Usuario:** Pulse el botón flotante `+` y seleccione **"Añadir Usuario"**. Rellene los datos (Nombre, Contraseña, Rol).
* **Ver Usuarios:** La pantalla principal muestra el listado de todos los usuarios registrados.

### 4.2. Gestión de Alumnos y Vínculos
Para asignar un alumno a un tutor:

1.  Pulse sobre la tarjeta de un Tutor en la lista.
2.  Seleccione la opción **"Vincular Alumno"**.
3.  Introduzca los datos del alumno (Nombre, Curso).

### 4.3. Informes y Auditoría
Para obtener un registro de las autorizaciones actuales:

1.  Pulse el menú de opciones (tres puntos) en la esquina superior derecha.
2.  Seleccione **"Exportar CSV"**.
3.  El archivo `informe_recogidas.csv` se guardará en la carpeta de **Descargas** de su dispositivo.
   
## RA6.f – Manual Técnico de Instalación

Documentación dirigida al equipo de TI del centro para el despliegue:
* **Requisitos:** Android 8.0+ (Min SDK 26) y cámara trasera.
* **Dependencias:** Listado de librerías en `build.gradle` (CameraX, Gson, ZXing).
* **Permisos:** Explicación del flujo de solicitud de permisos en tiempo de ejecución (`Manifest.permission.CAMERA`).
* **Compilación:** Instrucciones para generar el APK firmado o el AAB desde Android Studio.

### 7. Distribución de aplicaciones (RA7)
* **Build Optimizado:** Configuración de Gradle para generar Android App Bundles (AAB).
* **Seguridad del Código:** Activación de **R8** (`minifyEnabled true`) para ofuscación de código y reducción de tamaño (**RA7.a, c**).
* **Identidad Corporativa:** Personalización de iconos (`ic_launcher`), temas y colores corporativos (**RA7.b**).
* **Firma Digital:** App firmada con Keystore propia para garantizar integridad y actualizaciones seguras (**RA7.e**).
* **Estrategia de Distribución:** Despliegue mixto mediante Firebase App Distribution (Beta) y descarga directa vía QR para familias (**RA7.h**).

### 8. Pruebas avanzadas (RA8)
### RA8.g – Documentación de Pruebas y Resultados
Para garantizar la fiabilidad de SafePick, se ha generado un informe de resultados basado en las pruebas automáticas y manuales realizadas. La evidencia técnica principal reside en la suite de Tests Unitarios, que permite verificar el comportamiento del software de forma objetiva.
https://github.com/irolram/ProyectoFinalIvanRoldan/blob/88c2bb56c6de76a7f9a3123c1b89c1ad4a72c058/app/src/test/java/com/example/proyectofinalivanroldan/SafewPickLogicTest.kt#L1-L43

* Se ha implementado la clase de prueba AccesoValidatorTest para validar el motor de seguridad. A continuación, se detalla el resultado de la ejecución en el entorno de desarrollo (Android Studio):

* Prueba acceso_concedido_cuando_tutor_y_alumno_estan_vinculados: El sistema confirmó correctamente la relación existente en el JSON simulado. Estado: PASSED 

* Prueba acceso_denegado_cuando_tutor_no_esta_vinculado_al_alumno: El sistema bloqueó con éxito un intento de acceso con un ID de alumno no relacionado. Estado: PASSED 

* **Pruebas de Integración:** Validación completa del flujo Cámara -> Decodificación -> Consulta JSON -> Validación de Acceso (**RA8.b**).
* **Pruebas de Regresión:** Plan de pruebas manual para verificar Login y Escaneo tras cambios en la persistencia (**RA8.c**).
* **Rendimiento (Estrés):** Testado con datasets de +50 alumnos y navegación fluida en listas `LazyColumn` con Kotlin Flows (**RA8.d**).
* **Seguridad:**
    * *Autorización:* Repositorio intermedio que impide el acceso a datos sin vínculo explícito.
    * *Permisos:* Solicitud de permisos de cámara en tiempo de ejecución ("Mínimo Privilegio") (**RA8.e**).
