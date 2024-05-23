package com.example.hexgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class HexGame extends Application {

    private static final double r = 20; // hexagonun merkezinden köşesine olan mesafe
    private static final double n = Math.sqrt(r * r * 0.75); // hexagonun merkezinden kenar ortasına olan mesafe
    private static final double TILE_YUKSEKLIGI = 2 * r;
    private static final double TILE_GENISLIGI = 2 * n;

    private boolean playerTurn = true; // Oyuncu sırasını belirlemek için bir bayrak

    @Override
    public void start(Stage primaryStage) {
        AnchorPane tileMap = new AnchorPane();
        Scene scene = new Scene(tileMap, 950, 600);

        Button btn5x5 = new Button("5x5");
        Button btn11x11 = new Button("11x11");
        Button btn17x17 = new Button("17x17");

        btn5x5.setLayoutX(23);
        btn5x5.setLayoutY(530);
        btn11x11.setLayoutX(60);
        btn11x11.setLayoutY(530);
        btn17x17.setLayoutX(110);
        btn17x17.setLayoutY(530);

        btn5x5.setOnAction(event -> createTileMap(tileMap, 5, 5));
        btn11x11.setOnAction(event -> createTileMap(tileMap, 11, 11));
        btn17x17.setOnAction(event -> createTileMap(tileMap, 17, 17));

        tileMap.getChildren().addAll(btn5x5, btn11x11, btn17x17);

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

            });
        }

        public boolean isColored() {
            return isColored;
        }
    }
}
