package com.example.hexgame;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MainController {

    @FXML
    private Pane boardPane;

    @FXML
    private RadioButton size5Radio;

    @FXML
    private RadioButton size11Radio;

    @FXML
    private RadioButton size17Radio;

    @FXML
    private Label currentPlayerLabel;

    private Board board;
    private Color player1Color = Color.RED;
    private Color player2Color = Color.BLUE;
    private boolean isPlayer1Turn = true;

    private ToggleGroup sizeGroup;

    @FXML
    public void initialize() {
        sizeGroup = new ToggleGroup();
        size5Radio.setToggleGroup(sizeGroup);
        size11Radio.setToggleGroup(sizeGroup);
        size17Radio.setToggleGroup(sizeGroup);

        size5Radio.setSelected(true); // Default olarak 5x5 seçili olsun
        createBoard(5);
        updateCurrentPlayerLabel();
    }

    @FXML
    public void handleChangeSize() {
        int size = 0;
        if (size5Radio.isSelected()) {
            size = 5;
        } else if (size11Radio.isSelected()) {
            size = 11;
        } else if (size17Radio.isSelected()) {
            size = 17;
        }
        boardPane.getChildren().clear();
        createBoard(size);
        updateCurrentPlayerLabel();
    }

    private void createBoard(int size) {
        board = new Board(size, size);
        board.getChildren().forEach(node -> {
            if (node instanceof Hexagon) {
                node.setOnMouseClicked(event -> handleHexagonClick((Hexagon) node));
            }
        });
        boardPane.getChildren().add(board);
    }

    private void handleHexagonClick(Hexagon hexagon) {
        Color currentPlayerColor = isPlayer1Turn ? player1Color : player2Color;
        if (hexagon.handleClick(currentPlayerColor)) {
            isPlayer1Turn = !isPlayer1Turn;
            updateCurrentPlayerLabel();
        }
    }

    @FXML
    public void handleReset() {
        int size = 0;
        if (size5Radio.isSelected()) {
            size = 5;
        } else if (size11Radio.isSelected()) {
            size = 11;
        } else if (size17Radio.isSelected()) {
            size = 17;
        }
        boardPane.getChildren().clear();
        createBoard(size);
        isPlayer1Turn = true;
        updateCurrentPlayerLabel();
    }

    private void updateCurrentPlayerLabel() {
        String currentPlayer = isPlayer1Turn ? "Player 1 (Red)" : "Player 2 (Blue)";
        currentPlayerLabel.setText("Current Player: " + currentPlayer);
    }
}
