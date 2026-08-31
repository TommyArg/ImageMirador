# ImageMirador

Visualizador Multimedia

---

## Propósito y Filosofía de Diseño

El propósito fundamental de **ImageMirador** es actuar como un centro de control multimedia extremadamente versatil. A diferencia de los visores convencionales que sufren de latencia al explorar carpetas masivas o aplicar transformaciones complejas, ImageMirador integra patrones de diseño de software avanzados —como Inversión de Control, concurrencia estructurada y desacoplamiento estricto de capas— para ofrecer un rendimiento plano e instantáneo.

La aplicación permite la alternancia dinámica y transparente entre gráficos estáticos, secuencias animadas y flujos de video dentro del mismo viewport, eliminando la sobrecarga estructural y proporcionando una experiencia de usuario reactiva y minimalista.

---

## Características Principales

* **Soporte Multiformato Nativo:** Procesamiento integral de imágenes de alta densidad y gráficos web (`.jpg`, `.png`, `.gif`, `.bmp`) junto con reproducción de video de alta definición (`.mp4`) en un mismo espacio de trabajo.
* **Motor de Transformaciones Acelerado por Hardware:** Zoom libre centrado por GPU, manipulación de pivotes, rotaciones ortogonales de 90° y efecto espejo (*mirroring*) ejecutados mediante transformaciones matemáticas de bajo nivel que evitan relayouts innecesarios de la escena.
* **Procesamiento Concurrente con Virtual Threads:** Carga, descompresión e interpolación asíncrona de recursos en segundo plano utilizando los **Virtual Threads** de Java 25, garantizando cero bloqueos en la UI al procesar miles de archivos.
* **Vista de Galería Overlay Transparente:** Mosaico/Grid interactivo superpuesto que genera una vista previa rápida de la carpeta abierta, permitiendo la conmutación instantánea entre elementos sin perder el contexto visual.
* **Navegación Intuitiva y Persistencia de Estado:** Exploración por teclado atómica, retención de rutas en el sistema de archivos, ventana desacoplable con soporte *Always on Top* (Pin) y selectores asíncronos no bloqueantes.
* **Absorción de Resiliencia en I/O:** Servicio de escaneo con normalización de caracteres *case-insensitive* (`.JPG` = `.jpg`), detección inteligente de tipos MIME y manejo seguro de excepciones de entrada/salida.

---

## Aspectos Técnicos Destacados

* **Java 25 Standards:** Definición de DTOs inmutables mediante Records (MediaItem) y aprovisionamiento intensivo de ejecutores livianos (Executors.newVirtualThreadPerTaskExecutor()) para tareas con cuello de botella en I/O.Pipeline de Renderizado.
* **JavaFX:** Jerarquía de nodos optimizada (ScrollPane $\rightarrow$ StackPane $\rightarrow$ Group $\rightarrow$ ImageView/MediaView) para aislar la geometría de la escena y ejecutar transformaciones de escala mediante aceleración de hardware.

---

## Requisitos del Sistema y Compilación
### Prerrequisitos
* Java Development Kit (JDK) 25 o superior.

* Apache Maven 3.9+ como herramienta de construcción y gestión de dependencias.

* Módulos nativos de JavaFX 25 (javafx.controls, javafx.fxml, javafx.media, javafx.graphics).

### Instrucciones de Ejecución

Clonar el repositorio:
```text
git clone https://github.com/Kippyru/ImageMirador.git
cd ImageMirador
```
Compilar el proyecto:
```text
mvn clean compile
```
Lanzar la aplicación:
```text
mvn javafx:run
```
Empaquetar la aplicación en un JAR ejecutable:
```text
mvn clean package
```