module com.example.cardgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires lombok;


    opens com.example.cardgame to javafx.fxml;
    exports com.example.cardgame;
}