package com.example.hexgame;

import javafx.scene.paint.Color;

public class Player {

    // Boş oyuncu, kullanılmayan yerleri temsil etmek için kullanılır
    public static final Player NONE = new Player("NONE", Color.TRANSPARENT);

    private final String name; // Oyuncunun adı
    private final Color color; // Oyuncunun rengi

    // Oyuncu oluşturucu metodu
    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    // Oyuncunun adını döndüren metod
    public String getName() {
        return name;
    }

    // Oyuncunun rengini döndüren metod
    public Color getColor() {
        return color;
    }
}
