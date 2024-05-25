package com.example.hexgame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class Board {

    private final AnchorPane tileMap; // Taş haritası için kullanılan pane
    private final int row, col; // Satır ve sütun sayıları
    private final Player player1, player2; // Oyuncular
    private final Label turnLabel; // Turu gösteren etiket
    private boolean playerTurn = true; // Şu anki oyuncu turu
    private boolean gameEnded = false; // Oyunun bitip bitmediğini kontrol etmek için bir bayrak
    private boolean secondPlayerFirstMove = true; // İkinci oyuncunun ilk hamlesini takip etmek için bir bayrak
    private List<Tile> cornerTiles; // Köşe taşları listesi
    private Timeline colorChangeTimeline; // Renk değiştirme zaman çizelgesi

    // Kurucu metod, taş haritasını oluşturur
    public Board(AnchorPane tileMap, int row, int col, Player player1, Player player2, Label turnLabel) {
        this.tileMap = tileMap;
        this.row = row;
        this.col = col;
        this.player1 = player1;
        this.player2 = player2;
        this.turnLabel = turnLabel;
        createTileMap(); // Taş haritasını oluştur
        startCornerTileColorChange(); // Köşe taşlarının rengini değiştirmeyi başlat
    }

    // Taş haritasını oluşturan metod
    private void createTileMap() {
        tileMap.getChildren().clear(); // Taş haritasını temizle
        cornerTiles = new ArrayList<>(); // Köşe taşları listesini başlat

        // Taşların başlangıç koordinatlarını hesapla
        double xBaslangicKaydirma = 40;
        double yBaslangicKaydirma = 40;

        for (int y = 0; y < row; y++) {
            for (int x = 0; x < col; x++) {
                // Taşların koordinatlarını hesapla ve taşları oluştur
                double xKoord = x * Utils.TILE_GENISLIGI + y * Utils.n + xBaslangicKaydirma;
                double yKoord = y * Utils.TILE_YUKSEKLIGI * 0.75 + yBaslangicKaydirma;

                Tile tile = new Tile(x, y, xKoord, yKoord, this);
                tileMap.getChildren().add(tile); // Taşı haritaya ekle

                // Sadece dış kenarlardaki taşların kenarlarını renklendir
                if (isCornerTile(x, y)) {
                    cornerTiles.add(tile); // Köşe taşlarını listeye ekle
                }
                if (y == 0 || y == row - 1 || x == 0 || x == col - 1) {
                    Color strokeColor = getEdgeColor(x, y);
                    tile.setStrokeColor(strokeColor);
                }
            }
        }
    }

    // Kenarlardaki taşların renklerini belirleyen metod
    private Color getEdgeColor(int x, int y) {
        if (y == 0 || y == row - 1) {
            return player1.getColor(); // Üst ve alt kenarlar için Player 1'in rengi
        } else if (x == 0 || x == col - 1) {
            return player2.getColor(); // Sağ ve sol kenarlar için Player 2'nin rengi
        }
        return Color.BLACK; // Diğer durumlar için siyah (varsayılan)
    }

    // Köşe taşları belirleyen metod
    private boolean isCornerTile(int x, int y) {
        return (x == 0 && y == 0) || (x == 0 && y == row - 1) || (x == col - 1 && y == 0) || (x == col - 1 && y == row - 1);
    }

    // Köşe taşlarının rengini değiştiren zaman çizelgesini başlatan metod
    private void startCornerTileColorChange() {
        colorChangeTimeline = new Timeline(new KeyFrame(Duration.seconds(1.5), event -> changeCornerTileColors()));
        colorChangeTimeline.setCycleCount(Timeline.INDEFINITE); // Süresiz döngü
        colorChangeTimeline.play(); // Zaman çizelgesini başlat
    }

    // Köşe taşlarının renklerini değiştiren metod
    private void changeCornerTileColors() {
        for (Tile tile : cornerTiles) {
            Color currentColor = (Color) tile.getStroke();
            Color newColor = currentColor.equals(player1.getColor()) ? player2.getColor() : player1.getColor();
            tile.setStrokeColor(newColor);
        }
    }

    // Taşa tıklandığında çağrılan metod
    public void onTileClicked(Tile tile) {
        if (tile.isColored()) return; // Eğer taş zaten renklendirilmişse işlem yapma

        makeMove(tile);
    }

    // Hamleyi gerçekleştiren metod
    private void makeMove(Tile tile) {
        if (gameEnded) {
            return;
        }

        // Şu anki oyuncuyu al
        Player currentPlayer = getCurrentPlayer();

        // Taşı renklendir ve oyuncuyu ayarla
        tile.setFill(currentPlayer.getColor());
        tile.setPlayer(currentPlayer);
        tile.setColored(true);
        tile.setDisable(true);

        // Kazanma koşulunu kontrol et
        if (checkWinCondition(currentPlayer)) {
            String winner = currentPlayer.getName() + " Kazandı!";
            turnLabel.setText(winner);
            disableAllTiles();
            Utils.showWinnerAnimation(tileMap, currentPlayer.getName(), currentPlayer.getColor());
            gameEnded = true;
            colorChangeTimeline.stop(); // Oyunu kazanıldığında renk değiştirme animasyonunu durdur
            return;
        }

        // İkinci oyuncunun ilk hamlesinden sonra takas işlemi için kontrol et
        if (!playerTurn && secondPlayerFirstMove) {
            swapTiles();
        } else {
            // Oyuncu turunu değiştir
            playerTurn = !playerTurn;
            String playerTurnText = "Sıra: " + getCurrentPlayer().getName();
            turnLabel.setText(playerTurnText);
        }
    }

    // Taşları takas eden metod
    private void swapTiles() {
        if (!secondPlayerFirstMove) {
            return;
        }

        // Takas işlemi için diyalog göster
        showSwapDialog();
    }

    // Takas işlemi için diyalog gösteren metod
    private void showSwapDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Takas Yap");
        alert.setHeaderText(null);
        alert.setContentText("İkinci oyuncu ilk taşını oynadı. Taşları takas etmek ister misiniz?");

        ButtonType buttonTypeYes = new ButtonType("Evet");
        ButtonType buttonTypeNo = new ButtonType("Hayır");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            swapStones();
        }

        // Sıra her iki durumda da 1. oyuncuya geçmeli
        playerTurn = true;
        String playerTurnText = "Sıra: " + getCurrentPlayer().getName();
        turnLabel.setText(playerTurnText);

        secondPlayerFirstMove = false; // İkinci oyuncunun ilk hamlesi yapıldı olarak işaretle
    }

    // Taşları takas eden metod
    private void swapStones() {
        for (var node : tileMap.getChildren()) {
            if (node instanceof Tile) {
                Tile tile = (Tile) node;
                Player currentPlayer = tile.getPlayer();
                // İlk oyuncunun taşını bul
                if (currentPlayer == player1) {
                    // İlk oyuncunun taşını ikinci oyuncunun taşıyla değiştir
                    tile.setPlayer(player2);
                    tile.setFill(player2.getColor());
                } else if (currentPlayer == player2) {
                    // İkinci oyuncunun taşını birinci oyuncunun taşıyla değiştir
                    tile.setPlayer(player1);
                    tile.setFill(player1.getColor());
                }
            }
        }
    }

    // Şu anki oyuncuyu döndüren metod
    private Player getCurrentPlayer() {
        return playerTurn ? player1 : player2;
    }

    // Kazanma koşulunu kontrol eden metod
    private boolean checkWinCondition(Player player) {
        return player == player1 ? checkWinConditionHorizontal(player) : checkWinConditionVertical(player);
    }

    // Yatay kazanma koşulunu kontrol eden metod
    private boolean checkWinConditionHorizontal(Player player) {
        for (int x = 0; x < col; x++) {
            if (dfs(player, new boolean[row][col], x, 0)) {
                return true;
            }
        }
        return false;
    }

    // Dikey kazanma koşulunu kontrol eden metod
    private boolean checkWinConditionVertical(Player player) {
        for (int y = 0; y < row; y++) {
            if (dfs(player, new boolean[row][col], 0, y)) {
                return true;
            }
        }
        return false;
    }

    // Derinlik öncelikli arama algoritması ile kazanma koşulunu kontrol eden metod
    private boolean dfs(Player player, boolean[][] visited, int x, int y) {
        if (x < 0 || y < 0 || x >= col || y >= row || visited[y][x]) return false;

        Tile tile = getTile(x, y);
        if (tile == null || tile.getPlayer() != player) return false;

        visited[y][x] = true;

        if ((player == player1 && y == row - 1) || (player == player2 && x == col - 1)) {
            return true;
        }

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, -1}};
        for (int[] dir : directions) {
            if (dfs(player, visited, x + dir[0], y + dir[1])) {
                return true;
            }
        }
        return false;
    }

    // Belirli bir konumda bulunan taşı döndüren metod
    private Tile getTile(int x, int y) {
        for (var node : tileMap.getChildren()) {
            if (node instanceof Tile) {
                Tile tile = (Tile) node;
                if (tile.getXIndex() == x && tile.getYIndex() == y) {
                    return tile;
                }
            }
        }
        return null;
    }

    // Tüm taşları devre dışı bırakan metod
    private void disableAllTiles() {
        for (var node : tileMap.getChildren()) {
            if (node instanceof Tile) {
                node.setDisable(true);
            }
        }
    }
}
