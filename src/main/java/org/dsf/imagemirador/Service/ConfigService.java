package org.dsf.imagemirador.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.dsf.imagemirador.Dto.AppConfig;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;


public class ConfigService {
    // ANTES USÁBAMOS PREFERENCES Y HACÍA QUE SE GUARDE TOoDO EN LOS REGISTROS, BASICAMENTE USA COSO NATIVO DEL SO PARA REALIZAR LA PERSISTENCIA
    // AHORA LO IDEAL ES ESTO: SE GUARDA DONDE EL PROGRAMA ESTE ASI NO CREA COSAS INNCESESARIAS
    // POR EJEMPLO, SI YO ELIMINO EL PROGRAMA, EL ARCHIVO DE CONFIG VA A SEGUIR EXISTIENDO (si fuera preferences), EN CAMBIO SI HAGO QUE ESTE EN LA MISMA CARPETA SE BORRA TAMBIEN

    // private final Preferences prefs = Preferences.userNodeForPackage(ConfigService.class); //Ya cambiado, no hacia falta gritar...

    private static final String CONFIG_FILE = "config.json";
    private final ThemeManager themeManager;
    private Gson gson;
    private AppConfig currentConfig;

    public ConfigService(ThemeManager themeManager) {
        this.themeManager = themeManager;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    //GUARDA config
    public void saveConfig() {
        if (currentConfig == null) {
            currentConfig = new AppConfig();
        }

        String themeName = themeManager.getCurrentTheme().name();
        currentConfig.setThemeName(themeName);

        try (Writer writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(currentConfig, writer);
            System.out.println("Configuración guardada ♡");
        } catch (Exception e) {
            System.err.println("Error al guardar la configuración JSON: " + e.getMessage());
        }
    }

    //CARGA config
    public void loadConfig() {
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                currentConfig = gson.fromJson(reader, AppConfig.class);
            } catch (Exception e) {
                System.err.println("Error leyendo config.json, usando valores por defecto: " + e.getMessage());
                currentConfig = new AppConfig();
            }
        } else {
            currentConfig = new AppConfig();
        }

        // config cargada al ThemeManager
        if (themeManager != null) {
            try {
                String savedTheme = currentConfig.getThemeName();
                ThemeManager.Theme theme = ThemeManager.Theme.valueOf(savedTheme);
                themeManager.setTheme(theme);
                System.out.println("Configuración cargada: " + theme.getDisplayName());
            } catch (Exception e) {
                System.err.println("Error cargando config de tema, aplicando default: " + e.getMessage());
                themeManager.setTheme(ThemeManager.Theme.Claro); // Default
            }
        }

    }

    //Listener para guardar cambios automaticamente
    public void setupAutoSave() {
        themeManager.themeProperty().addListener((obs, old, newVal) -> {
            saveConfig();
        });
    }

    // Métoodo extra para exponer la configuración a otros controladores (para el KAN-27)
    public AppConfig getConfig() {
        if (currentConfig == null) loadConfig();
        return currentConfig;
    }
}