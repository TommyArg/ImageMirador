package org.dsf.imagemirador.Service;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;

public class ThemeManager {

    public enum Theme {
        Claro("light-mode", "Modo Claro"),
        Oscuro("dark-mode", "Modo Oscuro");

        private final String cssName;
        private final String displayName;

        Theme(String cssName, String displayName) {
            this.cssName = cssName;
            this.displayName = displayName;
        }

        public String getCssName() {
            return cssName;
        }
        public String getDisplayName() {
            return displayName;
        }
    }

    private final ObjectProperty<Theme> currentTheme = new SimpleObjectProperty<>(Theme.Claro);
    private Scene scene;

    private static final String CSS_RESOURCE_PATH = "/css/";

    public ThemeManager(Scene scene) {
        this.scene = scene;
        setupThemeListener();
    }

    //cuando cambia el tema se aplica solo
    private void setupThemeListener() {
        currentTheme.addListener((obs, oldTheme, newTheme) -> {
            applyTheme(newTheme);
        });
    }

    //aplica tema hotreload
    //DEJAR ESTA Y ELIMINAR LA DE DEBUG DESPUES SIIIIIIIII
    public void applyTheme(Theme theme) {
        if (scene == null) return;

        try {
            //limpia temas anteriores
            ObservableList<String> stylesheets = scene.getStylesheets();
            stylesheets.clear();

            //agregar CSS base (siempre)
            String baseCSS = getClass().getResource(CSS_RESOURCE_PATH + "base.css").toExternalForm();
            stylesheets.add(baseCSS);

            //agregar CSS del tema elegido
            String themeCSS = getClass().getResource(
                    CSS_RESOURCE_PATH + theme.getCssName() + ".css"
            ).toExternalForm();
            stylesheets.add(themeCSS);

            System.out.println("Tema aplicado: " + theme.getDisplayName() + " ♡");

        } catch (Exception e) {
            System.err.println("Error aplicando tema: " + e.getMessage());
        }
    }

    //DEBUG, capaz te sirve para algo, si no, borralo nomas
    /*
    public void applyTheme(Theme theme) {
        if (scene == null) {
            System.err.println("Scene es NULL, idiota");
            return;
        }

        try {
            ObservableList<String> stylesheets = scene.getStylesheets();
            stylesheets.clear();

            // ← DEBUG: Base CSS
            String baseCSS = getClass().getResource(CSS_RESOURCE_PATH + "base.css").toExternalForm();
            System.out.println("✓ Base CSS encontrado: " + baseCSS);
            stylesheets.add(baseCSS);

            // ← DEBUG: Theme CSS
            String themeCSS = getClass().getResource(
                    CSS_RESOURCE_PATH + theme.getCssName() + ".css"
            ).toExternalForm();
            System.out.println("✓ Theme CSS encontrado: " + themeCSS);
            System.out.println("  Buscando: " + CSS_RESOURCE_PATH + theme.getCssName() + ".css");
            stylesheets.add(themeCSS);

            System.out.println("✓ Tema aplicado: " + theme.getDisplayName() + " ♡");

        } catch (NullPointerException e) {
            System.err.println("CSS NO ENCONTRADO: " + e.getMessage());
            System.err.println("   Buscando en: " + CSS_RESOURCE_PATH);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error aplicando tema: " + e.getMessage());
            e.printStackTrace();
        }
    } */

    //cambia tema
    public void setTheme(Theme theme) {
        currentTheme.set(theme);
    }

    public Theme getCurrentTheme() {
        return currentTheme.get();
    }

    public ObjectProperty<Theme> themeProperty() {
        return currentTheme;
    }

}