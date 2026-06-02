package app;

import controller.JuegoController;
import controller.MusicManager;
import database.ConexionDB;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
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

        RadialGradient oro = new RadialGradient(0, 0.5, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#8D8877")),
                new Stop(0.3, Color.web("#676452")),
                new Stop(0.7, Color.web("#474639")),
                new Stop(1, Color.web("#212118")));

        Circle circulo = new Circle(160, oro);
        circulo.setStroke(Color.web("#1e2014"));
        circulo.setStrokeWidth(5);
        circulo.setEffect(new DropShadow(25, 7, 7, Color.rgb(30, 30, 25, 0.6)));

        Image imgCara = new Image(getClass().getResourceAsStream("/img/cara.png"));
        Image imgCruz = new Image(getClass().getResourceAsStream("/img/cruz.png"));
        ImageView estrella = new ImageView(imgCara);
        ImageView cruzFig = new ImageView(imgCruz);
        estrella.setFitWidth(280); estrella.setPreserveRatio(true);
        cruzFig.setFitWidth(280); cruzFig.setPreserveRatio(true);
        cruzFig.setVisible(false);

        StackPane moneda = new StackPane(circulo, estrella, cruzFig);

        Label rachaLabel = new Label("Racha: 0");
        rachaLabel.setStyle("-fx-text-fill:white;-fx-font-size:24;-fx-font-weight:bold;" +
                "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.8),4,0,0,2);");

        Button cara = new Button("CARA");
        cara.setStyle("-fx-base:#2E7D32;-fx-font-size:14;-fx-padding:10 28;");

        Button verR = new Button("VER RANKING");
        verR.setStyle("-fx-base:#1565C0;-fx-font-size:13;-fx-padding:10 16;");

        Button cruz = new Button("CRUZ");
        cruz.setStyle("-fx-base:#C62828;-fx-font-size:14;-fx-padding:10 28;");

        for (Button b : new Button[]{cara, verR, cruz}) b.getStyleClass().add("boton-juego");

        ListView<String> ranking = new ListView<>();
        ranking.setPrefHeight(200);
        ranking.setStyle("-fx-control-inner-background:#0D1B3E;-fx-text-fill:white;" +
                "-fx-font-size:13;-fx-font-family:Consolas;" +
                "-fx-background-radius:8;");

        String nombre = System.getProperty("player.name", "Jugador");
        System.out.printf("Bienvenido %s! Consigue la racha mas larga posible!%n", nombre);

        JuegoController ctrl = new JuegoController(moneda, estrella, cruzFig, ranking, cara, cruz, verR, nombre, rachaLabel);
        cara.setOnAction(e -> ctrl.jugar("CARA"));
        cruz.setOnAction(e -> ctrl.jugar("CRUZ"));
        verR.setOnAction(e -> ctrl.mostrarRanking());

        HBox filaBotones = new HBox(12, cara, verR, cruz);
        filaBotones.setAlignment(Pos.CENTER);

        VBox raiz = new VBox(22, rachaLabel, moneda, filaBotones, ranking);
        raiz.setAlignment(Pos.CENTER);
        raiz.setStyle("-fx-padding:40;-fx-background-color:linear-gradient(to bottom,#1A237E,#283593);");

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
