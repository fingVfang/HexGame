package com.example.hexgame;

import javafx.animation.ScaleTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class Tile extends Polygon {

    private boolean isColored = false; // Taşın renklendirilip renklendirilmediğini belirten flag
    private Player player = Player.NONE; // Taşın sahibi olan oyuncu
    private final int xIndex, yIndex; // Taşın konumunu belirten indeksler

    // Taş oluşturucu metodu
    public Tile(int xIndex, int yIndex, double x, double y, Board board) {
        this.xIndex = xIndex;
        this.yIndex = yIndex;

        // Altıgenin köşe noktalarını ayarla
        getPoints().addAll(
                x, y,
                x + Utils.n, y - Utils.r * 0.5,
                x + Utils.TILE_GENISLIGI, y,
                x + Utils.TILE_GENISLIGI, y + Utils.r,
                x + Utils.n, y + Utils.r * 1.5,
                x, y + Utils.r
        );

        setFill(Color.TRANSPARENT); // Başlangıçta taşın rengini şeffaf olarak ayarla
        setStrokeWidth(1); // Kenar kalınlığını ayarla
        setStroke(Color.BLACK); // Kenar rengini ayarla

        // Taşa tıklandığında gerçekleştirilecek işlemi tanımla
        setOnMouseClicked(event -> {
            board.onTileClicked(this); // Oyun tahtasındaki taşa tıklandığında olayı işle
            playClickAnimation(); // Tıklama animasyonunu oynat
        });
    }

    // Taşın renklendirilip renklendirilmediğini kontrol eden metod
    public boolean isColored() {
        return isColored;
    }

    // Taşın renklendirilip renklendirilmediğini ayarlayan metod
    public void setColored(boolean colored) {
        isColored = colored;
    }

    // Taşın sahibini döndüren metod
    public Player getPlayer() {
        return player;
    }

    // Taşın sahibini ayarlayan metod
    public void setPlayer(Player player) {
        this.player = player;
    }

    // Taşın x indeksini döndüren metod
    public int getXIndex() {
        return xIndex;
    }

    // Taşın y indeksini döndüren metod
    public int getYIndex() {
        return yIndex;
    }

    // Tıklama animasyonunu oynatan metod
    private void playClickAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
        st.setByX(0.2f); // X ekseni boyunca ölçeklendirme miktarı
        st.setByY(0.2f); // Y ekseni boyunca ölçeklendirme miktarı
        st.setCycleCount(2); // Tekrar sayısı
        st.setAutoReverse(true); // Otomatik tersleme
        st.play(); // Animasyonu başlat
    }
}
