package com.example.hexgame;

import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class Tile extends Polygon {
    private final Duration ANIMATION_DURATION = Duration.millis(200);
    private final Color DEFAULT_COLOR = Color.ANTIQUEWHITE;
    private final Color CLICKED_COLOR = Color.LIGHTBLUE;

    public Tile(double x, double y, double r) {
        double n = Math.sqrt(r * r * 0.75);
        double TILE_GENISLIGI = 2 * n;
        double TILE_YUKSEKLIGI = 2 * r;

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
        setFill(DEFAULT_COLOR);
        setStrokeWidth(1);
        setStroke(Color.BLACK);

        // Tıklama dinleyicisi ekle
        setOnMouseClicked(event -> {
            // Renk değişim animasyonu
            FillTransition fillTransition = new FillTransition(ANIMATION_DURATION, this, DEFAULT_COLOR, CLICKED_COLOR);
            fillTransition.setAutoReverse(true);
            fillTransition.play();
        });
    }
}
