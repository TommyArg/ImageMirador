module org.kevin.imagemirador {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.kordamp.ikonli.core;
    requires javafx.media;
    requires java.prefs;
    requires com.google.gson;
    //requires y opens para el gson, sino no puede leer propiedades privadas
    opens org.dsf.imagemirador.Dto to com.google.gson;
    opens org.dsf.imagemirador to javafx.fxml;
    exports org.dsf.imagemirador;
    exports org.dsf.imagemirador.Controller;
    opens org.dsf.imagemirador.Controller to javafx.fxml;
}