package org.dsf.imagemirador;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.dsf.imagemirador.Controller.MainController;
import org.dsf.imagemirador.Service.ConfigService;
import org.dsf.imagemirador.Service.ThemeManager;

import java.io.IOException;

public class VisualHotApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VisualHotApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        //inyecta el stage al main controller
        MainController controller = fxmlLoader.getController();
        controller.setStage(stage);

        stage.setTitle("VisualHot!");
        stage.setScene(scene);
        stage.show();

        //espera a que theme manager este listo, si no tira error
        Platform.runLater(() -> {
            ThemeManager themeManager = controller.getThemeManager();
            if (themeManager != null) {
                ConfigService configService = new ConfigService(themeManager);
                configService.setupAutoSave();
                configService.loadConfig();
                controller.setConfigService(configService);
            }
        });
    }
}