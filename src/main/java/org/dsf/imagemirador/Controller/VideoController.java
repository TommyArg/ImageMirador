package org.dsf.imagemirador.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import org.dsf.imagemirador.Viewer.MediaViewer;

public class VideoController {

    @FXML private HBox videoControlsBox;
    @FXML private Label timeLabel;
    @FXML private Slider progressSlider;
    @FXML private Slider volumeSlider;

    private MediaViewer mediaViewer;

    public void setMediaViewer(MediaViewer mediaViewer) {
        this.mediaViewer = mediaViewer;

        // ¡Magia aquí! Le entregamos los controles al visor para que los conecte al video
        if (this.mediaViewer != null) {
            this.mediaViewer.linkControls(progressSlider, volumeSlider, timeLabel);
        }
    }

    @FXML
    public void playVideoMethod() {
        if (mediaViewer != null) mediaViewer.playMedia();
    }

    @FXML
    public void pauseVideoMethod() {
        if (mediaViewer != null) mediaViewer.pauseMedia();
    }

    // Método público para que el MainController pueda encender/apagar la barra
    public void setVisible(boolean visible) {
        videoControlsBox.setVisible(visible);
        videoControlsBox.setManaged(visible);
    }
}