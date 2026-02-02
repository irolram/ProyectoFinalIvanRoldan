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

### 3. Componentes Reutilizables (RA3)
* **Modularidad:** Desarrollo de componentes aislados como
* `AlumnoDialog` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/0b7d4793195f3329389237d12d43482f1ac9c9d6/app/src/main/java/com/example/proyectofinalivanroldan/ui/components/AlumnoDialog.kt#L1-L77,
* `AddVinculoDialog` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/31ae5bca3653060cdfe6888e266b1db9c188bb1b/app/src/main/java/com/example/proyectofinalivanroldan/ui/components/AddVinculoDialog.kt#L1-L104,
* `PermissionRequest` https://github.com/irolram/ProyectoFinalIvanRoldan/blob/31ae5bca3653060cdfe6888e266b1db9c188bb1b/app/src/main/java/com/example/proyectofinalivanroldan/ui/components/PermissionRequest.kt#L1-L24
*
* para facilitar el mantenimiento y la reutilización en otras vistas (**RA3.b, c, d**).

### 4. Estándares y Usabilidad (RA4)
* **Navegación Intuitiva:** Implementación de `NavigationBar` para tutores y menús contextuales para administradores (**RA4.c, d, e**).
* **Feedback al Usuario:** Uso de `Snackbars` y validaciones en tiempo real en formularios de login para asegurar la claridad de los mensajes (**RA4.h**).

### 5. Datos e Informes (RA5 & RA6)
* **Persistencia JSON:** Arquitectura de almacenamiento en archivos locales (`JsonPersistence.kt`) estructurada y eficiente (**RA6.d**).
* **Exportación de Datos:** Funcionalidad para generar ficheros `.csv` con la relación de Tutores y Alumnos, incluyendo conteo automático de vínculos (**RA5.a, b, d**).

### 6. Distribución y Despliegue (RA7)
* **Build Optimizado:** Configuración de Gradle para generar Android App Bundles (AAB).
* **Seguridad del Código:** Activación de **R8** (`minifyEnabled true`) para ofuscación de código y reducción de tamaño (**RA7.a, c**).
* **Identidad Corporativa:** Personalización de iconos (`ic_launcher`), temas y colores corporativos (**RA7.b**).
* **Firma Digital:** App firmada con Keystore propia para garantizar integridad y actualizaciones seguras (**RA7.e**).
* **Estrategia de Distribución:** Despliegue mixto mediante Firebase App Distribution (Beta) y descarga directa vía QR para familias (**RA7.h**).

### 7. Calidad y Pruebas (RA8)
* **Pruebas de Integración:** Validación completa del flujo Cámara -> Decodificación -> Consulta JSON -> Validación de Acceso (**RA8.b**).
* **Pruebas de Regresión:** Plan de pruebas manual para verificar Login y Escaneo tras cambios en la persistencia (**RA8.c**).
* **Rendimiento (Estrés):** Testado con datasets de +50 alumnos y navegación fluida en listas `LazyColumn` con Kotlin Flows (**RA8.d**).
* **Seguridad:**
    * *Autorización:* Repositorio intermedio que impide el acceso a datos sin vínculo explícito.
    * *Permisos:* Solicitud de permisos de cámara en tiempo de ejecución ("Mínimo Privilegio") (**RA8.e**).
* **Uso de Recursos:** Optimización de CPU (15-20%) durante el análisis de imagen verificado con Android Profiler (**RA8.f**).
