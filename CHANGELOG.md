-  08072026 Tommy
  - Hecho  
  - Refactorizado: MediaItem DTO y FileScannerService desde MainController.
  - Añadidas nuevos formatos (incluyendo .gif y .bmp).

- 15072026 K
  - Agregue funcionalidad para cerrar la imagen, rotar, zoom, y restaurar
  - Reestructure el fxml para que soporte transformaciones
  - Cree el paquete Viewer, en un futuro se puede poner el videoViewer dentro por ejemplo

  
- 16072026 Tommy 
  - Cambiado filechooser -> directoryChooser. Posible expansión a futuro para "ver" subcarpetas, más allá del registro superficial que tiene ahora.
  - Añadidos mensajes de consola mostrando el índice de la imágen (dentro de la carpeta seleccionada).
  - Añadido una transformación a lower case todos los archivos dentro de la carpeta seleccionada porque dog.jpg ≠ Dog.JPG.
  - Añadido un poquito de error handling en FileScannerService.