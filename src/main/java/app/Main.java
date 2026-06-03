package app;

import controller.JuegoController;
import controller.MusicManager;
import database.ConexionDB;
import service.ClasificadorSGBD;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage v) {
        ConexionDB.crearTabla();
        MusicManager.iniciar();
        ClasificadorSGBD.analizar();

        Circle circulo = new Circle(160);
        circulo.getStyleClass().add("moneda-circulo");

        Image imgCara = new Image(getClass().getResourceAsStream("/img/cara.png"));
        Image imgCruz = new Image(getClass().getResourceAsStream("/img/cruz.png"));
        ImageView estrella = new ImageView(imgCara);
        ImageView cruzFig = new ImageView(imgCruz);
        estrella.setFitWidth(280); estrella.setPreserveRatio(true);
        cruzFig.setFitWidth(280); cruzFig.setPreserveRatio(true);
        cruzFig.setVisible(false);

        StackPane moneda = new StackPane(circulo, estrella, cruzFig);

        Label rachaLabel = new Label("Racha: 0");
        rachaLabel.getStyleClass().add("racha-label");

        Button cara = new Button("CARA");
        cara.getStyleClass().addAll("boton-juego", "btn-cara");

        Button verR = new Button("VER RANKING");
        verR.getStyleClass().addAll("boton-juego", "btn-ranking");

        Button cruz = new Button("CRUZ");
        cruz.getStyleClass().addAll("boton-juego", "btn-cruz");

        ListView<String> ranking = new ListView<>();
        ranking.setPrefHeight(200);
        ranking.getStyleClass().add("ranking-list");

        String nombre = System.getProperty("player.name", "Jugador");
        System.out.printf("Bienvenido %s! Consigue la racha mas larga posible!%n", nombre);

        JuegoController ctrl = new JuegoController(moneda, estrella, cruzFig, ranking, cara, cruz, verR, nombre, rachaLabel);
        cara.setOnAction(e -> ctrl.jugar("CARA"));
        cruz.setOnAction(e -> ctrl.jugar("CRUZ"));
        verR.setOnAction(e -> ctrl.mostrarRanking());

        HBox filaBotones = new HBox(12, cara, verR, cruz);
        filaBotones.getStyleClass().add("fila-botones");
        VBox raiz = new VBox(22, rachaLabel, moneda, filaBotones, ranking);
        raiz.getStyleClass().add("root-panel");

        Scene escena = new Scene(raiz, 500, 600);
        escena.getStylesheets().add(getClass().getResource("/css/estilo.css").toExternalForm());

        v.setScene(escena);
        v.setFullScreen(true);
        v.setTitle("Cara o Cruz");
        v.getIcons().add(imgCara);
        v.show();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("=== CARA O CRUZ ===\nNombre: ");
        String n = sc.nextLine().trim();
        if (n.isEmpty()) n = "Jugador";
        System.setProperty("player.name", n);
        launch(args);
    }
}
