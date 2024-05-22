package com.example.hexgame;

import javafx.scene.layout.Pane;

public class Board extends Pane {

    public Board(int rows, int cols) {
        double hexSize = Hexagon.SIZE;
        double hexHeight = Math.sqrt(3) * hexSize;
        double hexWidth = 2 * hexSize;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = col * hexWidth * 0.75;
                double y = row * hexHeight + (col % 2) * (hexHeight / 2);
                Hexagon hex = new Hexagon(x, y);
                getChildren().add(hex);
            }
        }
    }
}
