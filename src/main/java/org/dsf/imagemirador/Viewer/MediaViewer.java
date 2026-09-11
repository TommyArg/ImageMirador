package org.dsf.imagemirador.Viewer;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.dsf.imagemirador.Dto.AppConfig;
import org.dsf.imagemirador.Dto.MediaItem;

public class MediaViewer {
    private final MediaView mediaView;

    // motor de reproducción
    private MediaPlayer mediaPlayer;
    private ScrollPane scrollPane;

    //controles visuales que envía el VideoController
    private Slider progressSlider;
    private Slider volumeSlider;
    private Label timeLabel;

    public MediaViewer(MediaView mediaView, ScrollPane scrollPane) {
        this.mediaView = mediaView;
        this.scrollPane = scrollPane;

        //el video se ajusta a la ventana
        this.mediaView.setPreserveRatio(true);

        // ancho y alto sigan siguen al ScrollPane
        if (this.scrollPane != null) {
            this.mediaView.fitWidthProperty().bind(this.scrollPane.widthProperty());
            this.mediaView.fitHeightProperty().bind(this.scrollPane.heightProperty());
        }
    }

    //guardar controles para usar al cargar el video
    public void linkControls(Slider progressSlider, Slider volumeSlider, Label timeLabel) {
        this.progressSlider = progressSlider;
        this.volumeSlider = volumeSlider;
        this.timeLabel = timeLabel;
    }

    public void loadMedia(MediaItem item, AppConfig config) {
        if (item == null) return;

        //limpiar img o vid que se estaba viendo
        clear();

        try {
            // agarra el path del file
            Media media = new Media(item.path());

            // hacemos el nuevo reproductor
            mediaPlayer = new MediaPlayer(media);

            // enchufaaa a la pantalla MediaView, lo hace mediaplayer al... si, al mediaplayer(nuestro)
            mediaView.setMediaPlayer(mediaPlayer);

            // hacemos visible
            mediaView.setVisible(true);
            mediaView.setManaged(true);

            //todos los videos inician si o si
            mediaPlayer.setAutoPlay(true);

            //checkea si la duración del video es menor que la setteada en la config
            mediaPlayer.setOnReady(() -> {
                double durationInSeconds = media.getDuration().toSeconds();

                // conecta controles sliders y textitoos
                if (progressSlider != null && volumeSlider != null && timeLabel != null) {

                    // conectam el volumen del reproductor al slider
                    mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());

                    // actualizamos los números y la barrita mientras el video avanza
                    mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                        // el slider solo se mueve si el usuario NO lo está arrastrando con el mouse
                        if (!progressSlider.isValueChanging()) {
                            progressSlider.setValue((newTime.toSeconds() / durationInSeconds) * 100.0);
                        }
                        timeLabel.setText(formatTime(newTime, media.getDuration()));
                    });

                    // permite arrastrar el slider para adelantar y retroceder
                    progressSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
                        if (!isChanging) { // Al soltar el clic
                            mediaPlayer.seek(media.getDuration().multiply(progressSlider.getValue() / 100.0));
                        }
                    });

                    // para hacer clic directo en cualquier punto de la barra
                    progressSlider.setOnMouseClicked(event -> {
                        mediaPlayer.seek(media.getDuration().multiply(progressSlider.getValue() / 100.0));
                    });
                }
                // loop
                if (config.isAutoplayShortVideos() && durationInSeconds < config.getMaxShortVideoSeconds()) {
                    System.out.println("Video corto de " + Math.round(durationInSeconds) + "s detectado. Loopiiin activado!");

                    // llega al final y pone play de vuelta
                    mediaPlayer.setOnEndOfMedia(() -> {
                        System.out.println("Terminó el loop, rebobinando uwu");
                        mediaPlayer.seek(Duration.ZERO);
                        mediaPlayer.play();
                    });

                } else {
                    System.out.println("Video normal reproduciendo una sola vez uwu");

                    // stop si o si a los videos largos
                    mediaPlayer.setOnEndOfMedia(() -> {
                        mediaPlayer.stop();
                    });
                }
            });

            System.out.println("Video cargado! Ahora procesando...  " + item.name());

        } catch (Exception e) {
            System.out.println("Error al cargar el video: " + e.getMessage());
        }
    }

    // calcula los minutos y segundos para mostrar texto "00:00 / 00:00"  (si, me costaron las matemáticas acá)
    private String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed % 60;

        int intDuration = (int) Math.floor(duration.toSeconds());
        int durationMinutes = intDuration / 60;
        int durationSeconds = intDuration % 60;

        return String.format("%02d:%02d / %02d:%02d",
                elapsedMinutes, elapsedSeconds,
                durationMinutes, durationSeconds);
    }

    public void playMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void pauseMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void resetMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO); // esto es para resetear la duración, necesita ajustes porque creo que se entorpece con el restore de imageviewer
            // yo sugiero que veamos esto más adelante, cuando refactoricemos más el main
        }
    }

    // clearrr, estoy pensando en tal vez añadir esto a un eventual controller de (valga la redundancia) controles (next, prev, + - volumen, etc)
    public void clear() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose(); // libera recursos del motor
            mediaPlayer = null;
            System.out.println("Limpiado el motooor mediaPlayer!");
        }
        mediaView.setMediaPlayer(null); // limpiamos la vista/pantalla
        mediaView.setVisible(false);
        mediaView.setManaged(false);
        System.out.println("Limpiada la pantalla mediaView!");
    }
}