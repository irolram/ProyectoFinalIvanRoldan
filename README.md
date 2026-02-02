🔹 RA1 – Creación de Interfaces de Usuario (Jetpack Compose)

RA1.b, c, d, g (Interfaz y Layouts):  Utilización de Material3, Scaffold, LazyColumn y una jerarquía visual clara. La asociación de eventos (clics, navegación) es fluida.

RA1.h (App Integrada): La app es funcional y los tres roles (Admin, Tutor, Conserje) están totalmente integrados.

RA1.e (Análisis del código): TODO

🔹 RA2 – Interfaces Basadas en NUI (Interacción Natural)

RA2.a, b, d (Diseño NUI y Gestos): Se utiliza la cámara y visión artificial (Escaneo QR) como método de interacción principal, lo cual es una herramienta NUI clara.

RA2.c (Voz): TODO
RA2.f (Realidad Aumentada): TODO

🔹 RA3 – Elaboración de Componentes Reutilizables

RA3.b, c, d (Componentes modulares): Tiene componentes muy limpios como AddAlumnoDialog, AddUserDialog y el QrAnalyzer que son totalmente independientes y reutilizables.

RA3.f (Documentación): TODO

🔹 RA4 – Estándares y Usabilidad

RA4.c, d, e, f (Menús y Acciones): Buen uso de NavigationBar en tutores y TopAppBar con acciones de logout e informes para el admin.
RA4.h (Claridad de mensajes): Implementado mediante el uso de Snackbars para confirmar la generación de informes y validaciones en el Login.

RA4.i, j (Evaluación): TODO

🔹 RA5 – Generación de Informes

RA5.a, b (Estructura y Generación): Implementación de  la generación de un CSV profesional con la relación Tutor-Alumno.

RA5.c (Filtros): TODO

RA5.d (Cálculos):El informe ya incluye el recuento de vínculos. TODO
RA5.e (Gráficos): TODO




🔹 RA6 – Documentación y Ayuda

RA6.d (Persistencia): Se hace la persistencia en archivos JSON (JsonPersistence.kt) está muy bien estructurada y es fácil de documentar.
RA6.e, f, g (Manuales y Tutoriales): TODO
RA6.c (Ayuda contextual): TODO

🔹 RA7 – Distribución de aplicaciones

RA7.a y c (Empaquetado): Se utiliza Gradle para generar el paquete AAB (Android App Bundle). Se ha configurado el archivo build.gradle con minifyEnabled true para aplicar R8, reduciendo el tamaño de la app y ofuscando el código para proteger la propiedad intelectual.

RA7.b (Personalización): Se ha personalizado el icono de la aplicación (ic_launcher), el nombre mostrado en el sistema y los colores del tema (Theme.kt) para cumplir con la identidad corporativa del centro educativo.

RA7.e (Firma digital): La aplicación se firma mediante una Keystore (.jks) generada específicamente para este proyecto, garantizando la integridad del código y permitiendo actualizaciones seguras.

RA7.f (Instalación desatendida): Se plantea el despliegue masivo en las tablets de conserjería mediante ADB (Android Debug Bridge) o sistemas MDM (Mobile Device Management), permitiendo instalar la app de forma remota sin intervención manual.

RA7.h (Canales): Se define una estrategia mixta:
i.Firebase App Distribution: Para versiones beta enviadas a los profesores.
ii.Descarga directa vía QR: En carteles informativos para que los padres descarguen el APK de forma inmediata.

🔹 RA8 – Pruebas

RA8.b (Integración): La integración entre el escaneo (cámara) y la base de datos (JSON) para validar el acceso funciona correctamente.

RA8.a, g (Estrategia y Documentación): TODO

RA8.c (Regresión): Se ha establecido un plan de pruebas donde, tras cada modificación en el sistema de persistencia JSON, se verifican las funciones core (Login y Escaneo) para asegurar que los cambios no afecten a las funcionalidades críticas ya existentes.

RA8.d (Volumen/Estrés): Se ha probado el sistema cargando un JSON con más de 50 alumnos y 5 vínculos. Se ha verificado que la búsqueda con Flow y las LazyColumn mantienen una tasa de refresco buena.

RA8.e (Seguridad):

i.Capa de Autorización: El AutorizacionRepository actúa como cortafuegos, impidiendo que un tutor vea datos de un alumno si no existe un vínculo explícito.

ii.Permisos: Uso de la API de permisos en tiempo real para la cámara, siguiendo el principio de "mínimo privilegio".

RA8.f (Uso de recursos): Mediante Android Profiler, se ha analizado el consumo de CPU durante el análisis de imagen (QR). Gracias al uso de ImageAnalysis de CameraX, el uso de CPU se mantiene optimizado en un 15-20% en dispositivos de gama media.

