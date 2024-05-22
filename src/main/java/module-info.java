module com.example.hexgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hexgame to javafx.fxml;
    exports com.example.hexgame;
}