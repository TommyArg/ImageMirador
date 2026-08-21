package org.dsf.imagemirador.Controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.dsf.imagemirador.Dto.MediaItem;
import org.dsf.imagemirador.Service.FileScannerService;
import org.dsf.imagemirador.Service.ThumbnailService;
import org.dsf.imagemirador.Viewer.ImageViewer;
import org.dsf.imagemirador.Viewer.MediaViewer;
import org.dsf.imagemirador.Viewer.NavigationViewer;

import java.io.File;
import java.util.List;

public class MainController {

    @FXML private ImageView imageWindow;
    @FXML private ScrollPane scrollPane;
    @FXML private Group imageGroup;
    @FXML private CheckMenuItem checkMirror;
    @FXML private CheckMenuItem alwaysOnTop;
    @FXML private CheckMenuItem checkGallery;
    @FXML private MediaView mediaWindow;

    @FXML private javafx.scene.Node galleryView;
    @FXML private GalleryController galleryViewController;

    private final FileScannerService fileScannerService = new FileScannerService();
    private final ThumbnailService thumbnailService = new ThumbnailService();
    private final NavigationViewer navigator = new NavigationViewer();

    private ImageViewer imageViewer;
    private MediaViewer mediaViewer;
    private Stage stage;

    //el controlador recibe el Stage
    public void setStage(Stage stage) {
        this.stage = stage;
        if (imageViewer != null) {
            imageViewer.setStage(stage); //sin esto no anda, la cosa es que es un codigo que se repite lo anterior, no?
        }
    }

    @FXML
    public void initialize() {
        imageViewer = new ImageViewer(imageWindow, scrollPane, imageGroup, checkMirror);
        mediaViewer = new MediaViewer(mediaWindow);

        //por default, la galeria inicia oculta
        galleryView.setVisible(false);
        galleryView.setManaged(false);
    }

    @FXML
    public void openMethod() {
        System.out.println("Snif snif SNIIIF a ver busco tu cuestión...");
        Window window = imageWindow.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Elegí un archivo");
        fileChooser.getExtensionFilters().add(fileScannerService.getSupportedExtensionsFilter());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));

        File lastDir = fileScannerService.getLastDirectory();
        if (lastDir != null && lastDir.exists()) {
            fileChooser.setInitialDirectory(lastDir);
        }

        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile == null) {
            System.out.println("Selección cance-helada");
            return;
        }

        //se crea el Task para seleccionar archivos
        Task<List<MediaItem>> scanTask = fileScannerService.createScanTask(selectedFile);

        scanTask.setOnSucceeded(event -> {
            List<MediaItem> loadedFiles = scanTask.getValue();
            int startingIndex = fileScannerService.getSelectedFileIndex();

            if (loadedFiles != null && !loadedFiles.isEmpty()) {
                navigator.load(loadedFiles, startingIndex);
                System.out.println("Guau guau! Encontré " + navigator.getTotal() + " archivos compatibles!");

                // limpiamos la cuadrícula anterior por si abrimos una carpeta nueva
                TilePane thumbnailGrid = galleryViewController.getTilePane();
                if (thumbnailGrid != null) thumbnailGrid.getChildren().clear();

                for (MediaItem item : loadedFiles) {
                    thumbnailService.loadThumbnailAsync(item, thumbnail -> {
                        // acá van las miniaturasss
                        ImageView thumbView = new ImageView(thumbnail);
                        thumbView.setFitWidth(100);
                        thumbView.setFitHeight(100);
                        thumbView.setPreserveRatio(true);

                        // otro contenedor, este es para que el file se quede si o si en el cuadro de 100x100 centrado
                        StackPane thumbContainer = new StackPane(thumbView);
                        thumbContainer.setPrefSize(100, 100);

                        //cuando clickean a la miniatura, busca la posición y la pasa al visor grande
                        thumbContainer.setOnMouseClicked(e -> {
                            navigator.setIndex(loadedFiles.indexOf(item));
                            showFile();
                        });

                        // miniatura añadida a la cuadrícula
                        if (thumbnailGrid != null) thumbnailGrid.getChildren().add(thumbContainer);
                    });
                }

                // mostrar galería automáticamente despuéees de elegir el archivo y actualizar el check del menú
                if (!checkGallery.isSelected()) {
                    checkGallery.setSelected(true);
                    toggleGalleryMethod();
                }

                showFile();
            } else {
                System.out.println("Snif snif, no encontré ningún archivo compatible en esta carpeta...");
            }
        });

        scanTask.setOnFailed(event -> {
            System.err.println("Error fatal... CHOVER");
            scanTask.getException().printStackTrace();
        });

        //ejecuta el hilo
        Thread thread = new Thread(scanTask);
        thread.setDaemon(true);
        thread.start();
    }

    private void showFile() {
        MediaItem currentItem = navigator.getCurrent();
        if (currentItem == null) return;

        // limpiamos los 2 visores por las dudas
        imageViewer.clear();
        mediaViewer.clear();

        // switch para ver si es vid o img (ahora inteligente con el enum)
        if (currentItem.type() == MediaItem.MediaType.VIDEO) {
            // si es formato mp4 o mov viene mediaviewer
            System.out.println("Encontré tu videooo, agarra croquetas que empieza");
            mediaViewer.loadMedia(currentItem);
        } else {
            // si no es video, se lo mandamos al ImageViewer
            imageViewer.showImage(currentItem);
        }
    }

    @FXML
    public void toggleGalleryMethod() {
        boolean isVisible = checkGallery.isSelected();
        galleryView.setVisible(isVisible);
        galleryView.setManaged(isVisible);

        if (isVisible) {
            System.out.println("Galeria abierta OwO");
        } else {
            System.out.println("Galeria cerrada UnU");
        }
    }

    //navegacion, puse alt + right, porq right solo a veces no funciona, o si apreto para rotar tambien cuenta y rota y cambia de imagen
    @FXML
    public void rightMethod() {
        if (navigator.next() != null) {
            showFile();
            System.out.println("Derecha uwu -> Viendo archivo " + (navigator.getIndex() + 1) + " de " + navigator.getTotal());
        }
    }

    @FXML
    public void leftMethod() {
        if (navigator.previous() != null) {
            showFile();
            System.out.println("Izquierda uwu -> Viendo archivo " + (navigator.getIndex() + 1) + " de " + navigator.getTotal());
        }
    }

    @FXML
    public void closeMethod() {
        System.out.println("closeada tu wea >:3c");
        imageViewer.clear();
        mediaViewer.clear();
        navigator.clear();
    }

    @FXML public void alwaysOnTopMethod() { imageViewer.alwaysOnTop(alwaysOnTop.isSelected()); } //LITERALMENTE YO
    @FXML public void rotateRightMethod() { imageViewer.rotateRight(); }
    @FXML public void rotateLeftMethod() { imageViewer.rotateLeft(); }
    @FXML public void mirrorMethod() { imageViewer.mirror(checkMirror.isSelected()); }
    @FXML public void plusZoomMethod() { imageViewer.zoomIn(); }
    @FXML public void minusZoomMethod() { imageViewer.zoomOut(); }
    @FXML public void restoreMethod() { imageViewer.restore(); }

    @FXML public void scrollZoomMethod(ScrollEvent event) {
        imageViewer.scrollZoom(event.getDeltaY());
        event.consume();
    }
}