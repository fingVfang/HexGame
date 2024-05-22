package com.example.hexgame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Hexagon extends Polygon {
    public static final double SIZE = 20.0;
    private boolean isClicked = false;
    private Color color;

    public Hexagon(double centerX, double centerY) {
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i);
            double x = centerX + SIZE * Math.cos(angle);
            double y = centerY + SIZE * Math.sin(angle);
            getPoints().addAll(x, y);
        }
        setFill(Color.LIGHTGRAY);
        setStroke(Color.BLACK);
    }

    public boolean handleClick(Color playerColor) {
        if (!isClicked) {
            color = playerColor;
            setFill(color);
            isClicked = true;
            return true;
        }
        return false;
    }
}
