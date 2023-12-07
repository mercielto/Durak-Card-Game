package com.example.cardgame.client.application;

import com.example.cardgame.client.StageSingleton;
import com.example.cardgame.client.service.FxmlObjectsGetter;
import com.example.cardgame.properties.FxmlObjectProperties;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

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
