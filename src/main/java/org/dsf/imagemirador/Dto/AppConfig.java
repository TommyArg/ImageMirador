package org.dsf.imagemirador.Dto;

public class AppConfig {
    private String themeName = "Claro"; // Valor por defecto
    private boolean autoplayShortVideos = false;
    private int maxShortVideoSeconds = 11;

    public String getThemeName() { return themeName; }
    public void setThemeName(String themeName) { this.themeName = themeName; }

    public boolean isAutoplayShortVideos() { return autoplayShortVideos; }
    public void setAutoplayShortVideos(boolean autoplayShortVideos) { this.autoplayShortVideos = autoplayShortVideos; }

    public int getMaxShortVideoSeconds() { return maxShortVideoSeconds; }
    public void setMaxShortVideoSeconds(int maxShortVideoSeconds) { this.maxShortVideoSeconds = maxShortVideoSeconds; }
}