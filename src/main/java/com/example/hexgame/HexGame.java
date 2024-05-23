package com.example.hexgame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

public class HexGame extends Application {

    private static final double r = 20; // hexagonun merkezinden köşesine olan mesafe
    private static final double n = Math.sqrt(r * r * 0.75); // hexagonun merkezinden kenar ortasına olan mesafe
    private static final double TILE_YUKSEKLIGI = 2 * r;
    private static final double TILE_GENISLIGI = 2 * n;

    private boolean playerTurn = true; // Oyuncu sırasını belirlemek için bir bayrak

    private Label turnLabel; // Hamle sırasını gösteren etiket

    @Override
    public void start(Stage primaryStage) {
        AnchorPane tileMap = new AnchorPane();
        VBox root = new VBox(); // Yatay yerine dikey bir düzen kullanmak için VBox kullandık.
        Scene scene = new Scene(root, 800, 600);

        // Hamle sırasını gösteren etiket
        turnLabel = new Label("Sıra: Oyuncu 1");
        turnLabel.setAlignment(Pos.CENTER);

        Button btn5x5 = new Button("5x5");
        Button btn11x11 = new Button("11x11");
        Button btn17x17 = new Button("17x17");

        btn5x5.setOnAction(event -> {
            createTileMap(tileMap, 5, 5);
            turnLabel.setText("Sıra: Oyuncu 1");
        });
        btn11x11.setOnAction(event -> {
            createTileMap(tileMap, 11, 11);
            turnLabel.setText("Sıra: Oyuncu 1");
        });
        btn17x17.setOnAction(event -> {
            createTileMap(tileMap, 17, 17);
            turnLabel.setText("Sıra: Oyuncu 1");
        });

        btn5x5.setLayoutX(40); // Değer 20 yerine 40
        btn11x11.setLayoutX(120); // Değer 100 yerine 120
        btn17x17.setLayoutX(200); // Değer 180 yerine 200

        root.getChildren().addAll(turnLabel, tileMap, btn5x5, btn11x11, btn17x17);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Hex Game");
        primaryStage.show();
    }

    private void createTileMap(AnchorPane tileMap, int row, int col) {
        tileMap.getChildren().removeIf(node -> node instanceof Tile); // Mevcut haritadaki Tile'ları kaldır

        double xBaslangicKaydirma = 40; // tüm alanı sağa kaydırma
        double yBaslangicKaydirma = 40; // tüm alanı aşağı kaydırma

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                double xKoord = x * TILE_GENISLIGI + y * n + xBaslangicKaydirma;
                double yKoord = y * TILE_YUKSEKLIGI * 0.75 + yBaslangicKaydirma;

                Tile tile = new Tile(xKoord, yKoord);
                tileMap.getChildren().add(tile);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public class Tile extends Polygon {
        private boolean isColored = false; // Altığenin boyanıp boyanmadığını belirlemek için bayrak

        public Tile(double x, double y) {
            // Köşe koordinatlarını kullanarak hexagonu oluşturur
            getPoints().addAll(
                    x, y,
                    x + n, y - r * 0.5,
                    x + TILE_GENISLIGI, y,
                    x + TILE_GENISLIGI, y + r,
                    x + n, y + r * 1.5,
                    x, y + r
            );

            // Görselleri ayarla
            setFill(Color.TRANSPARENT); // Başlangıçta her kareyi boş olarak ayarla
            setStrokeWidth(1);
            setStroke(Color.BLACK);

            // Tıklama dinleyicisi ekle
            setOnMouseClicked(event -> {
                // Eğer altıgen daha önce boyanmışsa, tıklamayı işleme
                if (isColored) {
                    return;
                }

                // Tile'ın rengini değiştir
                if (playerTurn) {
                    setFill(Color.RED);
                } else {
                    setFill(Color.BLUE);
                }
                playerTurn = !playerTurn; // Oyuncu sırasını değiştir
                isColored = true; // Altıgeni boyandı olarak işaretle
                setDisable(true); // Tıklanabilirliği kapat

                String playerTurnText = playerTurn ? "Sıra: Oyuncu 1" : "Sıra: Oyuncu 2";
                turnLabel.setText(playerTurnText);
            });
        }

        public boolean isColored() {
            return isColored;
        }
    }
}
