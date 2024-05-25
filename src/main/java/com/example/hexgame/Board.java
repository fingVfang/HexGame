package com.example.hexgame;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class Board {

    private final AnchorPane tileMap; // Taş haritası için kullanılan pane
    private final int row, col; // Satır ve sütun sayıları
    private final Player player1, player2; // Oyuncular
    private final Label turnLabel; // Turu gösteren etiket
    private boolean playerTurn = true; // Şu anki oyuncu turu
    private boolean gameEnded = false; // Oyunun bitip bitmediğini kontrol etmek için bir bayrak
    private boolean firstMove = true; // İlk hamleyi takip etmek için bir bayrak

    // Kurucu metod, taş haritasını oluşturur
    public Board(AnchorPane tileMap, int row, int col, Player player1, Player player2, Label turnLabel) {
        this.tileMap = tileMap;
        this.row = row;
        this.col = col;
        this.player1 = player1;
        this.player2 = player2;
        this.turnLabel = turnLabel;
        createTileMap(); // Taş haritasını oluştur
    }

    // Taş haritasını oluşturan metod
    private void createTileMap() {
        tileMap.getChildren().clear(); // Taş haritasını temizle

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
            }
        }
    }

    // Taşa tıklandığında çağrılan metod
    public void onTileClicked(Tile tile) {
        if (tile.isColored()) return; // Eğer taş zaten renklendirilmişse işlem yapma

        Player currentPlayer = getCurrentPlayer(); // Şu anki oyuncuyu al
        tile.setFill(currentPlayer.getColor()); // Taşı renklendir
        tile.setPlayer(currentPlayer); // Taşın sahibini ayarla
        playerTurn = !playerTurn; // Oyuncu turunu değiştir
        tile.setColored(true); // Taşı renklendirildi olarak işaretle
        tile.setDisable(true); // Taşı devre dışı bırak

        // Turu gösteren etiketi güncelle
        String playerTurnText = "Sıra: " + getCurrentPlayer().getName();
        turnLabel.setText(playerTurnText);

        // Kazanma koşulunu kontrol et
        if (checkWinCondition(currentPlayer)) {
            String winner = currentPlayer.getName() + " Kazandı!";
            turnLabel.setText(winner); // Kazananı gösteren etiketi güncelle
            disableAllTiles(); // Tüm taşları devre dışı bırak
            Utils.showWinnerAnimation(tileMap, currentPlayer.getName(), currentPlayer.getColor()); // Kazanan animasyonunu göster
        }
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
            return;
        }

        // İlk hamle yapıldıktan sonra takas işlemi için kontrol et
        if (firstMove) {
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
        if (!firstMove) {
            return;
        }

        // İkinci oyuncunun taşını seçme diyalogunu göster
        showSwapDialog();
    }

    // Takas işlemi için diyalog gösteren metod
    private void showSwapDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Takas Yap");
        alert.setHeaderText(null);
        alert.setContentText("İlk taşı oynadınız. Taşları takas etmek ister misiniz?");

        ButtonType buttonTypeYes = new ButtonType("Evet");
        ButtonType buttonTypeNo = new ButtonType("Hayır");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            playerTurn = !playerTurn;
            String playerTurnText = "Sıra: " + getCurrentPlayer().getName();
            turnLabel.setText(playerTurnText);
        }

        firstMove = false; // İlk hamle yapıldı olarak işaretle
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
