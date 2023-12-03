module com.example.cardgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;


    opens com.example.cardgame to javafx.fxml;
    exports com.example.cardgame;
    exports com.example.cardgame.client.application;
    opens com.example.cardgame.client.application to javafx.fxml;
    exports com.example.cardgame.client.controller;
    opens com.example.cardgame.client.controller to javafx.fxml;
}