package org.dsf.imagemirador.Controller;    //si o si se necesita otro controller ya que no se puede atar 2 fxml a uno

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class GalleryController {

    @FXML private VBox galleryRoot;
    @FXML private StackPane dragHandle;
    @FXML private TilePane tilePane;

    private double startY;
    private double startHeight;

    //en este controller se puede poner las miniaturas creooo o eso se haria en el toggleGalleryMethod que esta en el MainController?
    //y abria que hacer un viewer nuevo que maneje esa logica si o si
    @FXML
    public void initialize() {
        System.out.println("Controlador de galeria anda uwu");

        // para que el StackPane principal no estire la galería a pantalla completa
        galleryRoot.maxHeightProperty().bind(galleryRoot.prefHeightProperty());
        galleryRoot.setPrefHeight(140.0);

        // cuando se hace clic en el borde superior, guardamos la posición
        dragHandle.setOnMousePressed(event -> {
            startY = event.getScreenY();
            startHeight = galleryRoot.getPrefHeight();
        });

        // mientras el usuario mueve el maaus sin soltar el click (le hace click training)
        dragHandle.setOnMouseDragged(event -> {
            // calculamos la diferencia (Al revés, porque subir el mouse resta píxeles en la pantalla)
            double deltaY = startY - event.getScreenY();
            double newHeight = startHeight + deltaY;

            // límites para que no desaparezca ni ocupe toda la pantalla (mínimo 50px, máximo 600px)
            if (newHeight >= 50 && newHeight <= 600) {
                galleryRoot.setPrefHeight(newHeight);
            }
        });
    }

    // para que el MainController nos mande las miniaturas procesadas
    public TilePane getTilePane() {
        return tilePane;
    }
}
