package org.dsf.imagemirador.Service;

import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import org.dsf.imagemirador.Dto.MediaItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileScannerService {

    private File lastDirectory;
    private List<MediaItem> cachedFiles = new ArrayList<>();
    private int selectedFileIndex = 0;

    // formatos permitidos
    private static final List<String> SUPPORTED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".mp4", ".jfif", ".mov");

    public File getLastDirectory() {
        return lastDirectory;
    }

    //devuelve un Task para que ande en segundo plano
    public Task<List<MediaItem>> createScanTask(File selectedFile) {
        return new Task<>() {
            @Override
            protected List<MediaItem> call() throws Exception {
                lastDirectory = selectedFile.getParentFile();
                cachedFiles = scanAndCacheDirectory(lastDirectory.toPath());
                selectedFileIndex = findFileIndex(selectedFile.getName());
                return cachedFiles;
            }
        };
    }

    public FileChooser.ExtensionFilter getSupportedExtensionsFilter() {
        return new FileChooser.ExtensionFilter(
                "Archivos Soportados",
                SUPPORTED_EXTENSIONS.stream()
                        .map(ext -> ext.startsWith(".") ? "*" + ext : "*." + ext)
                        .toArray(String[]::new)
        );
    }

    private MediaItem.MediaType determineMediaType(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        if (fileName.endsWith(".mp4") || fileName.endsWith(".mov")) {
            return MediaItem.MediaType.VIDEO;
        }
        return MediaItem.MediaType.IMAGE;
    }


    //escaneo y creacion de DTOs
    private List<MediaItem> scanAndCacheDirectory(Path directoryPath) {
        try (Stream<Path> paths = Files.list(directoryPath)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(this::isSupportedFile)
                    .sorted()
                    .map(path -> new MediaItem(
                            path.toUri().toString(),
                            path.getFileName().toString(),
                            determineMediaType(path)
                    ))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Ehm... Error al leer la carpeta: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    //para que sepa donde esta parado basicamente y abra esa imagen seleccionada especifica despues en el controller
    private int findFileIndex(String fileName) {
        for (int i = 0; i < cachedFiles.size(); i++) {
            if (cachedFiles.get(i).name().equals(fileName)) {
                return i;
            }
        }
        return 0;
    }

    public int getSelectedFileIndex() {
        return selectedFileIndex;
    }

    private boolean isSupportedFile(Path path) {
        // testeando aprendí que las extensiones pueden ser mayúsculas, y eso hace que dejen de ser aceptadas.
        // Esto lo arreglamos haciendo que todito se vuelva minúscula y listoo.
        String fileName = path.getFileName().toString().toLowerCase();
        return SUPPORTED_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }

    //determina el tipo de media
    private MediaItem.MediaType determineMediaType(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        if (fileName.endsWith(".mp4") || fileName.endsWith(".mov")) {
            return MediaItem.MediaType.VIDEO;
        }
        return MediaItem.MediaType.IMAGE;
    }
}