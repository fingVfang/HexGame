package com.example.hexgame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HexGame extends Application {

    private Label turnLabel; // Turu gösteren etiket
    private Board board; // Oyun tahtası
    private Player player1, player2; // Oyuncular

    @Override
    public void start(Stage primaryStage) {

        // Hoş geldiniz ekranı
        Label welcomeLabel = new Label("HexGame'e Hoş Geldiniz!");
        welcomeLabel.setStyle("-fx-font-size: 24; -fx-text-fill: #000;");
        Button startButton = new Button("Başla");
        startButton.setOnAction(event -> showMainScreen(primaryStage));

        Button howToPlayButton = new Button("Nasıl Oynanır?");
        howToPlayButton.setOnAction(event -> showHowToPlayDialog());

        VBox welcomeLayout = new VBox(20, welcomeLabel, startButton, howToPlayButton);
        welcomeLayout.setAlignment(Pos.CENTER);
        Scene welcomeScene = new Scene(welcomeLayout, 800, 600);

        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Hex Game");
        primaryStage.show();
    }

    private void showMainScreen(Stage primaryStage) {
        // Oyuncu isimlerini ve renklerini al
        String player1Name = getPlayerName("Oyuncu 1");
        String player2Name = getPlayerName("Oyuncu 2");
        List<javafx.scene.paint.Color> availableColors = new ArrayList<>(Utils.getAvailableColors());
        player1 = new Player(player1Name, Utils.getPlayerColor(player1Name, availableColors));
        availableColors.remove(player1.getColor());
        player2 = new Player(player2Name, Utils.getPlayerColor(player2Name, availableColors));

        // Ana düzen elemanlarını oluştur
        AnchorPane tileMap = new AnchorPane();
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        // Turu gösteren etiketi oluştur ve stilini ayarla
        turnLabel = new Label("Sıra: " + player1.getName());
        turnLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        turnLabel.setAlignment(Pos.CENTER_RIGHT);

        // Butonları oluştur ve oyun tahtasını belirli boyutlarda oluşturmak için kullan
        Button btn5x5 = new Button("5x5");
        Button btn11x11 = new Button("11x11");
        Button btn17x17 = new Button("17x17");
        btn5x5.setOnAction(event -> createBoard(tileMap, 5, 5));
        btn11x11.setOnAction(event -> createBoard(tileMap, 11, 11));
        btn17x17.setOnAction(event -> createBoard(tileMap, 17, 17));

        // "Nasıl Oynanır?" butonunu oluştur ve tıklanınca açıklama göster
        Button howToPlayButton = new Button("Nasıl Oynanır?");
        howToPlayButton.setOnAction(event -> showHowToPlayDialog());

        // Butonları bir HBox içinde düzenleyin
        HBox buttonBox = new HBox(10, btn5x5, btn11x11, btn17x17, howToPlayButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Ana düzeni oluştur ve sahneye ekle
        root.setTop(turnLabel);
        BorderPane.setAlignment(turnLabel, Pos.CENTER_RIGHT);
        root.setCenter(tileMap);
        root.setBottom(buttonBox);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Hex Game");
        primaryStage.show();
    }

    // Oyuncu adını al
    private String getPlayerName(String player) {
        TextInputDialog dialog = new TextInputDialog(player);
        dialog.setTitle("Oyuncu Adı");
        dialog.setHeaderText(null);
        dialog.setContentText(player + " adını girin:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse(player);
    }

    // Oyun tahtasını oluştur
    private void createBoard(AnchorPane tileMap, int row, int col) {
        board = new Board(tileMap, row, col, player1, player2, turnLabel);
        turnLabel.setText("Sıra: " + player1.getName());
    }

    // "Nasıl Oynanır?" açıklamasını göster
    private void showHowToPlayDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nasıl Oynanır?");
        alert.setHeaderText(null);
        alert.setContentText(
                "HexGame, iki oyunculu bir oyundur. Oyunun amacı, altıgenlerden oluşan bir tahtada\n" +
                        "karşılıklı iki kenarı birleştiren bir yol oluşturarak diğer oyuncuya engel olmaktır.\n" +
                        "Sıranız geldiğinde bir altıgen seçin ve kendi renginizle doldurun.\n" +
                        "Bir yolu tamamladığınızda, oyunu kazanırsınız.\n" +
                        "İyi eğlenceler!"
        );
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
