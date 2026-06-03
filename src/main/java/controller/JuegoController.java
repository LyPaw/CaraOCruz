package controller;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.Partida;
import service.RankingService;
import java.util.ArrayList;
import java.util.Random;

public class JuegoController {
    private final StackPane moneda;
    private final ImageView estrella, cruzFig;
    private final ListView<String> ranking;
    private final Label rachaLabel;
    private final Button cara, cruz, verR;
    private final String nombre;
    private final Random rand = new Random();
    private int rachaActual = 0;
    private final RankingService service = new RankingService();
    private boolean animando = false;

    public JuegoController(StackPane moneda, ImageView estrella, ImageView cruzFig,
                           ListView<String> ranking, Button cara, Button cruz,
                           Button verR, String nombre, Label rachaLabel) {
        this.moneda = moneda; this.estrella = estrella; this.cruzFig = cruzFig;
        this.ranking = ranking; this.cara = cara; this.cruz = cruz;
        this.verR = verR; this.nombre = nombre; this.rachaLabel = rachaLabel;
    }

    public void jugar(String eleccion) {
        if (animando) return;
        bloquearBotones();

        String resultado = generarResultado();

        ScaleTransition squish = crearAnimacionSquish();
        Timeline spin = crearAnimacionSpin();

        spin.setOnFinished(e -> {
            squish.stop();
            boolean ok = evaluarYMostrarResultado(resultado, eleccion);

            ScaleTransition expand = crearAnimacionExpand(() -> {
                procesarResultado(ok);
                actualizarInterfaz(resultado, eleccion, ok);
                desbloquearBotones();
            });
            expand.play();
        });

        squish.play();
        spin.play();
    }

    public void mostrarRanking() {
        var rankingList = service.obtenerRanking();
        StringBuilder sb = new StringBuilder("=== TOP 5 RANKING ===\n");
        for (Partida p : rankingList) {
            sb.append(String.format("%-18s %d racha%n", p.getNombre(), p.getRacha()));
        }
        System.out.print(sb);
        ArrayList<String> items = new ArrayList<>();
        for (Partida p : rankingList) {
            items.add(String.format("%s - %d", p.getNombre(), p.getRacha()));
        }
        ranking.getItems().setAll(items);
    }

    private void bloquearBotones() {
        animando = true;
        cara.setDisable(true);
        cruz.setDisable(true);
    }

    private void desbloquearBotones() {
        cara.setDisable(false);
        cruz.setDisable(false);
        animando = false;
    }

    private String generarResultado() {
        return rand.nextBoolean() ? "CARA" : "CRUZ";
    }

    private ScaleTransition crearAnimacionSquish() {
        ScaleTransition s = new ScaleTransition(Duration.millis(600), moneda);
        s.setToY(0.01);
        return s;
    }

    private Timeline crearAnimacionSpin() {
        Timeline t = new Timeline();
        for (int i = 0; i < 10; i++) {
            boolean mostrar = i % 2 == 0;
            t.getKeyFrames().add(new KeyFrame(Duration.millis(65 * i), e -> {
                estrella.setVisible(mostrar);
                cruzFig.setVisible(!mostrar);
            }));
        }
        return t;
    }

    private boolean evaluarYMostrarResultado(String resultado, String eleccion) {
        boolean ok = eleccion.equals(resultado);
        estrella.setVisible(resultado.equals("CARA"));
        cruzFig.setVisible(resultado.equals("CRUZ"));
        aplicarSombraResultado(ok);
        return ok;
    }

    private void aplicarSombraResultado(boolean ok) {
        Color c = ok ? Color.web("#4CAF50", 0.6) : Color.web("#F44336", 0.6);
        moneda.setEffect(new DropShadow(15, 3, 3, c));
    }

    private ScaleTransition crearAnimacionExpand(Runnable callback) {
        ScaleTransition s = new ScaleTransition(Duration.millis(300), moneda);
        s.setToY(1);
        s.setOnFinished(e -> callback.run());
        return s;
    }

    private void procesarResultado(boolean ok) {
        if (ok) {
            rachaActual++;
        } else {
            if (rachaActual > 0) service.guardarSiMejor(nombre, rachaActual);
            rachaActual = 0;
        }
    }

    private void actualizarInterfaz(String resultado, String eleccion, boolean ok) {
        rachaLabel.setText("Racha: " + rachaActual);
        System.out.printf("[%s] %s -> %s %s (racha:%d)%n", nombre, eleccion, resultado, ok ? "OK" : "X", rachaActual);
        ArrayList<String> items = new ArrayList<>();
        for (Partida p : service.obtenerRanking()) {
            items.add(String.format("%s - %d", p.getNombre(), p.getRacha()));
        }
        ranking.getItems().setAll(items);
    }
}
