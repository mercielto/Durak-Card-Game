package com.example.cardgame.client.application;

import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.StageSingleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void init() throws Exception {
        ClientSingleton.getClient();
    }

    @Override
    public void start(Stage stage) throws IOException {
        StageSingleton.setStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}