package org.dsf.imagemirador.Service;

import java.util.prefs.Preferences;

public class ConfigService {
    //EL PREFERENCES HACE QUE SE GUARDE TODO EN LOS REGISTROS, BASICAMENTE USA COSO NATIVO DEL SO PARA REALIZAR LA PERSISTENCIA
    //PERO CREO QUE LO IDEAL SEA QUE SE GUARDE DONDE EL PROGRAMA ESTE ASI NO CREA COSAS INNCESESARIAS
    //POR EJEMPLO, SI YO ELIMINO EL PROGRAMA, EL ARCHIVO DE CONFIG VA A SEGUIR EXISTIENDO, EN CAMBIO SI HAGO QUE ESTE EN LA MISMA CARPETA SE BORRA TAMBIEN
    private final Preferences prefs = Preferences.userNodeForPackage(ConfigService.class); //ESTOOOOOO HAY QUE CAMBIARLO
    private final ThemeManager themeManager;

    private static final String THEME_KEY = "theme";

    public ConfigService(ThemeManager themeManager) {
        this.themeManager = themeManager;
    }

    //GUARDA config
    public void saveConfig() {
        String themeName = themeManager.getCurrentTheme().name();
        prefs.put(THEME_KEY, themeName);
        System.out.println("Configuración guardada ♡");
    }

    //CARGA config
    public void loadConfig() {
        String savedTheme = prefs.get(THEME_KEY, ThemeManager.Theme.Claro.name());
        try {
            ThemeManager.Theme theme = ThemeManager.Theme.valueOf(savedTheme);
            themeManager.setTheme(theme);
            System.out.println("Configuración cargada: " + theme.getDisplayName());
        } catch (Exception e) {
            System.err.println("Error cargando config: " + e.getMessage());
        }
    }

    //Listener para guardar cambios automaticamente
    public void setupAutoSave() {
        themeManager.themeProperty().addListener((obs, old, newVal) -> {
            saveConfig();
        });
    }
}