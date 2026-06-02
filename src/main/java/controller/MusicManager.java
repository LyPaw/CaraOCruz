package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer mp;

    public static void iniciar() {
        try {
            var url = MusicManager.class.getResource("/sound/musica_fondo.wav");
            if (url == null) return;
            mp = new MediaPlayer(new Media(url.toString()));
            mp.setCycleCount(MediaPlayer.INDEFINITE);
            mp.setVolume(0.15);
            mp.play();
        } catch (Exception e) { System.err.println("Error musica: " + e.getMessage()); }
    }

    public static void detener() {
        if (mp != null) { mp.stop(); mp.dispose(); mp = null; }
    }
}
