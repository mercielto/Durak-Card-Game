package com.example.cardgame.client.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreateRoomApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(CreateRoomApplication.class.getResource("createRoom.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Create room");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
