package com.example.hexgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HexGame extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/hexgame/MainScene.fxml");
            if (fxmlLocation == null) {
                throw new IOException("FXML file not found.");
            }
            Parent root = FXMLLoader.load(fxmlLocation);
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Hex Game");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
